
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.alarm;

import java.lang.reflect.Field;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.csstudio.alarm.beast.JMSCommunicationThread;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class to help close the connection in the css alarm system. CSS will not
 * close the connection to active MQ so we have to do it in here by reflection.
 * This is a bit hacky and if it breaks it is likely that the alarm client model
 * has changed. If the connection is not closed the app will not crash but it is
 * using extra resources.
 * 
 * NB it is hard to test this class because it is not possible to create a
 * AlarmClientModel and JMSConnection without creating a connection.
 */
public class AlarmConnectionCloser {

    /** Connection which will be closed on close. */
    private Connection connection = null;

    /** logger. */
    private static final Logger LOG = IsisLog.getLogger(Alarm.class);

    /**
     * Create a closer for the alarm connection, use the connection which is in
     * the alarm client model at the time of creation.
     * 
     * @param alarmClientModel the alarm client model which contains the
     *            connection which must be closed.
     */
    public AlarmConnectionCloser(AlarmClientModel alarmClientModel) {

        // Access alarmModel.communicator which is of type AlarmClientCommunicator
        Object communicator;
        try {
            Field field = AlarmClientModel.class.getDeclaredField("communicator");
            field.setAccessible(true);
            communicator = field.get(alarmClientModel);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
            LOG.warn("While getting reference to the alarm model 'communicator' we had an error. ");
            LOG.catching(Level.WARN, e1);
            return;
        }
        
        if (communicator == null) {
            // There is no error but the communicator is null so we do not need
            // to close it.
            return;
        }

        // If we found the communicator pull out the connection from it.
        try {
            Field connectionField = JMSCommunicationThread.class.getDeclaredField("connection");
            connectionField.setAccessible(true);
            connection = (Connection) connectionField.get(communicator);

        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            LOG.warn("While getting reference to the connection from the communicator we had an error. ");
            LOG.catching(Level.WARN, e);
            return;
        }
    }

    /**
     * Close the connection.
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                LOG.warn("Exception thrown will closing the alarm connection. ");
                LOG.catching(Level.WARN, e);
            }
        } else {
            LOG.debug("No active MQ connection to close.");
        }
    }

}

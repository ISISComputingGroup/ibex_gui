/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.log.preferences;

/**
 * Constant definitions for plug-in preferences.
 */
public final class PreferenceConstants {
    /** The port to connect to for reading the log messages from jms. **/
    public static final String P_JMS_PORT = "jmsPort";
    /** The ActiveMQ topic that the messages arrive on. **/
    public static final String P_JMS_TOPIC = "jmsTopic";
    
    /** The username to connect to the log database. **/
    public static final String P_MESSAGE_SQL_USERNAME = "sqlMessageUsername";
    /** The password to connect to the log database. **/
    public static final String P_MESSAGE_SQL_PASSWORD = "sqlMessagePassword";
    /** The schema to connect to the log database. **/
    public static final String P_MESSAGE_SQL_SCHEMA = "sqlMessageSchema";
    
    /** Whether or not minor messages count to the log counter. **/
    public static final String P_MINOR_MESSAGE = "msgMinor";

    /** The default for the JMS port. **/
    public static final Integer DEFAULT_JMS_PORT = 61616;
    /** The default for the JMS topic. **/
    public static final String DEFAULT_JMS_TOPIC = "iocLogs";
    
    /** The default for the database username. **/
    public static final String DEFAULT_MESSAGE_SQL_USERNAME = "msg_report";
    /** The default for the database password. **/
    public static final String DEFAULT_MESSAGE_SQL_PASSWORD = "$msg_report";
    /** The default for the database schema. **/
    public static final String DEFAULT_MESSAGE_SQL_SCHEMA = "msg_log";
    /** The default for whether to include minor messages. **/
    public static final Boolean DEFAULT_MINOR_MESSAGE = false;
    
    private PreferenceConstants() {
    }
}

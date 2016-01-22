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

package uk.ac.stfc.isis.ibex.alarm;

import org.apache.logging.log4j.Logger;
import org.csstudio.alarm.beast.Messages;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModelListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class which provides an interaction between the perspective switcher and the alarm system
 */
public class Alarm extends AbstractUIPlugin {

    private static final int MAX_RETRIES = 100;
    private static final int WAIT_FOR_MODEL = 100;

	private static final Logger LOG = IsisLog.getLogger(Alarm.class);
	
	private static BundleContext context;
	private static Alarm instance;
	
	public static Alarm getInstance() {
		return instance;
	    }

	public static Alarm getDefault() {
		return instance;
	}

    private AlarmClientModel alarmModel;
	private AlarmCounter counter;
	AlarmClientModelListener listener;
	
	public Alarm() {
		super();
		instance = this;
		try {
			alarmModel = AlarmClientModel.getInstance();
		} catch (Exception e) {
			LOG.info("Alarm Client Model not found");
		}
		counter = new AlarmCounter(alarmModel);
	}

    public AlarmClientModel getAlarmModel() {
        return alarmModel;
    }

	static BundleContext getContext() {
		return context;
	}
	
	/**
	 * @return the counter being used
	 */
	public AlarmCounter getCounter() {
		return counter;
	}
	
    /**
     * This should be called after closing the alarm view, else the instance of
     * the alarm model will be held on to by BEAST.
     */
    public void releaseAlarm() {
        alarmModel.release();
    }

    /**
     * Run the alarm model update in the background, to ensure the button is
     * showing the correct number of alarms. This is made difficult by the fact
     * AlarmClientModel.getInstance() will return something that may not yet be
     * initialised.
     */
    public void updateAlarmModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                counter.resetCount();

                try {
                    alarmModel = AlarmClientModel.getInstance();
                } catch (Exception e) {
                    LOG.info("Alarm Client Model not found");
                    return;
                }
                
                // The alarm model counter runs on a separate thread so may not
                // be initialised immediately. Wait for a bit (up to 10 s) here
                // to see if it gets initialised.
                int i = 0;
                while (alarmModel.toString().contains(Messages.AlarmClientModel_NotInitialized) && i < MAX_RETRIES) {
                    try {
                        Thread.sleep(WAIT_FOR_MODEL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }

                counter.forceRefresh();
            }
        }).start();
    }
}

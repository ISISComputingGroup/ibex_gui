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
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class which provides an interaction between the perspective switcher and the
 * alarm system.
 */
public class Alarm extends Plugin implements InstrumentInfoReceiver {

	private static final Logger LOG = IsisLog.getLogger(Alarm.class);
	
	private static BundleContext context;
	private static Alarm instance;

    /** The alarmModel is a singleton so have a single copy. */
    private static AlarmClientModel alarmModel = null;

    /**
     * Alarm counter is based on the alarmModle so there is a single copy this.
     */
    private static AlarmCounter counter = new AlarmCounter(null);
    private AlarmSettings alarmSettings = new AlarmSettings();
    private AlarmConnectionCloser alarmConnectionCloser = null;
	
    /**
     * @return the instance of this singleton.
     */
	public static Alarm getInstance() {
        if (instance == null) {
            throw new RuntimeException(
                    "Alarms view gets created by eclipse but getInstance was called before it was created.");
        }
		return instance;
    }

    /**
     * @return the default alarm instance
     */
	public static Alarm getDefault() {
        return getInstance();
	}

    /**
     * The default constructor for this singleton, sets up the backend model and
     * counter to monitor the alarms.
     */
    public Alarm() {
		super();
		instance = this;
        setupAlarmModel();

    }

    /**
     * Reloads the information in the alarm tree view.
     */
    public void reload() {
        try {
            // Passing null reloads everything.
            alarmModel.readConfig(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If there is no alarm model then setup a new alarm model and counter.
     */
    private void setupAlarmModel() {
        try {
            if (alarmModel == null) {
                alarmModel = AlarmClientModel.getInstance();
                counter.setAlarmModel(alarmModel);
            }
		} catch (Exception e) {
			LOG.info("Alarm Client Model not found");
		}
    }

    /**
     * Stop the bundle releasing all the resources.
     * 
     * @param context context of the bundle
     * @throws Exception if something goes wrong
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        AlarmConnectionCloser alarmConnectionCloser = releaseAlarm();
        alarmConnectionCloser.close();
    }

    /**
     * Gets the context for the bundle.
     *
     * @return the context
     */
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
     * the alarm model will be held on to by BEAST. This returns a connection
     * closer which should be closed as late as possible because the rest of the
     * connection is closed on a separate thread; give it a chance to close
     * first.
     * 
     * @return an active MQ connection closer
     */
    private AlarmConnectionCloser releaseAlarm() {

        AlarmConnectionCloser alarmConnectionCloser = new AlarmConnectionCloser(alarmModel);
        alarmModel.release();
        alarmModel = null;
        counter.setAlarmModel(null);

        return alarmConnectionCloser;
    }

    /**
     * Set up the alarm model default value and create the new alarm model.
     * 
     * @param instrument that is being changed to
     */
    @Override
    public void setInstrument(InstrumentInfo instrument) {
        alarmSettings.setInstrument(instrument);
        setupAlarmModel();
    }

    /**
     * Close the current alarm model.
     * 
     * @param instrument that is being changed to
     */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        alarmConnectionCloser = releaseAlarm();
    }

    /**
     * Clean up for the active MQ.
     * 
     * @param instrument that has been changed to
     */
    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        // close the active MQ as late as possible
        if (alarmConnectionCloser != null) {
            alarmConnectionCloser.close();
            alarmConnectionCloser = null;
        }
    }

}

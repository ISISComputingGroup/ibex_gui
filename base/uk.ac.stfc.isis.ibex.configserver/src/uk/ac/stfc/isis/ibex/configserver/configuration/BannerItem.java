
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Instrument specific property displayed in spangle banner.
 * 
 * CREATED FROM JSON therefore uses snake case
 */
public class BannerItem extends ModelObject {

	private int index;
    private String name;
    private String pv;
    private Boolean local = true;
    private int width;

    private transient String currentValue = null;
    private transient AlarmState currentAlarmState = AlarmState.UNDEFINED;

    private transient ForwardingObservable<String> pvObservable;
    private transient ForwardingObservable<AlarmState> alarmObservable;

    /**
     * Creates an observable for the PV holding the current state of this banner
     * item.
     */
    public void createPVObservable() {
        String pv = this.pv;
        if (local) {
            pv = InstrumentUtils.addPrefix(pv);
        }

        ObservableFactory observableFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);

        pvObservable = observableFactory.getSwitchableObservable(new DefaultChannel(), pv);
        alarmObservable = observableFactory.getSwitchableObservable(new EnumChannel<>(AlarmState.class), pv + ".SEVR");
        pvObservable.subscribe(valueAdapter);
        alarmObservable.subscribe(alarmAdapter);
    }

    private final BaseObserver<String> valueAdapter = new BaseObserver<String>() {

        @Override
        public void onValue(String value) {
            setCurrentValue(value);
        }

        @Override
        public void onError(Exception e) {
            setCurrentValue(null);
            IsisLog.getLogger(getClass()).error("Exception in banner item (value) state adapter: " + e.getMessage());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setCurrentValue(null);
            }
        }
    };

    private final BaseObserver<AlarmState> alarmAdapter = new BaseObserver<AlarmState>() {

        @Override
        public void onValue(AlarmState alarm) {
            IsisLog.getLogger(getClass()).info("New alarm state in BannerItem " + alarmObservable + ": " + alarm);
            setCurrentAlarm(alarm);
        }

        @Override
        public void onError(Exception e) {
            setCurrentAlarm(AlarmState.INVALID);
            IsisLog.getLogger(getClass()).error("Exception in banner item (alarm) state adapter: " + e.getMessage());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setCurrentAlarm(AlarmState.INVALID);
            }
        }
    };

    /**
     * @return the index which indicates the order that elements on the banner should be displayed
     */
    public int index() {
        return index;
    }

    /**
     * Returns the display name of this banner item.
     * 
     * @return the display name of this banner item.
     */
    public String name() {
        return name;
    }

    /**
     * Returns the current value of this banner item.
     * 
     * @return the current value of this banner item.
     */
    public String value() {
        return currentValue;
    }

    /**
     * Returns the alarm status of this banner item.
     * 
     * @return the alarm status of this banner item.
     */
    public AlarmState alarm() {
        return currentAlarmState;
    }
    
    /**
     * @return the width of the item.
     */
    public int width() {
        return width;
    }

    /**
     * Sets the current state of the property based on the PV value and fires a
     * property change for listeners.
     * 
     * @param value
     *            the state value of the property.
     */
    public synchronized void setCurrentValue(String value) {
        firePropertyChange("value", this.currentValue, this.currentValue = value);
    }

    /**
     * Sets the current state of the alarm based on the PV value and fires a
     * property change for listeners.
     * 
     * @param alarm
     *            the alarm state to be set
     */
    public synchronized void setCurrentAlarm(AlarmState alarm) {
        firePropertyChange("alarm", this.currentAlarmState, this.currentAlarmState = alarm);
    }

}

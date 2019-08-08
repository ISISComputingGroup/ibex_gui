
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

package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.configserver.displaying.BlockState;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A component property that reads its value from a PV.
 */
public class ReadableComponentProperty extends ComponentProperty {

    private final UpdatedValue<String> value;

    /**
     * Specifies the block state, such as disconnected or under major alarm.
     */
    private BlockState pvState = BlockState.DEFAULT;

    /**
     * @param displayName display name of the component property
     * @param valueObservable an observable for the value
     * @param alarmObservable an observable for the alarm
     */
    public ReadableComponentProperty(String displayName, ForwardingObservable<String> valueObservable,
            ForwardingObservable<AlarmState> alarmObservable) {
        super(displayName);
        this.value = new TextUpdatedObservableAdapter(valueObservable) {
            @Override
            public void connectionChanged(boolean isConnected) {
                super.connectionChanged(isConnected);
                if (!isConnected) {
                    setPvState(BlockState.DISCONNECTED);
                }
            }
        };
        alarmObservable.addObserver(alarmAdapter);
    }

    /**
     * @return String an UpdatedValue containing the value
     */
    public UpdatedValue<String> value() {
        return value;
    }

    private final BaseObserver<AlarmState> alarmAdapter = new BaseObserver<AlarmState>() {

        @Override
        public void onValue(AlarmState value) {
            BlockState state = BlockState.DEFAULT;
            if (value.name().equals("MINOR")) {
                state = BlockState.MINOR_ALARM;
            } else if (value.name().equals("MAJOR")) {
                state = BlockState.MAJOR_ALARM;
            } else if (value.name().equals("INVALID")) {
                state = BlockState.DISCONNECTED;
            }

            setPvState(state);
        }

        @Override
        public void onError(Exception e) {
            BlockState state = BlockState.MINOR_ALARM;
            setPvState(state);
        }
    };

    /**
     * @return the overall PV status.
     */
    public BlockState getPvState() {
        return pvState;
    }

    private void setPvState(BlockState pvState) {
        firePropertyChange("pvState", this.pvState, this.pvState = pvState);
    }
}

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
package uk.ac.stfc.isis.ibex.managermode;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 *
 */
public abstract class ManagerModeObserver implements Closable {
    private Subscription subscription;

    private final Observer<Boolean> observer = new BaseObserver<Boolean>() {
        @Override
        public void onValue(Boolean value) {
            setManagerMode(value);
        }

        @Override
        public void onError(Exception e) {
            setUnknown();
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setUnknown();
            }
        }
    };

    protected final SettableUpdatedValue<String> text;
    protected final SettableUpdatedValue<Color> color;
    protected final SettableUpdatedValue<Boolean> availability;

    public ManagerModeObserver(
            ForwardingObservable<Boolean> observable, String selfID) {
        text = new SettableUpdatedValue<>();
        color = new SettableUpdatedValue<>();
        availability = new SettableUpdatedValue<>();
        
        subscription = observable.addObserver(observer);
    }

    @Override
    public void close() {
        subscription.removeObserver();
    }

    protected abstract void setManagerMode(Boolean value);

    protected abstract void setUnknown();

    public UpdatedValue<String> text() {
        return text;
    }

    public UpdatedValue<Color> color() {
        return color;
    }

    public UpdatedValue<Boolean> availability() {
        return availability;
    }

}

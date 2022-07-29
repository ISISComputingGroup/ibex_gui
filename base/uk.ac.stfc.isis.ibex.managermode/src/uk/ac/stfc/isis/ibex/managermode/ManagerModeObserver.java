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

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

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

    /**
     * Constructor for a manager mode observer.
     * @param observable the observable to use
     */
    public ManagerModeObserver(
            ForwardingObservable<Boolean> observable) {
        
        subscription = observable.subscribe(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        subscription.cancelSubscription();
    }

    /**
     * Called when the manager mode PV updates.
     * @param value the new status of manager mode
     */
    protected abstract void setManagerMode(Boolean value);

    /**
     * Called when the manager mode PV becomes disconnected.
     */
    protected abstract void setUnknown();

}

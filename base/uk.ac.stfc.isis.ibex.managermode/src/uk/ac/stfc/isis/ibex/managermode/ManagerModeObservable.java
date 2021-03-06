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

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;

/**
 *
 */
public class ManagerModeObservable {
    private final ObservableFactory obsFactory;

    /**
     * The observable.
     */
    public final ForwardingObservable<Boolean> observable;

    /**
     * Only instantiated from within this package.
     * 
     * Get the shared instance from ManagerModeModel instead.
     */
    ManagerModeObservable() {
        obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
        observable = obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix("CS:MANAGER"));
    }

    /**
     * Only instantiated from within this package.
     * 
     * Get the shared instance from ManagerModeModel instead.
     * 
     * @param observable an observable to use
     */
    ManagerModeObservable(ForwardingObservable<Boolean> observable) {
        obsFactory = null;
        this.observable = observable;
    }
}

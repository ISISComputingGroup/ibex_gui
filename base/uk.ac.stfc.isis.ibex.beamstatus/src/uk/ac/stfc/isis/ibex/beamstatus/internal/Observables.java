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
package uk.ac.stfc.isis.ibex.beamstatus.internal;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A class to hold things useful for all observables in the beam status plugin.
 */
public abstract class Observables extends ModelAdapter {
    /**
     * The beam status PVs are global so they do not need to be switched.
     */
    protected final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.NOTHING);

    protected static final String BEAM_CURRENT = "BEAM:CURR";
    protected static final String FREQ = "FREQ";

    /**
     * Helper method to adapt a forwarding observable to an updated value whilst
     * converting the returned number to a string.
     * 
     * @param observable
     *            The observable to adapt and convert.
     * @return The converted updated value.
     */
    protected UpdatedValue<String> adaptNumber(ForwardingObservable<Number> observable) {
        return adapt(convert(observable, new NumberConverter()));
    }

    /**
     * Helper method to adapt a forwarding observable to an updated value whilst
     * converting the returned Enum to a string.
     * 
     * @param observable
     *            The observable to adapt and convert.
     * @return The converted updated value.
     */
    protected <E extends Enum<E>> UpdatedValue<String> adaptEnum(ForwardingObservable<E> observable) {
        return adapt(convert(observable, new EnumConverter<E>()));
    }
}

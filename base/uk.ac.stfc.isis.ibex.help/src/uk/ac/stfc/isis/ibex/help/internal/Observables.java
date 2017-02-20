
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

package uk.ac.stfc.isis.ibex.help.internal;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds all of the observables for the server versioning.
 */
public class Observables {
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    /**
     * An observable for the server version number.
     */
    public final ForwardingObservable<String> serverRevision;
    /**
     * An observable for the server version's date.
     */
    public final ForwardingObservable<String> serverDate;

    /**
     * Constructor that hooks the class to the PVs that contain the relevant
     * data.
     */
    public Observables() {
        serverRevision = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix("CS:VERSION:SVN:REV"));
        serverDate = obsFactory.getSwitchableObservable(new StringChannel(),
                InstrumentUtils.addPrefix("CS:VERSION:SVN:DATE"));
	}
}

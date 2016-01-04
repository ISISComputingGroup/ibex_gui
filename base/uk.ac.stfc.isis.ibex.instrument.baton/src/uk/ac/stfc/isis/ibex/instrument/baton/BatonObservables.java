
/**
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

package uk.ac.stfc.isis.ibex.instrument.baton;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.instrument.internal.MachineName;
import uk.ac.stfc.isis.ibex.instrument.internal.UserName;

/**
 * Holds the Observables and Writables relating to the baton passing mechanism.
 */
public class BatonObservables {
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);

    public final Writable<String> requestPV;
    public final InitialiseOnSubscribeObservable<String> controlPV;
    public final String self = UserName.get() + "@" + MachineName.get();

    public BatonObservables() {
        requestPV = writeFactory.getSwitchableWritable(new StringChannel(),
                Instrument.getInstance().getPvPrefix() + "CS:CONTROL:REQUEST");
        controlPV = obsFactory.getSwitchableObservable(new StringChannel(),
                Instrument.getInstance().getPvPrefix() + "CS:CONTROL");
    }

    public void sendRequest() {
        requestPV.write(self);
    }

}

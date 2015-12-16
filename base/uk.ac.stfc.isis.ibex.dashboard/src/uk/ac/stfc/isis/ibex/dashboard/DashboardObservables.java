
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

package uk.ac.stfc.isis.ibex.dashboard;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DateTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;

/**
 * Holds the Observables for the non-DAE part of the dashboard and holds a
 * reference to the class containing the DAE Observables.
 */
public class DashboardObservables extends InstrumentVariables {

    private static final String SHUTTER_STATUS = "SHTR:STAT";    
    private static final String TIME = "CS:IOC:INSTETC_01:DEVIOS:TOD";
    private static final String USERS = "ED:SURNAME";
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    
    public final InitialiseOnSubscribeObservable<ShutterStatus> shutter;
    public final InitialiseOnSubscribeObservable<String> instrumentTime;
    public final InitialiseOnSubscribeObservable<String> users;
		
	public final DaeObservables dae;

	public DashboardObservables(Channels channels) {
		super(channels);

        shutter = obsFactory.getSwitchableObservable(new EnumChannel<>(ShutterStatus.class),
                Instrument.getInstance().getPvPrefix() + SHUTTER_STATUS);
        instrumentTime = obsFactory.getSwitchableObservable(new DateTimeChannel(),
                Instrument.getInstance().getPvPrefix() + TIME);
        users = obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                Instrument.getInstance().getPvPrefix() + USERS);

        dae = new DaeObservables();
	}
}

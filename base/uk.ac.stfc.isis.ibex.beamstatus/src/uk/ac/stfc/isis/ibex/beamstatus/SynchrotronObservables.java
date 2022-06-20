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
package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.beamstatus.internal.Observables;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;

/**
 * Class that contains all the observables relating to the synchrotron.
 */
public class SynchrotronObservables extends Observables {
	/**
	 * An updating value giving the beam current of the synchrotron.
	 */
	public final FacilityPV beamCurrent;

	/**
	 * An updating value giving the beam frequency of the synchrotron.
	 */
	public final FacilityPV beamFrequency;

	/**
	 * An updating value giving the beam energy of the synchrotron.
	 */
	public final FacilityPV beamEnergy;

	/**
	 * An updating value giving the bunch length of the synchrotron.
	 */
	public final FacilityPV bunchLength;

	/**
	 * An updating value giving the extract delay of the synchrotron.
	 */
	public final FacilityPV extractDelay;

	private static final PVAddress AC_PV = PVAddress.startWith("AC");
	private static final PVAddress SYNC_PV = AC_PV.append("SYNCH");

	/**
	 * Constructor to register the synchrotron observables.
	 */
	public SynchrotronObservables() {
		beamCurrent = new FacilityPV(SYNC_PV.endWith(BEAM_CURRENT), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), SYNC_PV.endWith(BEAM_CURRENT))));

		beamFrequency = new FacilityPV(SYNC_PV.endWith(FREQ), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), SYNC_PV.endWith(FREQ))));

		beamEnergy = new FacilityPV(AC_PV.endWith(BEAM_ENERGY), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), AC_PV.endWith(BEAM_ENERGY))));

		bunchLength = new FacilityPV(AC_PV.endWith(BUNCH_LENGTH), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), AC_PV.endWith(BUNCH_LENGTH))));

		extractDelay = new FacilityPV(AC_PV.endWith(EXTRACT_DELAY), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), AC_PV.endWith(EXTRACT_DELAY))));
	}
}

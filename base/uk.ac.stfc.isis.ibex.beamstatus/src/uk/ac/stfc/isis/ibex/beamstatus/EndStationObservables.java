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
import uk.ac.stfc.isis.ibex.beamstatus.internal.OnOff;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.DateTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;

/**
 * The observables for a generic ISIS end station.
 */
public abstract class EndStationObservables extends Observables {
	private final FacilityPV beam;
	private final FacilityPV pps;
	private final FacilityPV beamCurrent;
	private final FacilityPV uAHToday;
	private final FacilityPV lastBeamOff;
	private final FacilityPV lastBeamOn;

	/**
	 * Constructor to register the observables for an ISIS end station.
	 * 
	 * @param prefix The prefix for that specific station.
	 */
	protected EndStationObservables(PVAddress prefix) {

		beam = new FacilityPV(prefix.endWith("BEAM:STAT"), adaptEnum(
				obsFactory.getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), prefix.endWith("BEAM:STAT"))));

		pps = new FacilityPV(prefix.endWith(FREQ),
				adaptNumber(obsFactory.getSwitchableObservable(new NumberChannel(), prefix.endWith(FREQ))));

		beamCurrent = new FacilityPV(prefix.endWith(BEAM_CURRENT), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), prefix.endWith(BEAM_CURRENT))));

		uAHToday = new FacilityPV(prefix.endWith("BEAM:TOTAL"), adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), prefix.endWith("BEAM:TOTAL"))));

		lastBeamOff = new FacilityPV(prefix.endWith("BEAMOFF:TIME"),
				adapt(obsFactory.getSwitchableObservable(new DateTimeChannel(), prefix.endWith("BEAMOFF:TIME"))));

		lastBeamOn = new FacilityPV(prefix.endWith("BEAMON:TIME"),
				adapt(obsFactory.getSwitchableObservable(new DateTimeChannel(), prefix.endWith("BEAMON:TIME"))));
	}

	/**
	 * @return The updated value for the end station beam status.
	 */
	public FacilityPV beam() {
		return beam;
	}

	/**
	 * @return The updated value for the end station beam frequency.
	 */
	public FacilityPV pps() {
		return pps;
	}

	/**
	 * @return The updated value for the end station beam current.
	 */
	public FacilityPV beamCurrent() {
		return beamCurrent;
	}

	/**
	 * @return The updated value for the total current today at the end station.
	 */
	public FacilityPV uAHToday() {
		return uAHToday;
	}

	/**
	 * @return The updated value for the last time the beam was off for the end
	 *         station.
	 */
	public FacilityPV lastBeamOff() {
		return lastBeamOff;
	}

	/**
	 * @return The updated value for the last time the beam was on for the end
	 *         station.
	 */
	public FacilityPV lastBeamOn() {
		return lastBeamOn;
	}
}

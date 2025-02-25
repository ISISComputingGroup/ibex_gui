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

import uk.ac.stfc.isis.ibex.beamstatus.internal.OnOff;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;

/**
 * Class that contains all the observables relating to TS1.
 */
public class TS1Observables extends EndStationObservables {
	/**
	 * An updating value giving the temperature of the TS1 methane moderator.
	 */
	public final FacilityPV methaneTemperature;

	/**
	 * An updating value giving the temperature of the TS1 hydrogen moderator.
	 */
	public final FacilityPV hydrogenTemperature;

	/**
	 * An updating value giving the status of the muon kicker in TS1.
	 */
	public final FacilityPV muonKicker;
	
	/**
	 * An updating value giving the muon beam current.
	 */
	public final FacilityPV muonBeamCurr;
	
	/**
	 * An updating value giving the EPB1 beam current.
	 */
	public final FacilityPV epb1BeamCurr;

	private static final String MOD_PREFIX = "TG:TS1:MOD:";

	/**
	 * Constructor for the TS1 observables, registers values to PVs.
	 */
	public TS1Observables() {
		super(PVAddress.startWith("AC").append("TS1"));

		ForwardingObservable<Number> methaneTempObs = obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "METH:TEMP");
		methaneTemperature = new FacilityPV(MOD_PREFIX + "METH:TEMP", adaptNumber(methaneTempObs));

		ForwardingObservable<Number> hydrogenTempObs = obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "HDGN:TEMP");
		hydrogenTemperature = new FacilityPV(MOD_PREFIX + "HDGN:TEMP", adaptNumber(hydrogenTempObs));

		ForwardingObservable<OnOff> muonKickerObs = obsFactory
				.getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), "AC:MUON:KICKR:STAT");
		muonKicker = new FacilityPV("AC:MUON:KICKR:STAT", adaptEnum(muonKickerObs));
		
		
		ForwardingObservable<Number> muonBeamCurrObs = obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), "AC:MUON:BEAM:CURR");
		muonBeamCurr = new FacilityPV("AC:MUON:BEAM:CURR", adaptNumber(muonBeamCurrObs));
		
		
		ForwardingObservable<Number> epb1BeamCurrObs = obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), "AC:EPB1:BEAM:CURR");
		epb1BeamCurr = new FacilityPV("AC:EPB1:BEAM:CURR", adaptNumber(epb1BeamCurrObs));
		
	}

}

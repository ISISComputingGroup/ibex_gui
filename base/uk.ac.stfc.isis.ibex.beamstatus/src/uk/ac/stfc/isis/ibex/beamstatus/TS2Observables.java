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

package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.beamstatus.internal.YesNo;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;

/**
 * Class that contains all the observables relating to TS2.
 */
public class TS2Observables extends EndStationObservables {

	/**
	 * An updating value giving the temperature of the TS2 coupled methane
	 * moderator.
	 */
	public final FacilityPV coupledMethaneTemperature;

	/**
	 * An updating value giving the temperature of the TS2 coupled hydrogen
	 * moderator.
	 */
	public final FacilityPV coupledHydrogenTemperature;

	/**
	 * An updating value giving the temperature of the TS2 decoupled methane
	 * moderator.
	 */
	public final FacilityPV decoupledMethaneTemperature;

	/**
	 * An updating value giving the runtime duration of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorRuntime;

	/**
	 * An updating value giving the runtime limit of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorRuntimeLimit;

	/**
	 * An updating value giving the anneal pressure of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorAnnealPressure;

	/**
	 * An updating value giving the beam of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorUAHBeam;

	/**
	 * An updating value giving the beam limit of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorBeamLimit;
	
	/**
	 * An updating value giving the charge change time of the TS2 decoupled moderator.
	 */
	public final FacilityPV decoupledModeratorChargeChangeTime;
	
	private static final PVAddress TS2_PV = PVAddress.startWith("AC").append("TS2");

	private static final String MOD_PREFIX = "TG:TS2:DMOD:";

	/**
	 * Constructor for the TS2 observables, registers values to PVs.
	 */
	protected TS2Observables() {
		super(TS2_PV);

		coupledMethaneTemperature = new FacilityPV(TS2_PV.endWith("CMOD:METH:TEMP"), adaptNumber(obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), TS2_PV.endWith("CMOD:METH:TEMP"))));

		coupledHydrogenTemperature = new FacilityPV(TS2_PV.endWith("CMOD:HDGN:TEMP"), adaptNumber(obsFactory
				.getSwitchableObservable(new NumberWithPrecisionChannel(), TS2_PV.endWith("CMOD:HDGN:TEMP"))));

		decoupledMethaneTemperature = new FacilityPV(MOD_PREFIX + "METH:TEMP", adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "METH:TEMP")));

		decoupledModeratorRuntime = new FacilityPV(MOD_PREFIX + "RTIME:DUR", adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "RTIME:DUR")));

		decoupledModeratorRuntimeLimit = new FacilityPV(MOD_PREFIX + "RTLIM", adaptNumber(
				obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "RTLIM")));

		decoupledModeratorAnnealPressure = new FacilityPV(MOD_PREFIX + "RTLIM", adaptEnum(
				obsFactory.getSwitchableObservable(new EnumChannel<YesNo>(YesNo.class), MOD_PREFIX + "ANNPLOW:STAT")));

		decoupledModeratorUAHBeam = new FacilityPV(MOD_PREFIX + "BEAM",
				adaptNumber(obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "BEAM")));
		
		decoupledModeratorBeamLimit = new FacilityPV(MOD_PREFIX + "BEAM:LIM",
				adaptNumber(obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "BEAM:LIM")));
		
		decoupledModeratorChargeChangeTime = new FacilityPV(MOD_PREFIX + "CHRGCHNG:TIME",
				adaptNumber(obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "CHRGCHNG:TIME")));

	}
}

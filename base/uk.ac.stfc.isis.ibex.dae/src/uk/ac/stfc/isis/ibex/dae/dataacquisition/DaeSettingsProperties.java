/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae.dataacquisition;

/**
 * !!! Important !!!
 * 
 * These values need to correspond to the fields of the DaeSettings class.
 *
 */
@SuppressWarnings("checkstyle:JavadocVariableCheck")
public final class DaeSettingsProperties {

	public static final String MONITOR_SPECTRUM = "monitorSpectrum";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String NEW_WIRING_TABLE = "newWiringTable";
	public static final String NEW_DETECTOR_TABLE = "newDetectorTable";
	public static final String NEW_SPECTRA_TABLE = "newSpectraTable";
	public static final String WIRING_TABLE = "wiringTable";
	public static final String DETECTOR_TABLE = "detectorTable";
	public static final String SPECTRA_TABLE = "spectraTable";
	public static final String TIMING_SOURCE = "timingSource";
	public static final String SMP_VETO = "smpVeto";
	public static final String VETO_0 = "veto0";
	public static final String VETO_1 = "veto1";
	public static final String VETO_2 = "veto2";
	public static final String VETO_3 = "veto3";
	public static final String FERMI_CHOPPER_VETO = "fermiChopperVeto";
	public static final String FC_DELAY = "fcDelay";
	public static final String FC_WIDTH = "fcWidth";
	public static final String MUON_MS_MODE = "muonMsMode";
	public static final String MUON_CERENKOV_PULSE = "muonCerenkovPulse";
	public static final String TS2_PULSE_VETO = "ts2PulseVeto";
	public static final String ISIS_50HZ_VETO = "isis50HzVeto";

	private DaeSettingsProperties() {
		throw new UnsupportedOperationException();
	}
}

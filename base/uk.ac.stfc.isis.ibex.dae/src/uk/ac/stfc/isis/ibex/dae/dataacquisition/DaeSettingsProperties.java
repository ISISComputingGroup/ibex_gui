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
public final class DaeSettingsProperties {

	/**
	 * The current monitor spectrum number.
	 */
	public static final String MONITOR_SPECTRUM = "monitorSpectrum";
	/**
	 * The current lower limit of the monitor range.
	 */
	public static final String FROM = "from";
	/**
	 * The current upper limit of the monitor range.
	 */
	public static final String TO = "to";
	/**
	 * The path for the new wiring table.
	 */
	public static final String NEW_WIRING_TABLE = "newWiringTable";
	/**
	 * The path for the new detector table.
	 */
	public static final String NEW_DETECTOR_TABLE = "newDetectorTable";
	/**
	 * The path for the new spectra table.
	 */
	public static final String NEW_SPECTRA_TABLE = "newSpectraTable";
	/**
	 * The path for the current wiring table.
	 */
	public static final String WIRING_TABLE = "wiringTable";
	/**
	 * The path for the current detector table.
	 */
	public static final String DETECTOR_TABLE = "detectorTable";
	/**
	 * The path for the current spectra table.
	 */
	public static final String SPECTRA_TABLE = "spectraTable";
	/**
	 * The source to use for DAE timing.
	 */
	public static final String TIMING_SOURCE = "timingSource";
	/**
	 * The Yes/No value of the SMP Veto.
	 */
	public static final String SMP_VETO = "smpVeto";
	/**
	 * The Yes/No value of veto 0.
	 */
	public static final String VETO_0 = "veto0";
	/**
	 * The Yes/No value of veto 1.
	 */
	public static final String VETO_1 = "veto1";
	/**
	 * The Yes/No value of veto 2.
	 */
	public static final String VETO_2 = "veto2";
	/**
	 * The Yes/No value of veto 3.
	 */
	public static final String VETO_3 = "veto3";
	/**
	 * The Yes/No value of the Fermi chopper veto.
	 */
	public static final String FERMI_CHOPPER_VETO = "fermiChopperVeto";
	/**
	 * The delay on the Fermi chopper.
	 */
	public static final String FC_DELAY = "fcDelay";
	/**
	 * The width of the Fermi chopper.
	 */
	public static final String FC_WIDTH = "fcWidth";
	/**
	 * Whether the muon millisecond mode is enabled.
	 */
	public static final String MUON_MS_MODE = "muonMsMode";
	/**
	 * Whether the muon Cerenkov Pulse is first or second.
	 */
	public static final String MUON_CERENKOV_PULSE = "muonCerenkovPulse";
	/**
	 * The Yes/No value of the target station 2 pulse veto.
	 */
	public static final String TS2_PULSE_VETO = "ts2PulseVeto";
	/**
	 * The Yes/No value of the ISIS 50Hz veto.
	 */
	public static final String ISIS_50HZ_VETO = "isis50HzVeto";

	private DaeSettingsProperties() {
		throw new UnsupportedOperationException();
	}
}

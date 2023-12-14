
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

package uk.ac.stfc.isis.ibex.dae.dataacquisition;

import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.DETECTOR_TABLE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.FC_DELAY;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.FC_WIDTH;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.FERMI_CHOPPER_VETO;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.FROM;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.ISIS_50HZ_VETO;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.MONITOR_SPECTRUM;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.MUON_CERENKOV_PULSE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.MUON_MS_MODE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.NEW_DETECTOR_TABLE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.NEW_SPECTRA_TABLE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.NEW_WIRING_TABLE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.SMP_VETO;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.SPECTRA_TABLE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.TIMING_SOURCE;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.TO;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.TS2_PULSE_VETO;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.VETO_0;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.VETO_1;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.VETO_2;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.VETO_3;
import static uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties.WIRING_TABLE;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The settings for connecting to the DAE.
 */
public class DaeSettings extends ModelObject {

	/**
	 * The current monitor spectrum number.
	 */
	private int monitorSpectrum;

	/**
	 * The lower interval value for the DAE range.
	 */
	private double from;

	/**
	 * The upper interval value for the DAE range.
	 */
	private double to;

	/**
	 * The new wiring table applied with these settings.
	 */
	private String newWiringTable = "";

	/**
	 * The new detector table applied with these settings.
	 */
	private String newDetectorTable = "";

	/**
	 * The new spectra table applied with these settings.
	 */
	private String newSpectraTable = "";

	/**
	 * The current wiring table.
	 */
	private String wiringTable = "";

	/**
	 * The current detector table.
	 */
	private String detectorTable = "";

	/**
	 * The current spectra table.
	 */
	private String spectraTable = "";

	/**
	 * The source to use for DAE timing.
	 */
	private DaeTimingSource timingSource = DaeTimingSource.ISIS;

	/**
	 * The Yes/No value of the smpVeto.
	 */
	private BinaryChoice smpVeto = BinaryChoice.NO;

	/**
	 * The Yes/No value of veto0.
	 */
	private BinaryChoice veto0 = BinaryChoice.NO;

	/**
	 * The Yes/No value of veto1.
	 */
	private BinaryChoice veto1 = BinaryChoice.NO;

	/**
	 * The Yes/No value of veto2.
	 */
	private BinaryChoice veto2 = BinaryChoice.NO;

	/**
	 * The Yes/No value of veto3.
	 */
	private BinaryChoice veto3 = BinaryChoice.NO;

	/**
	 * The Yes/No value of the Fermi chopper veto.
	 */
	private BinaryChoice fermiChopperVeto = BinaryChoice.NO;

	/**
	 * The delay on the Fermi chopper.
	 */
	private double fcDelay;

	/**
	 * The width of he Fermi chopper.
	 */
	private double fcWidth;

	/**
	 * Whether the muon millisecond mode is enabled.
	 */
	private BinaryState muonMsMode = BinaryState.DISABLED;

	/**
	 * Whether the Muon Cerenkov Pulse is first or second.
	 */
	private MuonCerenkovPulse muonCerenkovPulse = MuonCerenkovPulse.FIRST;

	/**
	 * The Yes/No value of the target station 2 pulse veto.
	 */
	private BinaryChoice ts2PulseVeto = BinaryChoice.NO;

	/**
	 * The Yes/No value of the ISIS 50Hz veto.
	 */
	private BinaryChoice isis50HzVeto = BinaryChoice.NO;

	/**
	 * @return The current monitor spectrum number
	 */
	public int monitorSpectrum() {
		return monitorSpectrum;
	}

	/**
	 * @param value The new monitor spectrum number
	 */
	public void setMonitorSpectrum(int value) {
		firePropertyChange(MONITOR_SPECTRUM, monitorSpectrum, monitorSpectrum = value);
	}

	/**
	 * @return The current lower limit of the monitor range.
	 */
	public double from() {
		return from;
	}

	/**
	 * @param value The new lower limit of the monitor range.
	 */
	public void setFrom(double value) {
		firePropertyChange(FROM, from, from = value);
	}

	/**
	 * @return The current upper limit of the monitor range.
	 */
	public double to() {
		return to;
	}

	/**
	 * @param value The new upper limit of the monitor range.
	 */
	public void setTo(double value) {
		firePropertyChange(TO, to, to = value);
	}

	/**
	 * Get the path for the new wiring table (to be set as current table once
	 * changes are applied).
	 * 
	 * @return the file path.
	 */
	public String getNewWiringTable() {
		return newWiringTable;
	}

	/**
	 * Set the path for the new wiring table (to be set as current table once
	 * changes are applied).
	 * 
	 * @param value the file path.
	 */
	public void setNewWiringTable(String value) {
		firePropertyChange(NEW_WIRING_TABLE, newWiringTable, newWiringTable = value);
	}

	/**
	 * Get the path for the new detector table (to be set as current table once
	 * changes are applied).
	 * 
	 * @return the file path.
	 */
	public String getNewDetectorTable() {
		return newDetectorTable;
	}

	/**
	 * Set the path for the new detector table (to be set as current table once
	 * changes are applied).
	 * 
	 * @param value the file path.
	 */
	public void setNewDetectorTable(String value) {
		firePropertyChange(NEW_DETECTOR_TABLE, newDetectorTable, newDetectorTable = value);
	}

	/**
	 * Get the path for the new spectra table (to be set as current table once
	 * changes are applied).
	 * 
	 * @return the file path.
	 */
	public String getNewSpectraTable() {
		return newSpectraTable;
	}

	/**
	 * Set the path for the new spectra table (to be set as current table once
	 * changes are applied).
	 * 
	 * @param value the file path.
	 */
	public void setNewSpectraTable(String value) {
		firePropertyChange(NEW_SPECTRA_TABLE, newSpectraTable, newSpectraTable = value);
	}

	/**
	 * @return The current DAE timing source
	 */
	public DaeTimingSource timingSource() {
		return timingSource;
	}

	/**
	 * @param value The new DAE timing source.
	 */
	public void setTimingSource(DaeTimingSource value) {
		firePropertyChange(TIMING_SOURCE, timingSource, timingSource = value);
	}

	/**
	 * @return The current state of the smp veto
	 */
	public BinaryChoice smpVeto() {
		return smpVeto;
	}

	/**
	 * @param value The new state of the smp veto
	 */
	public void setSmpVeto(BinaryChoice value) {
		firePropertyChange(SMP_VETO, smpVeto, smpVeto = value);
	}

	/**
	 * @return The current state of veto 0
	 */
	public BinaryChoice veto0() {
		return veto0;
	}

	/**
	 * @param value The new state of veto 0
	 */
	public void setVeto0(BinaryChoice value) {
		firePropertyChange(VETO_0, veto0, veto0 = value);
	}

	/**
	 * @return The current state of veto 1
	 */
	public BinaryChoice veto1() {
		return veto1;
	}

	/**
	 * @param value The new state of veto 1
	 */
	public void setVeto1(BinaryChoice value) {
		firePropertyChange(VETO_1, veto1, veto1 = value);
	}

	/**
	 * @return The current state of veto 2
	 */
	public BinaryChoice veto2() {
		return veto2;
	}

	/**
	 * @param value The new state of veto 2
	 */
	public void setVeto2(BinaryChoice value) {
		firePropertyChange(VETO_2, veto2, veto2 = value);
	}

	/**
	 * @return The current state of veto 3
	 */
	public BinaryChoice veto3() {
		return veto3;
	}

	/**
	 * @param value The new state of veto 3
	 */
	public void setVeto3(BinaryChoice value) {
		firePropertyChange(VETO_3, veto3, veto3 = value);
	}

	/**
	 * @return The current state of the Fermi chopper veto
	 */
	public BinaryChoice fermiChopperVeto() {
		return fermiChopperVeto;
	}

	/**
	 * @param value The new state of the Fermi chopper veto
	 */
	public void setFermiChopperVeto(BinaryChoice value) {
		firePropertyChange(FERMI_CHOPPER_VETO, fermiChopperVeto, fermiChopperVeto = value);
	}

	/**
	 * @return The current Fermi chopper delay
	 */
	public double fcDelay() {
		return fcDelay;
	}

	/**
	 * @param value The new Fermi chopper delay
	 */
	public void setFcDelay(double value) {
		firePropertyChange(FC_DELAY, fcDelay, fcDelay = value);
	}

	/**
	 * @return The current Fermi chopper width
	 */
	public double fcWidth() {
		return fcWidth;
	}

	/**
	 * @param value The new fermi chopper width
	 */
	public void setFcWidth(double value) {
		firePropertyChange(FC_WIDTH, fcWidth, fcWidth = value);
	}

	/**
	 * @return The current state of the muon millisecond mode
	 */
	public BinaryState muonMsMode() {
		return muonMsMode;
	}

	/**
	 * @param value The new state of the muon millisecond mode
	 */
	public void setMuonMsMode(BinaryState value) {
		firePropertyChange(MUON_MS_MODE, muonMsMode, muonMsMode = value);
	}

	/**
	 * @return The current state of the muon Cerenkov pulse (first/second)
	 */
	public MuonCerenkovPulse muonCerenkovPulse() {
		return muonCerenkovPulse;
	}

	/**
	 * @param value The new state of the muon Cerenkov pulse (first/second)
	 */
	public void setMuonCerenkovPulse(MuonCerenkovPulse value) {
		firePropertyChange(MUON_CERENKOV_PULSE, muonCerenkovPulse, muonCerenkovPulse = value);
	}

	/**
	 * @return The current state of the target station 2 pulse veto
	 */
	public BinaryChoice ts2PulseVeto() {
		return ts2PulseVeto;
	}

	/**
	 * @param value The new state of the target station 2 pulse veto
	 */
	public void setTs2PulseVeto(BinaryChoice value) {
		firePropertyChange(TS2_PULSE_VETO, ts2PulseVeto, ts2PulseVeto = value);
	}

	/**
	 * @return The current state of the ISIS 50Hz veto
	 */
	public BinaryChoice isis50HzVeto() {
		return isis50HzVeto;
	}

	/**
	 * @param value The new state of the ISIS 50Hz veto
	 */
	public void setIsis50HzVeto(BinaryChoice value) {
		firePropertyChange(ISIS_50HZ_VETO, isis50HzVeto, isis50HzVeto = value);
	}

	/**
	 * Get the path for the currently set wiring table.
	 * 
	 * @return the file path.
	 */
	public String getWiringTable() {
		return wiringTable;
	}

	/**
	 * Set the path for the current wiring table.
	 * 
	 * @param value the file path.
	 */
	public void setWiringTable(String value) {
		firePropertyChange(WIRING_TABLE, this.wiringTable, this.wiringTable = value);
	}

	/**
	 * Get the path for the currently set detector table.
	 * 
	 * @return the file path.
	 */
	public String getDetectorTable() {
		return detectorTable;
	}

	/**
	 * Set the path for the current detector table.
	 * 
	 * @param value the file path.
	 */
	public void setDetectorTable(String value) {
		firePropertyChange(DETECTOR_TABLE, this.detectorTable, this.detectorTable = value);
	}

	/**
	 * Get the path for the currently set spectra table.
	 * 
	 * @return the file path.
	 */
	public String getSpectraTable() {
		return spectraTable;
	}

	/**
	 * Set the path for the current spectra table.
	 * 
	 * @param value the file path.
	 */
	public void setSpectraTable(String value) {
		firePropertyChange(SPECTRA_TABLE, this.spectraTable, this.spectraTable = value);
	}
}

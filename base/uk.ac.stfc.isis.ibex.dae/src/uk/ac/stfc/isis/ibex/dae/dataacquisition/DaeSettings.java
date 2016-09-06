
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

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class DaeSettings extends ModelObject {

	private int monitorSpectrum;
	private double from;
	private double to;
	private String newWiringTable = "";
	private String newDetectorTable = "";
	private String newSpectraTable = "";
    private String wiringTable = "";
    private String detectorTable = "";
    private String spectraTable = "";
	private DaeTimingSource timingSource = DaeTimingSource.ISIS;
	private BinaryChoice smpVeto = BinaryChoice.NO;
	private BinaryChoice veto0 = BinaryChoice.NO;
	private BinaryChoice veto1 = BinaryChoice.NO;
	private BinaryChoice veto2 = BinaryChoice.NO;
	private BinaryChoice veto3 = BinaryChoice.NO;
	private BinaryChoice fermiChopperVeto = BinaryChoice.NO;	
	private double fcDelay;
	private double fcWidth;
	private BinaryState muonMsMode = BinaryState.DISABLED;
	private MuonCerenkovPulse muonCerenkovPulse = MuonCerenkovPulse.FIRST;
	private BinaryChoice ts2PulseVeto = BinaryChoice.NO;;
	private BinaryChoice isis50HzVeto = BinaryChoice.NO;;
	
	public int monitorSpectrum() {
		return monitorSpectrum;
	}
	
	public void setMonitorSpectrum(int value) {
		firePropertyChange("monitorSpectrum", monitorSpectrum, monitorSpectrum = value);
	}

	public double from() {
		return from;
	}
	
	public void setFrom(double value) {
		firePropertyChange("from", from, from = value);
	}

	public double to() {
		return to;
	}
	
	public void setTo(double value) {
		firePropertyChange("to", to, to = value);
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
        firePropertyChange("newWiringTable", newWiringTable, newWiringTable = value);
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
        firePropertyChange("newDetectorTable", newDetectorTable, newDetectorTable = value);
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
        firePropertyChange("newSpectraTable", newSpectraTable, newSpectraTable = value);
	}
	
	public DaeTimingSource timingSource() {
		return timingSource;
	}

	public void setTimingSource(DaeTimingSource value) {
		firePropertyChange("timingSource", timingSource, timingSource = value);
	}

	public BinaryChoice smpVeto() {
		return smpVeto;
	}
	
	public void setSmpVeto(BinaryChoice value) {
		firePropertyChange("smpVeto", smpVeto, smpVeto = value);
	}

	public BinaryChoice veto0() {
		return veto0;
	}
	
	public void setVeto0(BinaryChoice value) {
		firePropertyChange("veto0", veto0, veto0 = value);
	}
	
	public BinaryChoice veto1() {
		return veto1;
	}
	
	public void setVeto1(BinaryChoice value) {
		firePropertyChange("veto1", veto1, veto1 = value);
	}
	
	public BinaryChoice veto2() {
		return veto2;
	}
	
	public void setVeto2(BinaryChoice value) {
		firePropertyChange("veto2", veto2, veto2 = value);
	}
	
	public BinaryChoice veto3() {
		return veto3;
	}
	
	public void setVeto3(BinaryChoice value) {
		firePropertyChange("veto3", veto3, veto3 = value);
	}
	
	public BinaryChoice fermiChopperVeto() {
		return fermiChopperVeto;
	}
	
	public void setFermiChopperVeto(BinaryChoice value) {
		firePropertyChange("fermiChopperVeto", fermiChopperVeto, fermiChopperVeto = value);
	}
		
	public double fcDelay() {
		return fcDelay;
	}
	
	public void setFcDelay(double value) {
		firePropertyChange("fcDelay", fcDelay, fcDelay = value);
	}

	public double fcWidth() {
		return fcWidth;
	}
	
	public void setFcWidth(double value) {
		firePropertyChange("fcWidth", fcWidth, fcWidth = value);
	}
	
	public BinaryState muonMsMode() {
		return muonMsMode;
	}
	
	public void setMuonMsMode(BinaryState value) {
		firePropertyChange("muonMsMode", muonMsMode, muonMsMode = value);
	}
	
	public MuonCerenkovPulse muonCerenkovPulse() {
		return muonCerenkovPulse;
	}
	
	public void setMuonCerenkovPulse(MuonCerenkovPulse value) {
		firePropertyChange("muonCerenkovPulse", muonCerenkovPulse, muonCerenkovPulse = value);
	}
	
	public BinaryChoice ts2PulseVeto() {
		return ts2PulseVeto;
	}
	
	public void setTs2PulseVeto(BinaryChoice value) {
		firePropertyChange("ts2PulseVeto", ts2PulseVeto, ts2PulseVeto = value);
	}
	
	public BinaryChoice isis50HzVeto() {
		return isis50HzVeto;
	}
	
	public void setIsis50HzVeto(BinaryChoice value) {
		firePropertyChange("isis50HzVeto", isis50HzVeto, isis50HzVeto = value);
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
        firePropertyChange("wiringTable", this.wiringTable, this.wiringTable = value);
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
        firePropertyChange("detectorTable", this.detectorTable, this.detectorTable = value);
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
        firePropertyChange("spectraTable", this.spectraTable, this.spectraTable = value);
    }
}


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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.dae.dataacquisition.BinaryChoice;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.BinaryState;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettings;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeTimingSource;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.MuonCerenkovPulse;
import uk.ac.stfc.isis.ibex.dae.updatesettings.AutosaveUnit;
import uk.ac.stfc.isis.ibex.dae.updatesettings.UpdateSettings;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class DataAcquisitionViewModel extends ModelObject {
	
	private DaeSettings settings;
	private UpdateSettings updateSettings;
	private UpdatedValue<Collection<String>> wiringTables;
	private UpdatedValue<Collection<String>> detectorTables;
	private UpdatedValue<Collection<String>> spectraTables;
	
	public void setModel(DaeSettings settings) {
		this.settings = settings;
		
		settings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), null, null);
			}
		});	
	}
	
	public void setUpdateSettings(UpdateSettings settings) {
		updateSettings = settings;
		
		updateSettings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), null, null);
			}
		});	
	}
	
	public void setWiringTableList(UpdatedValue<Collection<String>> tables) {
		wiringTables = tables;
		
		wiringTables.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				// Fire a property change event on wiringTableList not wiringTables
				firePropertyChange("wiringTableList", null, null);
			}
		});	
	}
	
	public void setDetectorTableList(UpdatedValue<Collection<String>> tables) {
		detectorTables = tables;
		
		detectorTables.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				// Fire a property change event on detectorTableList not detectorTables
				firePropertyChange("detectorTableList", null, null);
			}
		});	
	}
	
	public void setSpectraTableList(UpdatedValue<Collection<String>> tables) {
		spectraTables = tables;
		
		spectraTables.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				// Fire a property change event on spectraTableList not spectraTables
				firePropertyChange("spectraTableList", null, null);
			}
		});	
	}
	
	public int getMonitorSpectrum() {
		return settings.monitorSpectrum();
	}
	
	public void setMonitorSpectrum(int value) {
		settings.setMonitorSpectrum(value);
	}

	public double getFrom() {
		return settings.from();
	}
	
	public void setFrom(double value) {
		settings.setFrom(value);
	}

	public double getTo() {
		return settings.to();
	}
	
	public void setTo(double value) {
		settings.setTo(value);
	}
	
	public String[] getWiringTableList() {
        String[] tables = valueOrEmpty(wiringTables);
        tables = tables.length != 0 ? tables : new String[] {
                "No wiring tables found on instrument. Files must be located in C:\\Instrument\\Settings\\config\\[Instrument Name]\\configuration\\tables\\ and filename must contain string \"wiring\"." };
        return tables;
	}
	
	private String[] valueOrEmpty(UpdatedValue<Collection<String>> updated) {
		Collection<String> value = updated.getValue();
		return value != null ? value.toArray(new String[0]) : new String[0];
	}

	public String getWiringTable() {
		return settings.wiringTable();
	}
	
	public void setWiringTable(String value) {
		settings.setWiringTable(value);
	}
	
	public String[] getDetectorTableList() {
        String[] tables = valueOrEmpty(detectorTables);
        tables = tables.length != 0 ? tables : new String[] {
                "No detector tables found on instrument. Files must be located in C:\\Instrument\\Settings\\config\\[Instrument Name]\\configuration\\tables\\ and filename must contain string \"det\"." };
        return tables;
	}
	
	public String getDetectorTable() {
		return settings.detectorTable();
	}
	
	public void setDetectorTable(String value) {
		settings.setDetectorTable(value);
	}
	
	public String[] getSpectraTableList() {
        String[] tables = valueOrEmpty(spectraTables);
        tables = tables.length != 0 ? tables : new String[] {
                "No spectra tables found on instrument. Files must be located in C:\\Instrument\\Settings\\config\\[Instrument Name]\\configuration\\tables\\ and filename must contain string \"spec\"." };
        return tables;
	}
	
	public String getSpectraTable() {
		return settings.spectraTable();
	}
	
	public void setSpectraTable(String value) {
		settings.setSpectraTable(value);
	}
	
	public int getTimingSource() {
		return settings.timingSource().ordinal();
	}

	public void setTimingSource(int index) {
		DaeTimingSource value = DaeTimingSource.values()[index];
		settings.setTimingSource(value);
	}

	public boolean getSmpVeto() {
		return settings.smpVeto() == BinaryChoice.YES;
	}
	
	public void setSmpVeto(boolean selected) {
		settings.setSmpVeto(vetoIsActive(selected));
	}

	public boolean getVeto0() {
		return vetoIsActive(settings.veto0());
	}
	
	public void setVeto0(boolean selected) {
		settings.setVeto0(vetoIsActive(selected));	
	}

	public boolean getVeto1() {
		return vetoIsActive(settings.veto1());
	}
	
	public void setVeto1(boolean selected) {
		settings.setVeto1(vetoIsActive(selected));	
	}
	
	public boolean getVeto2() {
		return vetoIsActive(settings.veto2());
	}
	
	public void setVeto2(boolean selected) {
		settings.setVeto2(vetoIsActive(selected));	
	}
	
	public boolean getVeto3() {
		return vetoIsActive(settings.veto3());
	}
	
	public void setVeto3(boolean selected) {
		settings.setVeto3(vetoIsActive(selected));	
	}
		
	public boolean getFermiChopperVeto() {
		return vetoIsActive(settings.fermiChopperVeto());
	}
	
	public void setFermiChopperVeto(boolean selected) {
		settings.setFermiChopperVeto(vetoIsActive(selected));	
	}
		
	public double getFcDelay() {
		return settings.fcDelay();
	}
	
	public void setFcDelay(double value) {
		settings.setFcDelay(value);
	}

	public double getFcWidth() {
		return settings.fcWidth();
	}
	
	public void setFcWidth(double value) {
		settings.setFcWidth(value);
	}
	
	public boolean getMuonMsMode() {
		return settings.muonMsMode() == BinaryState.ENABLED;
	}
	
	public void setMuonMsMode(boolean selected) {
		settings.setMuonMsMode(selected ? BinaryState.ENABLED : BinaryState.DISABLED);
	}
	
	public boolean getMuonCerenkovPulse() {
		return settings.muonCerenkovPulse() == MuonCerenkovPulse.FIRST;
	}
	
	public void setMuonCerenkovPulse(boolean firstEnabled) {
		settings.setMuonCerenkovPulse(firstEnabled ? MuonCerenkovPulse.FIRST : MuonCerenkovPulse.SECOND);
	}
	
	public boolean getTs2PulseVeto() {
		return vetoIsActive(settings.ts2PulseVeto());
	}
	
	public void setTs2PulseVeto(boolean selected) {
		settings.setTs2PulseVeto(vetoIsActive(selected));
	}
	
	public boolean getIsis50HzVeto() {
		return vetoIsActive(settings.isis50HzVeto());
	}
	
	public void setIsis50HzVeto(boolean selected) {
		settings.setIsis50HzVeto(vetoIsActive(selected));
	}
	
	public int getAutosaveFrequency() {
		return updateSettings.getAutosaveFrequency();
	}
	
	public void setAutosaveFrequency(int value) {
		updateSettings.setAutosaveFrequency(value);
	}
	
	public int getAutosaveUnits() {
		return updateSettings.getAutosaveUnits().ordinal();
	}
	
	public void setAutosaveUnits(int index) {
		updateSettings.setAutosaveUnits(AutosaveUnit.values()[index]);
	}
	
	private static boolean vetoIsActive(BinaryChoice value) {
		return value == BinaryChoice.YES;
	}
	
	private static BinaryChoice vetoIsActive(Boolean selected) {
		return BinaryChoice.values()[selected ? 1 : 0];
	}
}

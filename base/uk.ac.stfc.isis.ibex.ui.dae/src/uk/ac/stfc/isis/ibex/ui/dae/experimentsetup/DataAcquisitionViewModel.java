
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

/**
 * This class is the View Model for the DAE screen and contains all
 * the logic for how the screen should be displayed.
 */
public class DataAcquisitionViewModel extends ModelObject {

	private DaeSettings settings;
	private UpdateSettings updateSettings;
    private DAEComboContentProvider comboContentProvider;
	private UpdatedValue<Collection<String>> wiringTables;
	private UpdatedValue<Collection<String>> detectorTables;
	private UpdatedValue<Collection<String>> spectraTables;

    /**
     * Sets the DAE settings.
     * 
     * @param settings the settings.
     */
	public void setModel(DaeSettings settings) {
		this.settings = settings;
		
		settings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), null, null);
			}
		});	
	}
	
    /**
     * Sets the updateable DAE settings.
     * 
     * @param settings the settings.
     */
	public void setUpdateSettings(UpdateSettings settings) {
		updateSettings = settings;
		
		updateSettings.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), null, null);
			}
		});	
	}
	
    /**
     * Sets the list of wiring tables currently available on the instrument.
     * 
     * @param tables the list of tables.
     */
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

    /**
     * Sets the list of detector tables currently available on the instrument.
     * 
     * @param tables the list of tables.
     */
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

    /**
     * Sets the list of spectra tables currently available on the instrument.
     * 
     * @param tables the list of tables.
     */
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
	
    /**
     * Gets the monitor spectrum.
     * 
     * @return The monitor spectrum.
     */
	public int getMonitorSpectrum() {
		return settings.monitorSpectrum();
	}
	
    /**
     * Sets the monitor spectrum.
     * 
     * @param value The monitor spectrum.
     */
	public void setMonitorSpectrum(int value) {
		settings.setMonitorSpectrum(value);
	}

    /**
     * Sets the monitor spectrum lower limit.
     * 
     * @return The monitor spectrum lower limit.
     */
	public double getFrom() {
		return settings.from();
	}
	
    /**
     * Gets the monitor spectrum lower limit.
     * 
     * @param value The monitor spectrum lower limit.
     */
	public void setFrom(double value) {
		settings.setFrom(value);
	}

    /**
     * Sets the monitor spectrum upper limit.
     * 
     * @return The monitor spectrum upper limit.
     */
	public double getTo() {
		return settings.to();
	}

    /**
     * Gets the monitor spectrum upper limit.
     * 
     * @param value The monitor spectrum upper limit.
     */
	public void setTo(double value) {
		settings.setTo(value);
	}

    /**
     * Gets the list of wiring tables currently available to the instrument.
     * 
     * @return the list of wiring tables.
     */
    public String[] getWiringTableList() {
        return comboContentProvider.getContent(wiringTables, "wiring");
    }

    /**
     * Returns the path to the wiring table currently in use.
     * 
     * @return the file path.
     */
	public String getWiringTable() {
        return settings.getWiringTable();
	}
	
    /**
     * Sets a new wiring table in the settings (does not take effect until
     * changes are applied).
     * 
     * @param value the path to the new wiring table.
     */
	public void setNewWiringTable(String value) {
		settings.setNewWiringTable(value);
	}
	
    /**
     * Gets the list of detector tables currently available to the instrument.
     * 
     * @return the list of detector tables.
     */
	public String[] getDetectorTableList() {
        return comboContentProvider.getContent(detectorTables, "det");
    }

    /**
     * Returns the path to the detector table currently in use.
     * 
     * @return the file path.
     */
	public String getDetectorTable() {
        return settings.getDetectorTable();
	}
	
    /**
     * Sets a new detector table in the settings (does not take effect until
     * changes are applied).
     * 
     * @param value the path to the new detector table.
     */
	public void setNewDetectorTable(String value) {
		settings.setNewDetectorTable(value);
	}

    /**
     * Gets the list of spectra tables currently available to the instrument.
     * 
     * @return the list of detector tables.
     */
	public String[] getSpectraTableList() {
        return comboContentProvider.getContent(spectraTables, "spec");
	}

    /**
     * Returns the path to the spectra table currently in use.
     * 
     * @return the file path.
     */
	public String getSpectraTable() {
        return settings.getSpectraTable();
	}

    /**
     * Sets a new spectra table in the settings (does not take effect until
     * changes are applied).
     * 
     * @param value the path to the new spectra table.
     */
	public void setNewSpectraTable(String value) {
		settings.setNewSpectraTable(value);
	}
	
	/**
	 * Returns the ordinal within DaeTimingSource of the current timing source.
	 * 
	 * @return the ordinal
	 */
	public int getTimingSource() {
		return settings.timingSource().ordinal();
	}

	/**
     * Sets a new timing source based on the ordinal of the source within DaeTimingSource
     * (does not take effect until changes are applied).
     * 
	 * @param index the ordinal of the new timing source
	 */
	public void setTimingSource(int index) {
		DaeTimingSource value = DaeTimingSource.values()[index];
		settings.setTimingSource(value);
	}

	/**
     * Gets whether the SMP Veto has been selected or not.
     * 
     * @return true if the SMP Veto has been enabled
     */
	public boolean getSmpVeto() {
		return settings.smpVeto() == BinaryChoice.YES;
	}
	
	/**
     * Sets whether the SMP Veto has been enabled (does not 
     * take effect until changes are applied).
     * 
	 * @param selected true to enable the SMP Veto
	 */
	public void setSmpVeto(boolean selected) {
		settings.setSmpVeto(vetoIsActive(selected));
	}

	/**
     * Gets whether Veto 0 has been selected or not.
     * 
     * @return true if Veto 0 has been enabled
     */
	public boolean getVeto0() {
		return vetoIsActive(settings.veto0());
	}
	
    /**
     * Sets whether Veto 0 has been enabled (does not take effect until changes
     * are applied).
     * 
     * @param selected
     *            true to enable Veto 0
     */
	public void setVeto0(boolean selected) {
		settings.setVeto0(vetoIsActive(selected));	
	}

	/**
     * Gets whether Veto 1 has been selected or not.
     * 
     * @return true if Veto 1 has been enabled
     */
	public boolean getVeto1() {
		return vetoIsActive(settings.veto1());
	}
	
    /**
     * Sets whether Veto 1 has been enabled (does not take effect until changes
     * are applied).
     * 
     * @param selected
     *            true to enable Veto 1
     */
	public void setVeto1(boolean selected) {
		settings.setVeto1(vetoIsActive(selected));	
	}
	
	/**
     * Gets whether Veto 2 has been selected or not.
     * 
     * @return true if Veto 2 has been enabled
     */	
	public boolean getVeto2() {
		return vetoIsActive(settings.veto2());
	}
	
    /**
     * Sets whether Veto 2 has been enabled (does not take effect until changes
     * are applied).
     * 
     * @param selected
     *            true to enable Veto 2
     */
	public void setVeto2(boolean selected) {
		settings.setVeto2(vetoIsActive(selected));	
	}
	
	/**
     * Gets whether Veto 3 has been selected or not.
     * 
     * @return true if Veto 3 has been enabled
     */
	public boolean getVeto3() {
		return vetoIsActive(settings.veto3());
	}

    /**
     * Sets whether Veto 3 has been enabled (does not take effect until changes
     * are applied).
     * 
     * @param selected
     *            true to enable Veto 3
     */
	public void setVeto3(boolean selected) {
		settings.setVeto3(vetoIsActive(selected));	
	}
	
	/**
     * Gets whether the Fermi Chopper Veto has been selected or not.
     * 
     * @return true if the Fermi Chopper Veto has been enabled
     */
	public boolean getFermiChopperVeto() {
		return vetoIsActive(settings.fermiChopperVeto());
	}
	
    /**
     * Sets whether the Fermi Chopper Veto has been enabled (does not take
     * effect until changes are applied).
     * 
     * @param selected
     *            true to enable the Fermi Chopper Veto
     */
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
	
    /**
     * Gets whether the TS2 Veto has been selected or not.
     * 
     * @return true if the TS2 Veto has been enabled
     */
	public boolean getTs2PulseVeto() {
		return vetoIsActive(settings.ts2PulseVeto());
	}
	
    /**
     * Sets whether the TS2 Veto has been enabled (does not take effect until
     * changes are applied).
     * 
     * @param selected
     *            true to enable the TS2 Veto
     */
	public void setTs2PulseVeto(boolean selected) {
		settings.setTs2PulseVeto(vetoIsActive(selected));
	}
	
    /**
     * Gets whether the ISIS 50 Hz Veto has been selected or not.
     * 
     * @return true if the ISIS 50 Hz Veto has been enabled
     */
	public boolean getIsis50HzVeto() {
		return vetoIsActive(settings.isis50HzVeto());
	}
	
    /**
     * Sets whether the ISIS 50Hz Veto has been enabled (does not take effect
     * until changes are applied).
     * 
     * @param selected
     *            true to enable the ISIS 50Hz Veto
     */
	public void setIsis50HzVeto(boolean selected) {
		settings.setIsis50HzVeto(vetoIsActive(selected));
	}
	
    /**
     * Get the frequency at which autosave saves values.
     * 
     * @return The frequency, it's units are variable and can be found from
     *         getAutosaveUnits.
     */
	public int getAutosaveFrequency() {
		return updateSettings.getAutosaveFrequency();
	}
	
    /**
     * Set the frequency at which autosave saves values.
     * 
     * @param value
     *            The frequency, it's units are variable and can be found from
     *            getAutosaveUnits.
     */
	public void setAutosaveFrequency(int value) {
		updateSettings.setAutosaveFrequency(value);
	}
	
    /**
     * Get the units at which the frequency of autosaving is set.
     * 
     * @return An ordinal of AutosaveUnit giving the units.
     */
	public int getAutosaveUnits() {
		return updateSettings.getAutosaveUnits().ordinal();
	}
	
    /**
     * Set the units at which the frequency of autosaving is measured.
     * 
     * @param index
     *            An ordinal of AutosaveUnit giving the units.
     */
	public void setAutosaveUnits(int index) {
		updateSettings.setAutosaveUnits(AutosaveUnit.values()[index]);
	}
	
	private static boolean vetoIsActive(BinaryChoice value) {
		return value == BinaryChoice.YES;
	}
	
	private static BinaryChoice vetoIsActive(Boolean selected) {
		return BinaryChoice.values()[selected ? 1 : 0];
	}

    /**
     * Sets the object responsible for filling the file selection combo boxes
     * with appropriate options.
     * 
     * @param provider The content provider.
     */
    public void setComboContentProvider(DAEComboContentProvider provider) {
        this.comboContentProvider = provider;
    }
}

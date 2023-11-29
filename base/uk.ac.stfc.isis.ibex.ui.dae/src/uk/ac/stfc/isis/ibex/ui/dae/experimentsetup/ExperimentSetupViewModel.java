
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsViewModel;


/**
 * The view model that contains the logic for displaying how the experiment is
 * set up on the DAE.
 */
public class ExperimentSetupViewModel extends ModelObject {

	private ExperimentSetup model;
	
	private DataAcquisitionViewModel daeSettings = new DataAcquisitionViewModel();
	private TimeChannelsViewModel timeChannels = new TimeChannelsViewModel();
	private PeriodsViewModel periodSettings = new PeriodsViewModel();
	
	private String currentInstrumentName;
	private boolean isChanged = false;
    private Map<String, String> cachedValues = new HashMap<>();
    private final Map<String, Map<String, String>> instrumentCachedValues = new HashMap<>();
    private Map<String, ArrayList<Boolean>> radioBtnsCachedValues = new HashMap<>();
    private final Map<String, Map<String, ArrayList<Boolean>>> instrumentRadioBtnsCachedValues = new HashMap<>();
    private Map<String, ArrayList<String>> tableCachedValues = new HashMap<>();
    private final Map<String, Map<String, ArrayList<String>>> instrumentTableCachedValues = new HashMap<>();

    /**
     * Sets all view models used in the experiment setup perspective to the
     * models in the backend.
     * 
     * @param model the model of the experiment setup.
     */
	public void setModel(ExperimentSetup model) {
		this.model = model;			
		daeSettings.setModel(model.daeSettings());
		daeSettings.setUpdateSettings(model.updateSettings());
		daeSettings.setWiringTableList(model.wiringList(), model.getWiringTablesDir());
		daeSettings.setDetectorTableList(model.detectorTables(), model.getDetectorTablesDir());
		daeSettings.setSpectraTableList(model.spectraTables(), model.getSpectraTablesDir());
		
		timeChannels.setModel(model.timeChannels());
        timeChannels.setTimeChannelFileList(model.timeChannelFiles(), model.getTimeChannelsDir());
		
		periodSettings.setSettings(model.periodSettings());
		periodSettings.setPeriodFilesList(model.periodFiles(), model.getPeriodFilesDir());
		
		currentInstrumentName = Instrument.getInstance().currentInstrument().name();

	}
	
    /**
     * updates the experiment setup saved in the blockserver with changes made
     * in the frontend.
     * 
     * @throws IOException if the update failed
     */
	public void updateDae() throws IOException {
		model.sendAllSettings();
	}
	
    /**
     * Returns the view model for the time channel settings.
     * 
     * @return the time channel view model.
     */
	public TimeChannelsViewModel timeChannels() {
		return timeChannels;
	}

    /**
     * Returns the view model for the DAE settings.
     * 
     * @return the DAE view model.
     */
	public DataAcquisitionViewModel daeSettings() {
		return daeSettings;
	}

    /**
     * Returns the view model for the period settings.
     * 
     * @return the period view model.
     */
	public PeriodsViewModel periodSettings() {
		return periodSettings;
	}
	
	 /**
     * Saves if changes have been made in the experiment setup but haven't been applied.
     * 
     * @param isChanged 
     *                  True if changes have been made in the experiment setup but haven't been applied.
     */
    public void setIsChanged(boolean isChanged) {
        firePropertyChange("isChanged", this.isChanged, this.isChanged = isChanged);
    }
    
    /**
     * True if changes have been made in the experiment setup but haven't been applied.
     * 
     * @return
     *         whether or not changes have been made in the experiment setup but haven't been applied.
     */
    public boolean getIsChanged() {
        return isChanged;
    }
    
    /**
     * Adds a value to the cached value map.
     * 
     * @param key
     *          The key for a given value.
     * @param value
     *          The cached value.               
     */
    public void addtoCachedValues(String key, String value) {
        if (cachedValues.containsKey(key)) {
            cachedValues.replace(key, value);
        } else {
            cachedValues.put(key, value);
        }
    }
    
    /**
     * Returns a value from the cached value map.
     * 
     * @param key
     *          The key to get the desired value.
     * @return
     *          The desired cached value.
     */
    public String getItemFromCachedValues(String key) {
        return cachedValues.getOrDefault(key, "");
    }
    
    /**
     * Adds a value to the cached value map.
     * 
     * @param key
     *          The key for a given value.
     * @param value
     *          The cached value.               
     */
    public void addtoRadioBtnsCachedValues(String key, ArrayList<Boolean> value) {
        if (radioBtnsCachedValues.containsKey(key)) {
            radioBtnsCachedValues.replace(key, value);
        } else {
            radioBtnsCachedValues.put(key, value);
        }
    }
    
    /**
     * Returns a value from the cached value map.
     * 
     * @param key
     *          The key to get the desired value.
     * @return
     *          The desired cached value.
     */
    public ArrayList<Boolean> getItemFromRadioBtnsCachedValues(String key) {
        ArrayList<Boolean> dflt = new ArrayList<Boolean>(); 
        return radioBtnsCachedValues.getOrDefault(key, dflt);
    }
    
    /**
     * Adds a value to the cached value map.
     * 
     * @param key
     *          The key for a given value.
     * @param value
     *          The cached value.               
     */
    public void addtoTableCachedValues(String key, ArrayList<String> value) {
        if (tableCachedValues.containsKey(key)) {
            tableCachedValues.replace(key, value);
        } else {
            tableCachedValues.put(key, value);
        }
    }
    
    /**
     * Returns a value from the cached value map.
     * 
     * @param key
     *          The key to get the desired value.
     * @return
     *          The desired cached value.
     */
    public ArrayList<String> getItemFromTableCachedValues(String key) {
        ArrayList<String> dflt = new ArrayList<String>(); 
        return tableCachedValues.getOrDefault(key, dflt);
    }
    
    /**
     * Tells the experiment setup UI to reset its layout.
     */
    public void resetChangeLabels() {
        firePropertyChange("resetLayout", null, null);
    }
    
    /**
     * Sets the current cached value maps into a map for instruments.
     * 
     * @param name
     *              The name of the instrument
     */
    private void setInstrumentCachedValues(String name) {
        if (instrumentCachedValues.containsKey(name)) {
            instrumentCachedValues.replace(name, cachedValues);
        } else {
            instrumentCachedValues.put(name, cachedValues);
        }
        
        if (instrumentRadioBtnsCachedValues.containsKey(name)) {
            instrumentRadioBtnsCachedValues.replace(name, radioBtnsCachedValues);
        } else {
            instrumentRadioBtnsCachedValues.put(name, radioBtnsCachedValues);
        }
        
        if (instrumentTableCachedValues.containsKey(name)) {
            instrumentTableCachedValues.replace(name, tableCachedValues);
        } else {
            instrumentTableCachedValues.put(name, tableCachedValues);
        }
    }
    
    /**
     * Returns a map of cached values dependent on the instrument.
     * 
     * @param name 
     *              The name of the instrument.
     * @return
     *              The associated map of cached values.
     */
    private Map<String, String> getInstrumentCachedValues(String name) {
        Map<String, String> dflt = new HashMap<>();
        return instrumentCachedValues.getOrDefault(name, dflt);
    }
    
    /**
     * Returns a map of cached values for radio buttons dependent on the instrument.
     * 
     * @param name 
     *              The name of the instrument.
     * @return
     *              The associated map of cached values.
     */
    private Map<String, ArrayList<Boolean>> getRadioBtnsInstrumentCachedValues(String name) {
        Map<String, ArrayList<Boolean>> btnsDflt = new HashMap<>();
        return instrumentRadioBtnsCachedValues.getOrDefault(name, btnsDflt);
    }
    
    /**
     * Returns a map of cached values for the tables dependent on the instrument.
     * 
     * @param name 
     *              The name of the instrument.
     * @return
     *              The associated map of cached values.
     */
    private Map<String, ArrayList<String>> getTableInstrumentCachedValues(String name) {
        Map<String, ArrayList<String>> btnsDflt = new HashMap<>();
        return instrumentTableCachedValues.getOrDefault(name, btnsDflt);
    }
    
    /**
	 * Saves the current maps of cached values and loads the one related to the
	 * instrument that is being loaded.
	 * 
	 * @param newInstrument The name of the instrument that is being loaded.
	 */
	public void switchInstrumentCachedValues(String newInstrument) {
		setInstrumentCachedValues(currentInstrumentName);
		cachedValues = getInstrumentCachedValues(newInstrument);
		radioBtnsCachedValues = getRadioBtnsInstrumentCachedValues(newInstrument);
		tableCachedValues = getTableInstrumentCachedValues(newInstrument);
		currentInstrumentName = newInstrument;

	}
}

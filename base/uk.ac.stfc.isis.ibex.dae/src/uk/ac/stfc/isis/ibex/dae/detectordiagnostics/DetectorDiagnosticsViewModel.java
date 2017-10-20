 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The detector diagnostics view model. 
 * 
 * Holds data coming from the PVs and is databound to the UI panel.
 */
public class DetectorDiagnosticsViewModel extends ModelObject {
    
    private static DetectorDiagnosticsViewModel instance;
    
    private DetectorDiagnosticsModel pvs;
    
    private List<SpectrumInformation> spectra = new ArrayList<>();

    private List<Integer> integralsList;

    private List<Integer> maxSpecBinCount;

    private List<Double> countRatesList;

    private List<Integer> spectrumNumbersList;

    private Integer maxFrames;
    private Double integralTimeRangeTo;
    private Double integralTimeRangeFrom;
    private Integer numberOfSpectra;
    private Integer startingSpectrumNumber;
    private Integer period;
    private Integer spectraType;

    private boolean diagnosticsEnabled;
    
    /**
     * The list of all spectra that should be displayed.
     * 
     * @return the list of all spectra that should be displayed
     */
    public List<SpectrumInformation> getSpectra() {
        return spectra;
    }
    
    /**
     * Instance of this singleton.
     * @return this singleton
     */
    public static synchronized DetectorDiagnosticsViewModel getInstance() {
        if (instance == null) {
            instance = new DetectorDiagnosticsViewModel();
        }
        return instance;
    }
    
    /**
     * Updates the list of spectrum numbers.
     * 
     * @param spectrumNumbersList the list of spectrum numbers
     */
    public synchronized void updateSpectrumNumbers(final List<Integer> spectrumNumbersList) {
        
        this.spectrumNumbersList = spectrumNumbersList;
        
        try {
            for (int i = 0; i < spectrumNumbersList.size(); i++) {          
                spectra.get(i).setSpectrumNumber(spectrumNumbersList.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore spectra that don't match the spectra count
        }
    }

    /**
     * Updates the list of count rates.
     * 
     * @param countRatesList the list of count rates
     */
    public synchronized void updateCountRates(final List<Double> countRatesList) {
        
        this.countRatesList = countRatesList;
        
        try {    
            for (int i = 0; i < countRatesList.size(); i++) {          
                spectra.get(i).setCountRate(countRatesList.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore spectra that don't match the spectra count
        }
    }
    
    /**
     * Updates the list of maximum counts.
     * 
     * @param maxSpecBinCount the list of maximum counts
     */
    public synchronized void updateMaxSpecBinCount(final List<Integer> maxSpecBinCount) {
        
        this.maxSpecBinCount = maxSpecBinCount;
        
        try {
            for (int i = 0; i < maxSpecBinCount.size(); i++) {          
                spectra.get(i).setMaxSpecBinCount(maxSpecBinCount.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore spectra that don't match the spectra count
        }
    }
    
    /**
     * Updates the list of integrals.
     * 
     * @param integralsList the list of integrals
     */
    public synchronized void updateIntegrals(final List<Integer> integralsList) {
        
        this.integralsList = integralsList;
        
        try {
            for (int i = 0; i < integralsList.size(); i++) {          
                spectra.get(i).setIntegral(integralsList.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore spectra that don't match the spectra count
        }
    }

    /**
     * Starts observing the relevant PVs.
     */
    public synchronized void startObserving() {
        stopObserving();
        pvs = new DetectorDiagnosticsModel(this);
        pvs.startObserving();
        fireSpectraPropertyChangeOnGuiThread();
    }
    
    /**
     * Stops observing the relevant PVs.
     */
    public void stopObserving() {
        if (pvs != null) {
            pvs.stopObserving();
        }
        fireSpectraPropertyChangeOnGuiThread();
    }

    /**
     * Update the number of spectra that should be displayed.
     * 
     * @param size the new number of spectra
     */
    synchronized void updateSpectraCount(int size) {
        spectra.clear();
        for (int i = 0; i < size; i++) {
            spectra.add(new SpectrumInformation());
        }
        
        refresh();       
        fireSpectraPropertyChangeOnGuiThread();        
    }
    
    private void fireSpectraPropertyChangeOnGuiThread() {
        firePropertyChangeOnGuiThread("spectra", null, spectra);
    }
    
    private void firePropertyChangeOnGuiThread(final String key, final Object oldValue, final Object newValue) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                firePropertyChange(key, oldValue, newValue);
            }
        });
    }
    
    private void refresh() {
        updateCountRates(countRatesList);
        updateIntegrals(integralsList);
        updateMaxSpecBinCount(maxSpecBinCount);
        updateSpectrumNumbers(spectrumNumbersList);
    }
    
    /**
     * Returns the integer index of the spectra type.
     * 
     * @return the integer index of the spectra type
     */
    public Integer getSpectraType() {
        return spectraType; 
    }
    
    /**
     * Sets the spectra type.
     * 
     * @param type the type
     */
    public void setSpectraType(Integer type) {
        if (type == null) {
            return;
        }
        pvs.setSpectraToDisplay(type);
        firePropertyChangeOnGuiThread("spectraType", this.spectraType, this.spectraType = type);
    }
    
    /**
     * Gets the period.
     * 
     * @return the period
     */
    public Integer getPeriod() {
        return period;
    }
    
    /**
     * Sets the period.
     * 
     * @param value the period
     */
    public void setPeriod(Integer value) {
        if (value == null) {
            return;
        }
        pvs.setPeriod(value);
        firePropertyChangeOnGuiThread("period", this.period, this.period = value);
    }
    
    /**
     * Gets the starting spectrum number.
     *
     * @return the starting spectrum number
     */
    public Integer getStartingSpectrumNumber() {
        return startingSpectrumNumber;
    }
    
    /**
     * Sets the starting spectrum number.
     *
     * @param value the new starting spectrum number
     */
    public void setStartingSpectrumNumber(Integer value) {
        if (value == null) {
            return;
        }
        pvs.setStartingSpectrumNumber(value);
        firePropertyChangeOnGuiThread("startingSpectrumNumber", this.startingSpectrumNumber, this.startingSpectrumNumber = value);
    }
    
    /**
     * Gets the number of spectra.
     *
     * @return the number of spectra
     */
    public Integer getNumberOfSpectra() {
        return numberOfSpectra;
    }
    
    /**
     * Sets the number of spectra.
     *
     * @param value the new number of spectra
     */
    public void setNumberOfSpectra(Integer value) {
        if (value == null) {
            return;
        }
        pvs.setNumberOfSpectra(value);
        firePropertyChangeOnGuiThread("numberOfSpectra", this.numberOfSpectra, this.numberOfSpectra = value);
    }
    
    /**
     * Gets the integral time range from.
     *
     * @return the integral time range from
     */
    public String getIntegralTimeRangeFrom() {
        return integralTimeRangeFrom != null ? integralTimeRangeFrom.toString() : "";
    }
    
    /**
     * Sets the integral time range from.
     *
     * @param value the new integral time range from
     */
    public void setIntegralTimeRangeFrom(String value) {
        if (value == null) {
            return;
        }
        Double doubleValue = Double.parseDouble(value);
        pvs.setIntegralTimeRangeFrom(doubleValue);
        firePropertyChangeOnGuiThread("integralTimeRangeFrom", this.integralTimeRangeFrom, this.integralTimeRangeFrom = doubleValue);
    }
    
    /**
     * Gets the integral time range to.
     *
     * @return the integral time range to
     */
    public String getIntegralTimeRangeTo() {
        return integralTimeRangeTo != null ? integralTimeRangeTo.toString() : "";
    }
    
    /**
     * Sets the integral time range to.
     *
     * @param value the new integral time range to
     */
    public void setIntegralTimeRangeTo(String value) {
        if (value == null) {
            return;
        }
        Double doubleValue = Double.parseDouble(value);
        pvs.setIntegralTimeRangeTo(doubleValue);
        firePropertyChangeOnGuiThread("integralTimeRangeTo", this.integralTimeRangeTo, this.integralTimeRangeTo = doubleValue);
    }
    
    /**
     * Gets the max frames.
     *
     * @return the max frames
     */
    public Integer getMaxFrames() {
        return maxFrames;
    }
    
    /**
     * Sets the max frames.
     *
     * @param value the new max frames
     */
    public void setMaxFrames(Integer value) {
        if (value == null) {
            return;
        }
        pvs.setMaxFrames(value);
        firePropertyChangeOnGuiThread("maxFrames", this.maxFrames, this.maxFrames = value);
    }

    /**
     * Sets the diagnostics enabled.
     *
     * @param isEnabled the new diagnostics enabled
     */
    public void setDiagnosticsEnabled(boolean isEnabled) {
        firePropertyChangeOnGuiThread("diagnosticsEnabled", this.diagnosticsEnabled, this.diagnosticsEnabled = isEnabled);
    }
    
    /**
     * Checks if is diagnostics enabled.
     *
     * @return true, if is diagnostics enabled
     */
    public boolean isDiagnosticsEnabled() {
        return diagnosticsEnabled;
    }

}

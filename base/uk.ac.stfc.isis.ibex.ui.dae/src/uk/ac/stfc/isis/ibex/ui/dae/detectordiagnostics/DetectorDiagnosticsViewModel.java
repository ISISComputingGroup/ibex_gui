 /*
 * Copyright (C) 2012-2017 Science & Technology Facilities Council. This file is
 * part of the ISIS IBEX application. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsModel;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.IDetectorDiagnosticsViewModelBinding;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.SpectrumInformation;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The detector diagnostics view model. 
 * 
 * Holds data coming from the PVs and is databound to the UI panel.
 */
public class DetectorDiagnosticsViewModel extends ModelObject implements IDetectorDiagnosticsViewModelBinding {
    
    private static DetectorDiagnosticsViewModel instance;
    private DataBindingContext bindingContext = new DataBindingContext();
    
    private DetectorDiagnosticsModel detectorDiagnosticsModel;
    
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

    /** True if the diagnostics is enabled */
    private boolean diagnosticsEnabled;

    /** Set to true if the diagnostics should be enabled */
    private boolean enableDiagnostics = true;

    /** Error last time Enable Diagnostics was written; blank for no error */
    private String writeToEnableDiagnosticError;

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
     * Update the spectrum numbers used.
     * 
     * @param spectrumNumbersList the new values
     */
    @Override
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
     * Update the count rates for the spectra.
     * 
     * @param countRatesList the new values
     */
    @Override
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
     * Update the maximum bin count for the spectra.
     * 
     * @param maxSpecBinCount the new values
     */
    @Override
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
     * Update the integrals for the spectra.
     * 
     * @param integralsList the new values
     */
    @Override
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
        detectorDiagnosticsModel = DetectorDiagnosticsModel.getInstance();
        detectorDiagnosticsModel.bind(this);
        fireSpectraPropertyChangeOnGuiThread();
        
        bindingContext.bindValue(BeanProperties.value("writeToEnableDiagnosticError").observe(this),
                BeanProperties.value("writeToEnableDiagnosticError").observe(detectorDiagnosticsModel));
        
    }
    

    /**
     * Update the number of spectra that should be displayed.
     * 
     * @param size the new number of spectra
     */
    @Override
    public synchronized void updateSpectraCount(int size) {
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
    @Override
    public void setSpectraType(Integer type) {
        if (type == null) {
            return;
        }
        detectorDiagnosticsModel.setSpectraToDisplay(type);
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
     * Get the detector diagnostics Model.
     * 
     * @return the pvs
     */
    public DetectorDiagnosticsModel getDetectorDiagnosticsModel() {
        return detectorDiagnosticsModel;
    }

    /**
     * Sets the period.
     * 
     * @param value
     *            the period
     */
    @Override
    public void setPeriod(Integer value) {
        if (value == null) {
            return;
        }
        detectorDiagnosticsModel.setPeriod(value);
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
    @Override
    public void setStartingSpectrumNumber(Integer value) {
        if (value == null) {
            return;
        }
        detectorDiagnosticsModel.setStartingSpectrumNumber(value);
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
    @Override
    public void setNumberOfSpectra(Integer value) {
        if (value == null) {
            return;
        }
        detectorDiagnosticsModel.setNumberOfSpectra(value);
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
     * Set the integral time range lower limit.
     * 
     * @param value new value
     */
    @Override
    public void setIntegralTimeRangeFrom(String value) {
        if (value == null) {
            return;
        }
        Double doubleValue = Double.parseDouble(value);
        detectorDiagnosticsModel.setIntegralTimeRangeFrom(doubleValue);
        firePropertyChangeOnGuiThread("integralTimeRangeFrom", this.integralTimeRangeFrom, this.integralTimeRangeFrom = doubleValue);
    }
    
    /**
     * @return the integral time range upper limit
     */
    public String getIntegralTimeRangeTo() {
        return integralTimeRangeTo != null ? integralTimeRangeTo.toString() : "";
    }
    
    /**
     * Set the integral time range upper limit.
     * 
     * @param value the new value
     */
    @Override
    public void setIntegralTimeRangeTo(String value) {
        if (value == null) {
            return;
        }
        Double doubleValue = Double.parseDouble(value);
        detectorDiagnosticsModel.setIntegralTimeRangeTo(doubleValue);
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
     * Set the maximum frames.
     * 
     * @param value the new value
     */
    @Override
    public void setMaxFrames(Integer value) {
        if (value == null) {
            return;
        }
        detectorDiagnosticsModel.setMaxFrames(value);
        firePropertyChangeOnGuiThread("maxFrames", this.maxFrames, this.maxFrames = value);
    }

    /**
     * Sets the diagnostics enabled. (This is the readback to set use the
     * enabledDiagnostics property)
     *
     * @param isEnabled the new diagnostics enabled
     */
    @Override
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

    /**
     * Sets the enable diagnostics flag, this enables the diagnostics so it can
     * be observed. The read back of whether it is enabled is diagnostics
     * enabled.
     *
     * @param enableDiagnostics
     *            set that the the detector diagnostics should be enabled.
     */
    public void setEnableDiagnostics(boolean enableDiagnostics) {
        firePropertyChangeOnGuiThread("enableDiagnostics", this.enableDiagnostics,
                this.enableDiagnostics = enableDiagnostics);
    }

    /**
     * Checks the value of enable diagnostics.
     *
     * @return true, if is diagnostics will be enabled; false otherwise.
     */
    public boolean isEnableDiagnostics() {
        return enableDiagnostics;
    }

    /**
     * @return the write to enable diagnostic error count
     */
    public String getWriteToEnableDiagnosticError() {
        return writeToEnableDiagnosticError;
    }

    /**
     * Set the error that occurred when trying to write to Enable Diagnostic
     * Error.
     * 
     * @param error
     *            the last error that happened when Enable Diagnostics was
     *            written to
     */
    public void setWriteToEnableDiagnosticError(String error) {
        firePropertyChange("writeToEnableDiagnosticError", this.writeToEnableDiagnosticError,
                this.writeToEnableDiagnosticError = error);
    }

    /**
     * Set whether to show the detector diagnostics.
     * 
     * @param isShown true to show; false otherwise
     */
    public void setVisible(boolean isShown) {
        detectorDiagnosticsModel.setDetectorDiagnosticsEnabled(isShown);
    }

}

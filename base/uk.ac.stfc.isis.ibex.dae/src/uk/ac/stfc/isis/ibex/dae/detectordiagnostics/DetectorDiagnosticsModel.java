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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 *
 */
public class DetectorDiagnosticsModel extends ModelObject {
    
    private static DetectorDiagnosticsModel instance;
    
    private SpectrumDiagnosticsPvConnections pvs;
    
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
    
    public List<SpectrumInformation> getSpectra() {
        return spectra;
    }
    
    /**
     * Instance of this singleton.
     * @return this singleton
     */
    public static synchronized DetectorDiagnosticsModel getInstance() {
        if (instance == null) {
            instance = new DetectorDiagnosticsModel();
        }
        return instance;
    }
    
    /**
     * @param spectrumNumbersList the list of spectrum numbers
     */
    public synchronized void updateSpectrumNumbers(final List<Integer> spectrumNumbersList) {
        
        this.spectrumNumbersList = spectrumNumbersList;
        
        if (spectrumNumbersList.size() != spectra.size()) {
            return;
        }
        
        for (int i = 0; i < spectrumNumbersList.size(); i++) {          
            spectra.get(i).setSpectrumNumber(spectrumNumbersList.get(i));
        }
    }

    /**
     * @param countRatesList the list of count rates
     */
    public synchronized void updateCountRates(final List<Double> countRatesList) {
        
        this.countRatesList = countRatesList;
        
        if (countRatesList.size() != spectra.size()) {
            return;
        }
        
        for (int i = 0; i < countRatesList.size(); i++) {          
            spectra.get(i).setCountRate(countRatesList.get(i));
        }
    }
    
    /**
     * @param maxSpecBinCount the list of maximum counts
     */
    public synchronized void updateMaxSpecBinCount(final List<Integer> maxSpecBinCount) {
        
        this.maxSpecBinCount = maxSpecBinCount;
        
        if (maxSpecBinCount.size() != spectra.size()) {
            return;
        }
        
        for (int i = 0; i < maxSpecBinCount.size(); i++) {          
            spectra.get(i).setMaxSpecBinCount(maxSpecBinCount.get(i));
        }
    }
    
    /**
     * @param integralsList the list of integrals
     */
    public synchronized void updateIntegrals(final List<Integer> integralsList) {
        
        this.integralsList = integralsList;
        
        if (integralsList.size() != spectra.size()) {
            return;
        }
        
        for (int i = 0; i < integralsList.size(); i++) {          
            spectra.get(i).setIntegral(integralsList.get(i));
        }
    }

    /**
     * 
     */
    public synchronized void startObserving() {
        stopObserving();
        pvs = new SpectrumDiagnosticsPvConnections(this);
        pvs.startObserving();
        fireSpectraPropertyChangeOnGuiThread();
    }
    
    public void stopObserving() {
        if (pvs != null) {
            pvs.stopObserving();
        }
    }

    /**
     * @param size
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
        firePropertyChangeOnGuiThread("spectra", Collections.EMPTY_LIST, spectra);
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
    
    public Integer getSpectraType(){
        return spectraType; 
    }
    
    public void setSpectraType(Integer type){
        if (type == null) {
            return;
        }
        pvs.setSpectraToDisplay(type);
        firePropertyChangeOnGuiThread("spectraType", this.spectraType, this.spectraType = type);
    }
    
    public Integer getPeriod(){
        return period;
    }
    
    public void setPeriod(Integer value){
        if (value == null) {
            return;
        }
        pvs.setPeriod(value);
        firePropertyChangeOnGuiThread("period", this.period, this.period = value);
    }
    
    public Integer getStartingSpectrumNumber(){
        return startingSpectrumNumber;
    }
    
    public void setStartingSpectrumNumber(Integer value){
        if (value == null) {
            return;
        }
        pvs.setStartingSpectrumNumber(value);
        firePropertyChangeOnGuiThread("startingSpectrumNumber", this.startingSpectrumNumber, this.startingSpectrumNumber = value);
    }
    
    public Integer getNumberOfSpectra(){
        return numberOfSpectra;
    }
    
    public void setNumberOfSpectra(Integer value){
        if (value == null) {
            return;
        }
        pvs.setNumberOfSpectra(value);
        firePropertyChangeOnGuiThread("numberOfSpectra", this.numberOfSpectra, this.numberOfSpectra = value);
    }
    
    public Integer getIntegralTimeRangeFrom(){
        return integralTimeRangeFrom != null ? integralTimeRangeFrom.intValue() : null;
    }
    
    public void setIntegralTimeRangeFrom(Integer value){
        if (value == null) {
            return;
        }
        pvs.setIntegralTimeRangeFrom(value.doubleValue());
        firePropertyChangeOnGuiThread("integralTimeRangeFrom", this.integralTimeRangeFrom, this.integralTimeRangeFrom = value.doubleValue());
    }
    
    public Integer getIntegralTimeRangeTo(){
        return integralTimeRangeTo != null ? integralTimeRangeTo.intValue() : null;
    }
    
    public void setIntegralTimeRangeTo(Integer value){
        if (value == null) {
            return;
        }
        pvs.setIntegralTimeRangeTo(value.doubleValue());
        firePropertyChangeOnGuiThread("integralTimeRangeTo", this.integralTimeRangeTo, this.integralTimeRangeTo = value.doubleValue());
    }
    
    public Integer getMaxFrames(){
        return maxFrames;
    }
    
    public void setMaxFrames(Integer value){
        if (value == null) {
            return;
        }
        pvs.setMaxFrames(value);
        firePropertyChangeOnGuiThread("maxFrames", this.maxFrames, this.maxFrames = value);
    }

}

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
    
    public static final int PAGE_SIZE = 1000;
    
    private static DetectorDiagnosticsModel instance;
    
    private SpectrumDiagnosticsPvConnections pvs;
    
    private FutureValueHolder futureValues = new FutureValueHolder();
    
    private List<SpectrumInformation> spectra = new ArrayList<>();
    
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
        
        if(spectrumNumbersList.size() != spectra.size()){
            futureValues.spectrumNumbersList = spectrumNumbersList;
            applyFutureValuesIfValid();
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
        
        if(countRatesList.size() != spectra.size()){
            futureValues.countRatesList = countRatesList;
            applyFutureValuesIfValid();
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
        
        if(maxSpecBinCount.size() != spectra.size()){
            futureValues.maximumsList = maxSpecBinCount;
            applyFutureValuesIfValid();
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
        
        if(integralsList.size() != spectra.size()){
            futureValues.integralsList = integralsList;
            applyFutureValuesIfValid();
            return;
        }
        
        for (int i = 0; i < integralsList.size(); i++) {          
            spectra.get(i).setIntegral(integralsList.get(i));
        }
    }

    /**
     * 
     */
    public void startObserving() {
        pvs = new SpectrumDiagnosticsPvConnections();
        pvs.startObserving();
        firePropertyChange("spectra", Collections.EMPTY_LIST, spectra);
    }

    /**
     * @param size
     */
    private synchronized void updateSpectraCount(int size) {
        spectra.clear();
        for (int i = 0; i < size; i++) {
            spectra.add(new SpectrumInformation());
        }
    }
    
    private void applyFutureValuesIfValid() {
        if (futureValues.spectrumNumbersList.size() == futureValues.countRatesList.size() 
                && futureValues.countRatesList.size() == futureValues.maximumsList.size() 
                && futureValues.maximumsList.size() == futureValues.integralsList.size()) {
            
            updateSpectraCount(futureValues.spectrumNumbersList.size());
            
            updateCountRates(futureValues.countRatesList);
            updateIntegrals(futureValues.integralsList);
            updateMaxSpecBinCount(futureValues.maximumsList);
            updateSpectrumNumbers(futureValues.spectrumNumbersList);
            
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    firePropertyChange("spectra", Collections.EMPTY_LIST, spectra);
                }
            });
        }
    }

}

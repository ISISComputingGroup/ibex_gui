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
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 *
 */
public class DetectorDiagnosticsModel extends ModelObject {
    
    public static final int PAGE_SIZE = 1000;
    
    private static DetectorDiagnosticsModel instance;
    
    private SpectrumDiagnostics pvs;
    
    private SpectrumRange range;
    
    private DetectorDiagnosticsModel() {
        
    }
    
    private List<SpectrumInformation> spectra = new ArrayList<>();
    
    public List<SpectrumInformation> getSpectra(){
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
     * @param countRatesList the list of values for that page
     */
    public void updateCountRates(final List<Double> countRatesList) {
        for (int i = 0; i < countRatesList.size(); i++) {          
            spectra.get(i).setCountRate(countRatesList.get(i));
        }
    }
    
    /**
     * @param countRatesList the list of values for that page
     */
    public void updateMaxSpecBinCount(final List<Long> countRatesList) {
        for (int i = 0; i < countRatesList.size(); i++) {          
            spectra.get(i).setMaxSpecBinCount(countRatesList.get(i));
        }
    }

    /**
     * 
     */
    public void startObserving() {
        pvs = new SpectrumDiagnostics();
        setRange(0,1);
    }

    /**
     * 
     */
    public void setRange(int start, int numberOfSpectra) {
        range = new SpectrumRange(start, numberOfSpectra);
        
        spectra.clear();
        for(Integer spectrumNumber : range.spectraRequired()){
            spectra.add(new SpectrumInformation(spectrumNumber));
        }
        pvs.startObserving(range);
        firePropertyChange("spectra", null, spectra);
    }

}

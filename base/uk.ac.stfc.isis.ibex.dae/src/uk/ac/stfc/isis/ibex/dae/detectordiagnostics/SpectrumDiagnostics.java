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

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.FloatArrayChannel;

/**
 *
 */
public class SpectrumDiagnostics {
    
    private final ObservableFactory obsFactory;
    
    private final DetectorDiagnosticsModel model;

    /**
     * The observable.
     */
    public List<ForwardingObservable<float[]>> countRatePages = new ArrayList<>();

    /**
     * @param spectrumRange the spectrum range
     */
    public SpectrumDiagnostics() {    
        model = DetectorDiagnosticsModel.getInstance();
        obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);                   
    }
    
    public void startObserving(SpectrumRange spectrumRange){
        
        
        for (final Integer page : spectrumRange.pagesRequired()) {
            
            ForwardingObservable<float[]> countRate = obsFactory.getSwitchableObservable(new FloatArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:CNTRATE"));
            
            countRatePages.add(countRate);
            
            countRate.addObserver(new Observer<float[]>() {
                @Override
                public void onValue(float[] values) {
                    
                    System.out.println("aaaaaaaaaaaaa " + values);
                    
                    if (values == null || values.length == 0) {
                        return;
                    }
                    
                    // Convert to collection for ease of use
                    List<Float> valuesList = new ArrayList<>(values.length);
                    for (float value : values) {
                        valuesList.add(value);
                    }
                    
                    model.updateValues(page, valuesList);
                    
                }
    
                @Override
                public void onError(Exception e) {
                    // 
                }
    
                @Override
                public void onConnectionStatus(boolean isConnected) {
                    // 
                }
    
                @Override
                public void update(float[] value, Exception error, boolean isConnected) {
                    // 
                }            
            });
        }
        
    }

}

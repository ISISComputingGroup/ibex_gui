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
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntArrayChannel;

/**
 *
 */
public class SpectrumDiagnosticsPvConnections {
    
    private final ObservableFactory obsFactory;
    
    private final DetectorDiagnosticsModel model;

    /**
     * The observable for count rates.
     */
    public ForwardingObservable<double[]> countRate;
    
    /**
     * The observable for the maximum spectrum bin count.
     */
    public ForwardingObservable<int[]> maxSpecBinCount;
    
    /**
     * The observable for the integral.
     */
    public ForwardingObservable<int[]> integral;

    private SwitchableObservable<int[]> spectrumNumbers;

    /**
     * @param spectrumRange the spectrum range
     */
    public SpectrumDiagnosticsPvConnections() {    
        model = DetectorDiagnosticsModel.getInstance();
        obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);                   
    }
    
    public void startObserving(){
        
        spectrumNumbers = obsFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SPEC"));
        
        spectrumNumbers.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateSpectrumNumbers(convertPrimitiveIntArrayToList(values)); 
            }         
        });
            
        countRate = obsFactory.getSwitchableObservable(new DoubleArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:CNTRATE"));
        
        countRate.addObserver(new SpectrumObserver<double[]>() {
            @Override
            public void onNonNullValue(double[] values) {
                model.updateCountRates(convertPrimitiveDoubleArrayToList(values)); 
            }         
        });
        
        integral = obsFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SUM"));
        
        integral.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateIntegrals(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
        maxSpecBinCount = obsFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:MAX"));
        
        maxSpecBinCount.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateMaxSpecBinCount(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
    }
    
    private List<Double> convertPrimitiveDoubleArrayToList(double[] array){
        // Convert to collection for ease of use
        // Can't use Arrays.asList() because it's an array of primitives.
        List<Double> valuesList = new ArrayList<>(array.length);
        for (double value : array) {
            valuesList.add(value);
        }
        return valuesList;
    }
    
    private List<Integer> convertPrimitiveIntArrayToList(int[] array){
        // Convert to collection for ease of use
        // Can't use Arrays.asList() because it's an array of primitives.
        List<Integer> valuesList = new ArrayList<>(array.length);
        for (int value : array) {
            valuesList.add(value);
        }
        return valuesList;
    }
    
    private abstract class SpectrumObserver<T> implements Observer<T>{
        
        public abstract void onNonNullValue(T value);
        
        @Override
        public void onValue(T value) {
            if (value != null) {
                onNonNullValue(value);
            } 
        }
        
        @Override
        public void onError(Exception e) {
            System.out.println("error!");
            e.printStackTrace();
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
        }

        @Override
        public void update(T value, Exception error, boolean isConnected) {
            if (value != null) {
                onValue(value);
            }
            
            if (error != null) {
                onError(error);
            }
            
            onConnectionStatus(isConnected);
        }  
    }

}

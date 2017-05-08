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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntegerChannel;

/**
 *
 */
public class SpectrumDiagnosticsPvConnections {
    
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

    private WritableFactory writableFactory;
    private ObservableFactory observableFactory;
    
    private Writable<Integer> diagnosticsEnabled;
    private Writable<Integer> spectraToDisplay;
    private Writable<Integer> period;
    private Writable<Integer> startingSpectrumNumber;
    private Writable<Integer> numberOfSpectra;
    private Writable<Double> integralTimeRangeTo;
    private Writable<Double> integralTimeRangeFrom;
    private Writable<Integer> maxFrames;
    
    private Observable<Integer> numberOfSpectraThatMatchedCriteria;

    private ClosableObservable<Integer> maxFramesObservable;

    private ClosableObservable<Double> integralLowLimitObservable;

    private ClosableObservable<Double> integralHighLimitObservable;

    private ClosableObservable<Integer> numberOfSpectraObservable;

    private ClosableObservable<Integer> startingSpectrumNumberObservable;

    private ClosableObservable<Integer> spectraPeriodsObservable;

    private ClosableObservable<SpectraToDisplay> spectraTypeObservable;

    private SwitchableObservable<Boolean> diagnosticsEnabledObservable;

    public SpectrumDiagnosticsPvConnections(final DetectorDiagnosticsModel model) {    
        this.model = model;

        setUpWritables();  
    }
    
    private void setUpWritables() {
        
        writableFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        
        diagnosticsEnabled = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:ENABLE:SP"));
        spectraToDisplay = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:SHOW:SP"));
        period = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:PERIOD:SP"));
        startingSpectrumNumber = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:START:SP")); 
        numberOfSpectra = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:NUM:SP"));
        integralTimeRangeTo = writableFactory.getSwitchableWritable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTHIGH:SP"));
        integralTimeRangeFrom = writableFactory.getSwitchableWritable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTLOW:SP"));
        maxFrames = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:FRAMES:SP"));
        
    }
    
    private void setDiagnosticsEnabled(final boolean enabled) {
        
        // Can't actually write to PV yet - write a "checked write" function that will throw an exception if it can't connect.
        
        System.out.println("Setting detector diagnostics enabled PV to " + enabled);
        if (enabled) {
            try {
                diagnosticsEnabled.write(1);
                throw new IOException();
                // TODO: Replace below exception with IOException once #2298 is merged
            } catch (IOException e) {
                diagnosticsEnabled.subscribe(new SameTypeWriter<Integer>() {

                    @Override
                    public void write(Integer value) {
                        diagnosticsEnabled.write(value);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
 
                    @Override
                    public void onCanWriteChanged(boolean canWrite) {
                        if (canWrite) {
                            write(1);
                        }
                    }
                    
                });
            }
        } else {
            diagnosticsEnabled.write(0);
        }
    }
    
    public void setSpectraToDisplay(Integer value){
        spectraToDisplay.write(value);
    }
    
    public void setPeriod(Integer value){
        period.write(value);
    }
    
    public void setStartingSpectrumNumber(Integer value){
        startingSpectrumNumber.write(value);
    }
    
    public void setNumberOfSpectra(Integer value){
        numberOfSpectra.write(value);
    }
    
    public void setIntegralTimeRangeFrom(Double value) {
        integralTimeRangeFrom.write(value);
    }
    
    public void setIntegralTimeRangeTo(Double value) {
        integralTimeRangeTo.write(value);
    }
    
    public void setMaxFrames(Integer value) {
        maxFrames.write(value);
    }
    
    public void startObserving() {
        
        setDiagnosticsEnabled(true);
        
        observableFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        
        diagnosticsEnabledObservable = observableFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix("DAE:DIAG:ENABLED"));       
        diagnosticsEnabledObservable.addObserver(new SpectrumObserver<Boolean>() {
            @Override
            public void onNonNullValue(Boolean value) {
                if (value != null) {
                    model.setDiagnosticsEnabled(value);
                } else {
                    model.setDiagnosticsEnabled(false);
                }
            }  
            
            @Override
            public void onConnectionStatus(boolean isConnected) {
                if (!isConnected) {
                    model.setDiagnosticsEnabled(false);
                }
            }
        });
          
        spectrumNumbers = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SPEC"));       
        spectrumNumbers.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateSpectrumNumbers(convertPrimitiveIntArrayToList(values)); 
            }         
        });
            
        countRate = observableFactory.getSwitchableObservable(new DoubleArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:CNTRATE"));       
        countRate.addObserver(new SpectrumObserver<double[]>() {
            @Override
            public void onNonNullValue(double[] values) {
                model.updateCountRates(convertPrimitiveDoubleArrayToList(values)); 
            }         
        });
        
        integral = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SUM"));       
        integral.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateIntegrals(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
        maxSpecBinCount = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:MAX"));       
        maxSpecBinCount.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                model.updateMaxSpecBinCount(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
        numberOfSpectraThatMatchedCriteria = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:MATCH"));      
        numberOfSpectraThatMatchedCriteria.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                model.updateSpectraCount(value);
            }  
        });
        
        maxFramesObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:FRAMES:SP"));      
        maxFramesObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                model.setMaxFrames(value);
            }  
        });
        
        integralLowLimitObservable = observableFactory.getPVObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTLOW:SP"));      
        integralLowLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                model.setIntegralTimeRangeFrom(value.intValue());
            }  
        });
        
        integralHighLimitObservable = observableFactory.getPVObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTHIGH:SP"));      
        integralHighLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                model.setIntegralTimeRangeTo(value.intValue());
            }  
        });
        
        numberOfSpectraObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:NUM:SP"));      
        numberOfSpectraObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                model.setNumberOfSpectra(value);
            }  
        });
        
        startingSpectrumNumberObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:START:SP"));      
        startingSpectrumNumberObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                model.setStartingSpectrumNumber(value);
            }  
        });
        
        spectraPeriodsObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:PERIOD:SP"));      
        spectraPeriodsObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                model.setPeriod(value);
            }  
        });
        
        spectraTypeObservable = observableFactory.getPVObservable(new EnumChannel<SpectraToDisplay>(SpectraToDisplay.class), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:SHOW"));      
        spectraTypeObservable.addObserver(new SpectrumObserver<SpectraToDisplay>() {
            @Override
            public void onNonNullValue(SpectraToDisplay value) {
                model.setSpectraType(value.ordinal());
            }  
        });
        
    }
    
    public void stopObserving() {
        setDiagnosticsEnabled(false);
        
        spectrumNumbers.close();
        countRate.close();
        integral.close();
        maxSpecBinCount.close();

    }
    
    private List<Double> convertPrimitiveDoubleArrayToList(final double[] array) {
        // Convert to collection for ease of use
        // Can't use Arrays.asList() because it's an array of primitives.
        List<Double> valuesList = new ArrayList<>(array.length);
        for (Double value : array) {
            valuesList.add(value);
        }
        return valuesList;
    }
    
    private List<Integer> convertPrimitiveIntArrayToList(final int[] array) {
        // Convert to collection for ease of use
        // Can't use Arrays.asList() because it's an array of primitives.
        List<Integer> valuesList = new ArrayList<>(array.length);
        for (Integer value : array) {
            valuesList.add(value);
        }
        return valuesList;
    }
    
    private abstract class SpectrumObserver<T> implements Observer<T> {
        
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
                onNonNullValue(value);
            }
            
            if (error != null) {
                onError(error);
            }
            
            onConnectionStatus(isConnected);
        }  
    }

}

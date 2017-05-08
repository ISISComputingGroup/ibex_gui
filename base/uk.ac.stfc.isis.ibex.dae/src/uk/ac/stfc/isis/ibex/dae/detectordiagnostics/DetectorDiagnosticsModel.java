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
 * The Class DetectorDiagnosticsModel.
 */
public class DetectorDiagnosticsModel {
    
    private final DetectorDiagnosticsViewModel viewModel;

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

    /**
     * Constructor.
     * @param viewModel the view model
     */
    public DetectorDiagnosticsModel(final DetectorDiagnosticsViewModel viewModel) {    
        this.viewModel = viewModel;

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
        
        try {
            if (enabled) {
                diagnosticsEnabled.write(1);
            } else {
                diagnosticsEnabled.write(0);
            }
            // Replace below exception with IOException once #2298 is merged
            // The below is a hack so that the compiler doesn't complain about exceptions that are never thrown...
            if (new Boolean(false)) {
                throw new IOException();
            }
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
    }
    
    /**
     * Sets the spectra to display.
     *
     * @param value the new spectra to display
     */
    public void setSpectraToDisplay(Integer value) {
        spectraToDisplay.write(value);
    }
    
    /**
     * Sets the period.
     *
     * @param value the new period
     */
    public void setPeriod(Integer value) {
        period.write(value);
    }
    
    /**
     * Sets the starting spectrum number.
     *
     * @param value the new starting spectrum number
     */
    public void setStartingSpectrumNumber(Integer value) {
        startingSpectrumNumber.write(value);
    }
    
    /**
     * Sets the number of spectra.
     *
     * @param value the new number of spectra
     */
    public void setNumberOfSpectra(Integer value) {
        numberOfSpectra.write(value);
    }
    
    /**
     * Sets the integral time range from.
     *
     * @param value the new integral time range from
     */
    public void setIntegralTimeRangeFrom(Double value) {
        integralTimeRangeFrom.write(value);
    }
    
    /**
     * Sets the integral time range to.
     *
     * @param value the new integral time range to
     */
    public void setIntegralTimeRangeTo(Double value) {
        integralTimeRangeTo.write(value);
    }
    
    /**
     * Sets the max frames.
     *
     * @param value the new max frames
     */
    public void setMaxFrames(Integer value) {
        maxFrames.write(value);
    }
    
    /**
     * Start observing.
     */
    public void startObserving() {
        
        setDiagnosticsEnabled(true);
        
        observableFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        
        diagnosticsEnabledObservable = observableFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix("DAE:DIAG:ENABLED"));       
        diagnosticsEnabledObservable.addObserver(new SpectrumObserver<Boolean>() {
            @Override
            public void onNonNullValue(Boolean value) {
                if (value != null) {
                    viewModel.setDiagnosticsEnabled(value);
                } else {
                    viewModel.setDiagnosticsEnabled(false);
                }
            }  
            
            @Override
            public void onConnectionStatus(boolean isConnected) {
                if (!isConnected) {
                    viewModel.setDiagnosticsEnabled(false);
                }
            }
        });
          
        spectrumNumbers = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SPEC"));       
        spectrumNumbers.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                viewModel.updateSpectrumNumbers(convertPrimitiveIntArrayToList(values)); 
            }         
        });
            
        countRate = observableFactory.getSwitchableObservable(new DoubleArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:CNTRATE"));       
        countRate.addObserver(new SpectrumObserver<double[]>() {
            @Override
            public void onNonNullValue(double[] values) {
                viewModel.updateCountRates(convertPrimitiveDoubleArrayToList(values)); 
            }         
        });
        
        integral = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:SUM"));       
        integral.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                viewModel.updateIntegrals(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
        maxSpecBinCount = observableFactory.getSwitchableObservable(new IntArrayChannel(), InstrumentUtils.addPrefix("DAE:DIAG:TABLE:MAX"));       
        maxSpecBinCount.addObserver(new SpectrumObserver<int[]>() {
            @Override
            public void onNonNullValue(int[] values) {
                viewModel.updateMaxSpecBinCount(convertPrimitiveIntArrayToList(values)); 
            }         
        });
        
        numberOfSpectraThatMatchedCriteria = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:MATCH"));      
        numberOfSpectraThatMatchedCriteria.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.updateSpectraCount(value);
            }  
        });
        
        maxFramesObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:FRAMES:SP"));      
        maxFramesObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setMaxFrames(value);
            }  
        });
        
        integralLowLimitObservable = observableFactory.getPVObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTLOW:SP"));      
        integralLowLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                viewModel.setIntegralTimeRangeFrom(value.toString());
            }  
        });
        
        integralHighLimitObservable = observableFactory.getPVObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTHIGH:SP"));      
        integralHighLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                viewModel.setIntegralTimeRangeTo(value.toString());
            }  
        });
        
        numberOfSpectraObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:NUM:SP"));      
        numberOfSpectraObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setNumberOfSpectra(value);
            }  
        });
        
        startingSpectrumNumberObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:START:SP"));      
        startingSpectrumNumberObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setStartingSpectrumNumber(value);
            }  
        });
        
        spectraPeriodsObservable = observableFactory.getPVObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:PERIOD:SP"));      
        spectraPeriodsObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setPeriod(value);
            }  
        });
        
        spectraTypeObservable = observableFactory.getPVObservable(new EnumChannel<SpectraToDisplay>(SpectraToDisplay.class), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:SHOW"));      
        spectraTypeObservable.addObserver(new SpectrumObserver<SpectraToDisplay>() {
            @Override
            public void onNonNullValue(SpectraToDisplay value) {
                viewModel.setSpectraType(value.ordinal());
            }  
        });
        
    }
    
    /**
     * Stop observing.
     */
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

}

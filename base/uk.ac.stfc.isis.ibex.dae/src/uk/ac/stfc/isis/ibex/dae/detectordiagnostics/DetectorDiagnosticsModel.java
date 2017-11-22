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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntArrayChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.IntegerChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Detector diagnostics model, which provides connections 
 * to the PVs holding the detector diagnostics data.
 */
public final class DetectorDiagnosticsModel extends ModelObject {
    
    /** convert seconds to ms */
    private static final int S_TO_MS = 1000;

    /** Time to delay before telling server that we want diagnostic enabled */
    private static final int TIME_TO_REFRESH_ENABLE_DIAGNOSTICS_IN_S = 60;

    private static final Logger LOG = IsisLog.getLogger(DetectorDiagnosticsModel.class);

    private static DetectorDiagnosticsModel instance;
    
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

    private Job detectorDiagnosticsEnabledJob;

    private String writeToEnableDiagnosticError = "";

    protected boolean errorLoggedInJob;

    private Subscription diagnosticsEnabledSubscription;

    /**
     * Constructor.
     * 
     * @param viewModel the view model
     */
    private DetectorDiagnosticsModel() {
        setUpWritables();
        createDetectorDiagnosticsEnabledJob();
    }

    /**
     * Gets the single instance of DetectorDiagnosticsModel.
     *
     * @return single instance of DetectorDiagnosticsModel
     */
    public static DetectorDiagnosticsModel getInstance() {
        if (DetectorDiagnosticsModel.instance == null) {
            DetectorDiagnosticsModel.instance = new DetectorDiagnosticsModel();
        }
        return DetectorDiagnosticsModel.instance;

    }

    private void setUpWritables() {
        
        writableFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        
        diagnosticsEnabled = writableFactory.getSwitchableWritable(new IntegerChannel(),
                InstrumentUtils.addPrefix("DAE:DIAG:ENABLE:FOR"));
        spectraToDisplay = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:SHOW:SP"));
        period = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:PERIOD:SP"));
        startingSpectrumNumber = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:START:SP")); 
        numberOfSpectra = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:NUM:SP"));
        integralTimeRangeTo = writableFactory.getSwitchableWritable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTHIGH:SP"));
        integralTimeRangeFrom = writableFactory.getSwitchableWritable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTLOW:SP"));
        maxFrames = writableFactory.getSwitchableWritable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:FRAMES:SP"));
    }
    
    /**
     * Creates a detector diagnostics job. The job will periodically tell the
     * server that the client is using the detector diagnostics and so they
     * should not be turned off. The job is not started. Once started the job
     * will reschedule itself until it is cancelled.
     */
    private void createDetectorDiagnosticsEnabledJob() {
        detectorDiagnosticsEnabledJob = new Job("Detector Diagnostics Enabled") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    diagnosticsEnabled.write(TIME_TO_REFRESH_ENABLE_DIAGNOSTICS_IN_S * 2);
                    setWriteToEnableDiagnosticError("");
                    errorLoggedInJob = false;
                } catch (IOException e) {
                    setWriteToEnableDiagnosticError("Can not enable detector diagnostics on this instrument.");
                    if (!errorLoggedInJob) {
                        LOG.info("Diagnostics can not be enabled because " + e.getMessage());
                        errorLoggedInJob = true;
                    }
                }
                schedule(TIME_TO_REFRESH_ENABLE_DIAGNOSTICS_IN_S * S_TO_MS);
                return Status.OK_STATUS;
            }
        };
        rescheduleDetectorDiagnosticJobOnCanWriteChange();
    }
    
    /**
     * Sets the spectra to display.
     *
     * @param value the new spectra to display
     */
    public void setSpectraToDisplay(Integer value) {
        try {
            spectraToDisplay.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the period.
     *
     * @param value the new period
     */
    public void setPeriod(Integer value) {
        try {
            period.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the starting spectrum number.
     *
     * @param value the new starting spectrum number
     */
    public void setStartingSpectrumNumber(Integer value) {
        try {
            startingSpectrumNumber.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the number of spectra.
     *
     * @param value the new number of spectra
     */
    public void setNumberOfSpectra(Integer value) {
        try {
            numberOfSpectra.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the integral time range from.
     *
     * @param value the new integral time range from
     */
    public void setIntegralTimeRangeFrom(Double value) {
        try {
            integralTimeRangeFrom.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the integral time range to.
     *
     * @param value the new integral time range to
     */
    public void setIntegralTimeRangeTo(Double value) {
        try {
            integralTimeRangeTo.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Sets the max frames.
     *
     * @param value the new max frames
     */
    public void setMaxFrames(Integer value) {
        try {
            maxFrames.write(value);
        } catch (IOException e) {
            handleWriteException(e);
        }
    }
    
    /**
     * Bind the view model to the model.
     * 
     * @param viewModel
     *            the view model to bind to
     */
    public void bind(final IDetectorDiagnosticsViewModelBinding viewModel) {
        
        observableFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        
        diagnosticsEnabledObservable = observableFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix("DAE:DIAG:ENABLED"));       
        diagnosticsEnabledObservable.addObserver(new SpectrumObserver<Boolean>() {
            @Override
            public void onNonNullValue(Boolean value) {
                viewModel.setDiagnosticsEnabled(value);
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
        
        numberOfSpectraThatMatchedCriteria = observableFactory.getSwitchableObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:MATCH"));      
        numberOfSpectraThatMatchedCriteria.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.updateSpectraCount(value);
            }  
        });
        
        maxFramesObservable = observableFactory.getSwitchableObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:FRAMES:SP"));      
        maxFramesObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setMaxFrames(value);
            }  
        });
        
        integralLowLimitObservable = observableFactory.getSwitchableObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTLOW:SP"));      
        integralLowLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                viewModel.setIntegralTimeRangeFrom(value.toString());
            }  
        });
        
        integralHighLimitObservable = observableFactory.getSwitchableObservable(new DoubleChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:INTHIGH:SP"));      
        integralHighLimitObservable.addObserver(new SpectrumObserver<Double>() {
            @Override
            public void onNonNullValue(Double value) {
                viewModel.setIntegralTimeRangeTo(value.toString());
            }  
        });
        
        numberOfSpectraObservable = observableFactory.getSwitchableObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:NUM:SP"));      
        numberOfSpectraObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setNumberOfSpectra(value);
            }  
        });
        
        startingSpectrumNumberObservable = observableFactory.getSwitchableObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:START:SP"));      
        startingSpectrumNumberObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setStartingSpectrumNumber(value);
            }  
        });
        
        spectraPeriodsObservable = observableFactory.getSwitchableObservable(new IntegerChannel(), InstrumentUtils.addPrefix("DAE:DIAG:PERIOD:SP"));      
        spectraPeriodsObservable.addObserver(new SpectrumObserver<Integer>() {
            @Override
            public void onNonNullValue(Integer value) {
                viewModel.setPeriod(value);
            }  
        });
        
        spectraTypeObservable = observableFactory.getSwitchableObservable(new EnumChannel<SpectraToDisplay>(SpectraToDisplay.class), InstrumentUtils.addPrefix("DAE:DIAG:SPEC:SHOW"));      
        spectraTypeObservable.addObserver(new SpectrumObserver<SpectraToDisplay>() {
            @Override
            public void onNonNullValue(SpectraToDisplay value) {
                viewModel.setSpectraType(value.ordinal());
            }  
        });
        
    }
    
    /**
     * Close the model, use close instance instead
     */
    private void close() {
        setDetectorDiagnosticsEnabled(false);
        diagnosticsEnabledSubscription.removeObserver();
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
    
    /**
     * Logs an exception when a write to a PV failed.
     * 
     * This should not be able to happen as the controls get greyed out when the PVs are unavailable.
     * 
     * @param error - the exception generated by the failed write
     */
    private void handleWriteException(Throwable error) {
        if (error == null) {
            return;
        }
        
        // Only log errors if we should be able to write to the instrument.
        if (diagnosticsEnabled.canWrite()) {
            LOG.error(error.getMessage(), error);
        }
    }

    /**
     * Enable or disable the detector diagnostics.
     * 
     * @param enabled
     *            true to enable the diagnostics; false to disable
     */
    public void setDetectorDiagnosticsEnabled(boolean enabled) {
        if (enabled) {
            if (detectorDiagnosticsEnabledJob.getState() == Job.NONE) {
                detectorDiagnosticsEnabledJob.schedule();
            }
        } else {
            detectorDiagnosticsEnabledJob.cancel();
        }
    }

    /**
     * @return the write to enable diagnostic error; Blank if none
     */
    public String getWriteToEnableDiagnosticError() {
        return writeToEnableDiagnosticError;
    }

    /**
     * Set the error that occurred when trying to write to Enable Diagnostic
     * Error; blank for no error.
     * 
     * @param error
     *            the last error that happened when Enable Diagnostics was
     *            written to; blank for no error
     */
    public void setWriteToEnableDiagnosticError(String error) {
        firePropertyChange("writeToEnableDiagnosticError", this.writeToEnableDiagnosticError,
                this.writeToEnableDiagnosticError = error);
    }

    /**
     * Close the instance of the model.
     */
    public static void closeInstance() {
        if (DetectorDiagnosticsModel.instance != null) {
            DetectorDiagnosticsModel.instance.close();
        }
    }

    /**
     * Create a listener which will immediately reschedule the detector
     * diagnostics job (if it is already scheduled) if the PV becomes writable.
     * This means that the detector diagnostics will be immediately enabled if
     * possible instead of having to wait for the scheduled job to run. This
     * will happen on instrument change.
     * 
     */
    private void rescheduleDetectorDiagnosticJobOnCanWriteChange() {
        diagnosticsEnabledSubscription = diagnosticsEnabled.subscribe(new ConfigurableWriter<Integer, Integer>() {

            @Override
            public void write(Integer value) throws IOException {
                //
            }

            @Override
            public void uncheckedWrite(Integer value) {
                //
            }

            @Override
            public void onError(Exception e) {
                //
            }

            @Override
            public void onCanWriteChanged(boolean canWrite) {
                if (canWrite) {
                    if (detectorDiagnosticsEnabledJob.getState() == Job.WAITING
                            || detectorDiagnosticsEnabledJob.getState() == Job.SLEEPING) {
                        detectorDiagnosticsEnabledJob.cancel();
                        detectorDiagnosticsEnabledJob.schedule();
                    }
                }
            }

            @Override
            public boolean canWrite() {
                return false;
            }

            @Override
            public Subscription writeTo(Writable<Integer> writable) {
                return null;
            }
        });
    }
}

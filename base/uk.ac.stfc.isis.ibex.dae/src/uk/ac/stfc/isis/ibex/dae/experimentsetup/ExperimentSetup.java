
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup;

import java.io.IOException;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.dae.DaeWritables;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettings;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.ObservingDaeSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.ObservingPeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.ObservingTimeChannels;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeChannels;
import uk.ac.stfc.isis.ibex.dae.internal.FileList;
import uk.ac.stfc.isis.ibex.dae.updatesettings.ObservingUpdateSettings;
import uk.ac.stfc.isis.ibex.dae.updatesettings.UpdateSettings;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Class that acts as a model for all the information associated with an
 * experiment.
 */
public class ExperimentSetup extends Closer  {
	
	private final ObservingDaeSettings daeSettings;
	private final ObservingPeriodSettings periodSettings;
	private final ObservingUpdateSettings updateSettings;
	private final ObservingTimeChannels timeChannels;
	
	private final UpdatedObservableAdapter<Collection<String>> detectorTables;
	private final UpdatedObservableAdapter<Collection<String>> spectraTables;
	private final UpdatedObservableAdapter<Collection<String>> wiringTables;
	private final UpdatedObservableAdapter<Collection<String>> periodFiles;
    private final UpdatedObservableAdapter<Collection<String>> timeChannelFiles;
    
    private final ForwardingObservable<String> instrumentName;
    
    private final ForwardingObservable<String> periodFilesDir;
    private final ForwardingObservable<String> timeChannelsDir;
    private final ForwardingObservable<String> wiringTablesDir;
    private final ForwardingObservable<String> detectorTablesDir;
    private final ForwardingObservable<String> spectraTablesDir;

    /**
     * Model of the experiment setup including DAE, period, time channel and
     * update settings.
     * 
     * @param observables observables for experiment setting PVs
     * @param writables writables for experiment setting PVs
     */
	public ExperimentSetup(DaeObservables observables, DaeWritables writables) {
		daeSettings = registerForClose(new ObservingDaeSettings(observables.daeSettings, writables.daeSettings));
		periodSettings = registerForClose(new ObservingPeriodSettings(observables.hardwarePeriods, writables.hardwarePeriods));
		updateSettings = registerForClose(new ObservingUpdateSettings(observables.updateSettings, writables.updateSettings));
		timeChannels = registerForClose(new ObservingTimeChannels(observables.timeChannelSettings, writables.timeChannelSettings));

        periodFilesDir = observables.periodFilesDir;
        timeChannelsDir = observables.timeChannelsDir;
        wiringTablesDir = observables.wiringTablesDir;
        detectorTablesDir = observables.detectorTablesDir;
        spectraTablesDir = observables.spectraTablesDir;
		
        instrumentName = observables.instrumentName;

		detectorTables = createAdapter(observables.detectorTables);
		spectraTables = createAdapter(observables.spectraTables);
		wiringTables = createAdapter(observables.wiringTables);
		periodFiles = createAdapter(observables.periodFiles);
        timeChannelFiles = createAdapter(observables.timeChannelFiles);
	}
	
    /**
     * Returns model of DAE settings.
     * 
     * @return the model
     */
	public DaeSettings daeSettings() {
		return daeSettings;
	}
	
    /**
     * Returns observable list of wiring tables available to the instrument.
     * 
     * @return the list of wiring tables
     */
	public UpdatedValue<Collection<String>> wiringList() {
		return wiringTables;
	}
	
    /**
     * Returns observable list of detector tables available to the instrument.
     * 
     * @return the list of detector tables
     */
	public UpdatedValue<Collection<String>> detectorTables() {
		return detectorTables;
	}
	
    /**
     * Returns observable list of spectra tables available to the instrument.
     * 
     * @return the list of spectra tables
     */
	public UpdatedValue<Collection<String>> spectraTables() {
		return spectraTables;
	}
	
    /**
     * Returns observable list of period files available to the instrument.
     * 
     * @return the list of period files
     */
	public UpdatedValue<Collection<String>> periodFiles() {
		return periodFiles;
	}
	
    /**
     * Returns observable list of time channel files available to the
     * instrument.
     * 
     * @return the list of time channel files
     */
    public UpdatedValue<Collection<String>> timeChannelFiles() {
        return timeChannelFiles;
    }

    /**
     * Returns observable object of the update settings.
     * 
     * @return the update settings
     */
	public UpdateSettings updateSettings() {
		return updateSettings;
	}

    /**
     * Returns observable object of the time channel model.
     * 
     * @return the timechannels
     */
	public TimeChannels timeChannels() {
		return timeChannels;
	}
	
    /**
     * Returns observable object of the period settings.
     * 
     * @return the period settings
     */
	public PeriodSettings periodSettings() {
		return periodSettings;
	}
	
    /**
     * Sends all changes made to the experiment setup in the GUI to the server.
     * 
     * @throws IOException if the send failed
     */
	public synchronized void sendAllSettings() throws IOException {
		IsisLog.getLogger(this.getClass()).info("Sending new DAE settings...");
		daeSettings.sendUpdate();
		periodSettings.sendUpdate();
		updateSettings.sendUpdate();
		timeChannels.sendUpdate();
		IsisLog.getLogger(this.getClass()).info("Finished sending new DAE settings.");
	}
	
	private UpdatedObservableAdapter<Collection<String>> createAdapter(
			ForwardingObservable<String> tables) {
		FileList files = registerForClose(new FileList(tables));
		return registerForClose(new UpdatedObservableAdapter<>(new ForwardingObservable<>(files)));
	}

    /**
     * Returns an Observable for the name of the current instrument.
     * 
     * @return The instrument name.
     */
    public ForwardingObservable<String> getInstrumentName() {
        return instrumentName;
    }

    /**
     * Returns an Observable for the directory containing the period files.
     * 
     * @return The directory.
     */
    public ForwardingObservable<String> getPeriodFilesDir() {
        return periodFilesDir;
    }

    /**
     * Returns an Observable for the directory containing the time channel files.
     * 
     * @return The directory.
     */
    public ForwardingObservable<String> getTimeChannelsDir() {
        return timeChannelsDir;
    }

    /**
     * Returns an Observable for the directory containing the wiring tables.
     * 
     * @return The directory.
     */
    public ForwardingObservable<String> getWiringTablesDir() {
        return wiringTablesDir;
    }

    /**
     * Returns an Observable for the directory containing the detector tables.
     * 
     * @return The directory.
     */
    public ForwardingObservable<String> getDetectorTablesDir() {
        return detectorTablesDir;
    }

    /**
     * Returns an Observable for the directory containing the spectra tables.
     * 
     * @return The directory.
     */
    public ForwardingObservable<String> getSpectraTablesDir() {
        return spectraTablesDir;
    }
}


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
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ExperimentSetup extends Closer  {
	
	private final ObservingDaeSettings daeSettings;
	private final ObservingPeriodSettings periodSettings;
	private final ObservingUpdateSettings updateSettings;
	private final ObservingTimeChannels timeChannels;
	
	private final UpdatedObservableAdapter<Collection<String>> detectorTables;
	private final UpdatedObservableAdapter<Collection<String>> spectraTables;
	private final UpdatedObservableAdapter<Collection<String>> wiringTables;
	private final UpdatedObservableAdapter<Collection<String>> periodFiles;
	
	public ExperimentSetup(DaeObservables observables, DaeWritables writables) {
		daeSettings = registerForClose(new ObservingDaeSettings(observables.daeSettings, writables.daeSettings));
		periodSettings = registerForClose(new ObservingPeriodSettings(observables.hardwarePeriods, writables.hardwarePeriods));
		updateSettings = registerForClose(new ObservingUpdateSettings(observables.updateSettings, writables.updateSettings));
		timeChannels = registerForClose(new ObservingTimeChannels(observables.timeChannelSettings, writables.timeChannelSettings));
		
		detectorTables = createAdapter(observables.detectorTables);
		spectraTables = createAdapter(observables.spectraTables);
		wiringTables = createAdapter(observables.wiringTables);
		periodFiles = createAdapter(observables.periodFiles);
	}
	
	public DaeSettings daeSettings() {
		return daeSettings;
	}
	
	public UpdatedValue<Collection<String>> wiringList() {
		return wiringTables;
	}
	
	public UpdatedValue<Collection<String>> detectorTables() {
		return detectorTables;
	}
	
	public UpdatedValue<Collection<String>> spectraTables() {
		return spectraTables;
	}
	
	public UpdatedValue<Collection<String>> periodFiles() {
		return periodFiles;
	}
	
	public UpdateSettings updateSettings() {
		return updateSettings;
	}
	
	public TimeChannels timeChannels() {
		return timeChannels;
	}
	
	public PeriodSettings periodSettings() {
		return periodSettings;
	}
	
	public void sendAllSettings() {
		daeSettings.sendUpdate();
		periodSettings.sendUpdate();
		updateSettings.sendUpdate();
		timeChannels.sendUpdate();
	}
	
	private UpdatedObservableAdapter<Collection<String>> createAdapter(
			ForwardingObservable<String> tables) {
		FileList files = registerForClose(new FileList(tables));
		return registerForClose(new UpdatedObservableAdapter<>(new ForwardingObservable<>(files)));
	}
}

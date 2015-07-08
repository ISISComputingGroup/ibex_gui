
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

package uk.ac.stfc.isis.ibex.ui.dae.runinformation;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.run.InstrumentState;

public class RunInformationViewModel extends Closer {
	
	public UpdatedValue<String> instrument;
	public UpdatedValue<String> runStatus;
	public UpdatedValue<String> runNumber;
	public UpdatedValue<String> isisCycle;
	public UpdatedObservableAdapter<Number> beamCurrent;
	public UpdatedObservableAdapter<Integer> goodFrames;
	public UpdatedObservableAdapter<Double> totalCounts;
	public UpdatedObservableAdapter<Integer> memoryUsed;
	public UpdatedObservableAdapter<String> timingSource;
	
	public UpdatedObservableAdapter<String> rbNumber;
	public UpdatedObservableAdapter<Integer> rawFrames;
	public UpdatedObservableAdapter<Number> totalCurrent;
	public UpdatedObservableAdapter<Double> countRate;
	public UpdatedObservableAdapter<Double> eventMode;
	
	public UpdatedObservableAdapter<String> startTime;
	public UpdatedObservableAdapter<Integer> runDuration;
	public UpdatedObservableAdapter<Integer> timeChannels;
	public UpdatedObservableAdapter<Integer> spectra;
	public UpdatedObservableAdapter<String> title;
	public UpdatedObservableAdapter<String> users;
	
	public UpdatedObservableAdapter<Integer> period;
	public UpdatedObservableAdapter<Integer> periodGoodFrames;
	public UpdatedObservableAdapter<Integer> periodDuration;
	public UpdatedObservableAdapter<Integer> totalPeriods;
	public UpdatedObservableAdapter<Integer> periodRawFrames;
	public UpdatedObservableAdapter<String> periodType;
	public UpdatedObservableAdapter<Integer> periodSequence;
	
	public UpdatedObservableAdapter<Integer> monitorSpectrum;
	public UpdatedObservableAdapter<Double> monitorFrom;
	public UpdatedObservableAdapter<Integer> monitorCounts;
	public UpdatedObservableAdapter<Double> monitorTo;
	public UpdatedObservableAdapter<Double> npRatio;

	public RunInformationViewModel(DaeObservables observables) {
		instrument = adapt(observables.instrumentName);
		beamCurrent = adapt(observables.beamCurrent);
		goodFrames = adapt(observables.goodFrames);
		totalCounts = adapt(observables.totalDaeCounts);
		memoryUsed = adapt(observables.daeMemoryUsed);
		timingSource = adapt(observables.timingSource);
		
		runStatus = new TextUpdatedObservableAdapter(registerForClose(new InstrumentState(observables.runState)));
		rbNumber = adapt(observables.rbNumber);
		rawFrames = adapt(observables.rawFrames);
		totalCurrent = adapt(observables.beamCurrent);
		countRate = adapt(observables.countRate);
		eventMode = adapt(observables.eventMode);
		
		runNumber = adapt(observables.runNumber);
		startTime = adapt(observables.startTime);
		runDuration = adapt(observables.runDuration);
		timeChannels = adapt(observables.timeChannels);
		spectra = adapt(observables.spectra);		
		isisCycle = adapt(observables.isisCycle);
		
		title = adapt(observables.title);
		users = adapt(observables.users);
		
		period = adapt(observables.currentPeriod);
		periodGoodFrames = adapt(observables.periodGoodFrames);
		periodDuration = adapt(observables.periodDuration);
		totalPeriods = adapt(observables.totalPeriods);
		periodRawFrames = adapt(observables.periodRawFrames);
		periodType = adapt(observables.periodType);
		periodSequence = adapt(observables.periodSequence);
		
		monitorSpectrum = adapt(observables.monitorSpectrum);
		monitorFrom = adapt(observables.monitorFrom);
		monitorCounts = adapt(observables.monitorCounts);
		monitorTo = adapt(observables.monitorTo);
		npRatio = adapt(observables.npRatio);
	}

	private <T> UpdatedObservableAdapter<T> adapt(InitialiseOnSubscribeObservable<T> observable) {
		return registerForClose(new UpdatedObservableAdapter<>(observable));
	}
}

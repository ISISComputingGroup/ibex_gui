
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
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.run.InstrumentState;

/**
 * A view model holding the logic for updating the display of run information in
 * the DAE.
 */
public class RunInformationViewModel extends Closer {
	
    /**
     * An updating string with the name of the instrument.
     */
	public UpdatedValue<String> instrument;

    /**
     * An updating string with the status of the run.
     */
	public UpdatedValue<String> runStatus;

    /**
     * An updating string with the run number.
     */
	public UpdatedValue<String> runNumber;

    /**
     * An updating string with the cycle number.
     */
	public UpdatedValue<String> isisCycle;

    /**
     * An updating number giving the current beam current.
     */
	public UpdatedObservableAdapter<Number> beamCurrent;

    /**
     * An updating integer giving the number of good frames for this run.
     */
	public UpdatedObservableAdapter<Integer> goodFrames;

    /**
     * An updating double giving the number of counts for this run.
     */
	public UpdatedObservableAdapter<Double> totalCounts;

    /**
     * An updating integer giving the memory used on the DAE.
     */
	public UpdatedObservableAdapter<Integer> memoryUsed;

    /**
     * An updating string giving the timing source being used by the DAE.
     */
	public UpdatedObservableAdapter<String> timingSource;
	
    /**
     * An updating string with the current RB number.
     */
	public UpdatedObservableAdapter<String> rbNumber;

    /**
     * An updating integer giving the number of raw frames for this run.
     */
	public UpdatedObservableAdapter<Integer> rawFrames;

    /**
     * An updating number giving the total current for this run.
     */
	public UpdatedObservableAdapter<Number> totalCurrent;

    /**
     * An updating double giving the count rate of the DAE.
     */
	public UpdatedObservableAdapter<Double> countRate;

	public UpdatedObservableAdapter<Double> eventMode;
	
    /**
     * An updating string with the start time of the current run.
     */
	public UpdatedObservableAdapter<String> startTime;

    /**
     * An updating integer giving the duration of the current run in seconds.
     */
	public UpdatedObservableAdapter<Integer> runDuration;

	public UpdatedObservableAdapter<Integer> timeChannels;
	public UpdatedObservableAdapter<Integer> spectra;

    /**
     * An updating string giving the title for the current run.
     */
	public UpdatedObservableAdapter<String> title;

    /**
     * An updating boolean giving whether or not to display the title on the
     * webpage.
     */
    public UpdatedObservableAdapter<Boolean> displayTitle;

    /**
     * An updating string giving the users of the instrument for the current
     * run.
     */
	public UpdatedObservableAdapter<String> users;
	

	public UpdatedObservableAdapter<Integer> period;
	public UpdatedObservableAdapter<Integer> periodGoodFrames;

    /**
     * An updating integer giving the duration of the current period in seconds.
     */
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

    /**
     * The constructor that binds updating values to observables.
     * 
     * @param observables
     *            A class containing observables bound to underling PVs
     */
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
        displayTitle = adapt(observables.displayTitle);
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

	private <T> UpdatedObservableAdapter<T> adapt(ForwardingObservable<T> observable) {
		return registerForClose(new UpdatedObservableAdapter<>(observable));
	}
}

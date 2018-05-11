
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
     * The name of the instrument.
     */
	public UpdatedValue<String> instrument;

    /**
     * The status of the run.
     */
	public UpdatedValue<String> runStatus;

    /**
     * The run number.
     */
	public UpdatedValue<String> runNumber;

    /**
     * The cycle number.
     */
	public UpdatedValue<String> isisCycle;

    /**
     * The current beam current.
     */
	public UpdatedObservableAdapter<Number> beamCurrent;

    /**
     * The number of good frames for this run.
     */
	public UpdatedObservableAdapter<Integer> goodFrames;

    /**
     * The number of counts for this run.
     */
	public UpdatedObservableAdapter<Double> totalCounts;

    /**
     * The memory used on the DAE.
     */
	public UpdatedObservableAdapter<Integer> memoryUsed;

    /**
     * The timing source being used by the DAE.
     */
	public UpdatedObservableAdapter<String> timingSource;
	
    /**
     * The current RB number.
     */
	public UpdatedObservableAdapter<String> rbNumber;

    /**
     * The number of raw frames for this run.
     */
	public UpdatedObservableAdapter<Integer> rawFrames;

    /**
     * The total current for this run.
     */
	public UpdatedObservableAdapter<Number> totalCurrent;

    /**
     * The count rate of the DAE.
     */
	public UpdatedObservableAdapter<Double> countRate;

    /**
     * The event mode value.
     */
	public UpdatedObservableAdapter<Double> eventMode;
	
    /**
     * The start time of the current run.
     */
	public UpdatedObservableAdapter<String> startTime;

    /**
     * The duration of the current run in seconds.
     */
	public UpdatedObservableAdapter<Integer> runDuration;

    /**
     * The number of time channels.
     */
	public UpdatedObservableAdapter<Integer> timeChannels;

    /**
     * The number of spectra.
     */
	public UpdatedObservableAdapter<Integer> spectra;

    /**
     * The title for the current run.
     */
	public UpdatedObservableAdapter<String> title;

    /**
     * Whether or not to display the title on the webpage.
     */
    public UpdatedObservableAdapter<Boolean> displayTitle;

    /**
     * The users of the instrument for the current run.
     */
	public UpdatedObservableAdapter<String> users;
	
    /**
     * The current period.
     */
	public UpdatedObservableAdapter<Integer> period;

    /**
     * The number of good frames for the current period.
     */
	public UpdatedObservableAdapter<Integer> periodGoodFrames;

    /**
     * The number of raw frames for the current period.
     */
    public UpdatedObservableAdapter<Integer> periodRawFrames;

    /**
     * The duration of the current period in seconds.
     */
	public UpdatedObservableAdapter<Integer> periodDuration;

    /**
     * The total number of periods.
     */
	public UpdatedObservableAdapter<Integer> totalPeriods;

    /**
     * The period type.
     */
	public UpdatedObservableAdapter<String> periodType;

    /**
     * The period sequence.
     */
	public UpdatedObservableAdapter<Integer> periodSequence;
	
    /**
     * The detector number for the monitor spectrum.
     */
	public UpdatedObservableAdapter<Integer> monitorSpectrum;

    /**
     * The lower time limit for the monitor.
     */
	public UpdatedObservableAdapter<Double> monitorFrom;

    /**
     * The upper time limit for the monitor.
     */
    public UpdatedObservableAdapter<Double> monitorTo;

    /**
     * The number of monitor counts.
     */
	public UpdatedObservableAdapter<Integer> monitorCounts;

    /**
     * The neutron/proton ratio.
     */
	public UpdatedObservableAdapter<Double> npRatio;
	
	/**
     * The simulation mode status.
     */
	public UpdatedObservableAdapter<Boolean> simMode;

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
		simMode = adapt(observables.simulationMode);
		
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

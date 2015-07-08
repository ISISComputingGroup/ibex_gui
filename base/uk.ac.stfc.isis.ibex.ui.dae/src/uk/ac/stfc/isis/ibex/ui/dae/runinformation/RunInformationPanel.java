
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;

public class RunInformationPanel extends Composite {

	private Label instrument;
	private Label runStatus;
	private Label runNumber;
	private Label beamCurrent;
	private Label rbNumber;
	private Label startTime;
	private Label goodFrames;
	private Label rawFrames;
	private Label runDuration;
	private Label counts;
	private Label totalCurrent;
	private Label timeChannels;
	private Label memoryUsed;
	private Label countRate;
	private Label spectra;
	private Label timing;
	private Label eventMode;
	private Label isisCycle;
	
	private Label title;
	private Label users;
	
	private Label period;
	private Label numberOfPeriods;
	private Label periodType;
	private Label periodGoodFrames;
	private Label periodRawFrames;
	private Label periodSequence;
	private Label periodDuration;
	
	private Label monitorSpectrum;
	private Label monitorCounts;
	private Label npRatio;
	private Label monitorFrom;
	private Label monitorTo;

	private DataBindingContext bindingContext;

	public RunInformationPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group grpSetup = new Group(this, SWT.NONE);
		grpSetup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSetup.setText("Setup");
		GridLayout gl_grpSetup = new GridLayout(6, false);
		gl_grpSetup.horizontalSpacing = 20;
		grpSetup.setLayout(gl_grpSetup);
		
		Label lblInstrument = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblInstrument = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_lblInstrument.widthHint = 110;
		lblInstrument.setLayoutData(gd_lblInstrument);
		lblInstrument.setText("Instrument:");
		
		instrument = new Label(grpSetup, SWT.NONE);
		GridData gd_instrument = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_instrument.widthHint = 65;
		instrument.setLayoutData(gd_instrument);
		instrument.setText("UNKNOWN");
		
		Label lblRunstatus = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblRunstatus = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblRunstatus.widthHint = 110;
		lblRunstatus.setLayoutData(gd_lblRunstatus);
		lblRunstatus.setText("Run Status:");
		
		runStatus = new Label(grpSetup, SWT.NONE);
		GridData gd_runStatus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_runStatus.widthHint = 65;
		runStatus.setLayoutData(gd_runStatus);
		runStatus.setText("UNKNOWN");
		
		
		Label lblRunNumber = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblRunNumber = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblRunNumber.widthHint = 110;
		lblRunNumber.setLayoutData(gd_lblRunNumber);
		lblRunNumber.setText("Run Number:");
		
		runNumber = new Label(grpSetup, SWT.NONE);
		runNumber.setText("UNKNOWN");
		GridData gd_runNumber = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_runNumber.widthHint = 65;
		runNumber.setLayoutData(gd_runNumber);
		
		Label lblBeamCurrent = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblBeamCurrent = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblBeamCurrent.widthHint = 110;
		lblBeamCurrent.setLayoutData(gd_lblBeamCurrent);
		lblBeamCurrent.setText("Beam Current:");
		
		beamCurrent = new Label(grpSetup, SWT.NONE);
		GridData gd_beamCurrent = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_beamCurrent.widthHint = 65;
		beamCurrent.setLayoutData(gd_beamCurrent);
		beamCurrent.setText("UNKNOWN");
		
		Label lblRbNumber = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblRbNumber = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblRbNumber.widthHint = 110;
		lblRbNumber.setLayoutData(gd_lblRbNumber);
		lblRbNumber.setText("RB Number:");
		
		rbNumber = new Label(grpSetup, SWT.NONE);
		GridData gd_rbNumber = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_rbNumber.widthHint = 65;
		rbNumber.setLayoutData(gd_rbNumber);
		rbNumber.setText("UNKNOWN");
		
		Label lblStartTime = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblStartTime = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblStartTime.widthHint = 110;
		lblStartTime.setLayoutData(gd_lblStartTime);
		lblStartTime.setText("Start Time:");
		
		startTime = new Label(grpSetup, SWT.NONE);
		GridData gd_startTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_startTime.widthHint = 150;
		startTime.setLayoutData(gd_startTime);
		startTime.setText("UNKNOWN");
		
		Label lblGoodFrames = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblGoodFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblGoodFrames.widthHint = 110;
		lblGoodFrames.setLayoutData(gd_lblGoodFrames);
		lblGoodFrames.setText("Good Frames:");
		
		goodFrames = new Label(grpSetup, SWT.NONE);
		GridData gd_goodFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_goodFrames.widthHint = 65;
		goodFrames.setLayoutData(gd_goodFrames);
		goodFrames.setText("UNKNOWN");
		
		Label lblRawFrames = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblRawFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblRawFrames.widthHint = 110;
		lblRawFrames.setLayoutData(gd_lblRawFrames);
		lblRawFrames.setText("Raw Frames:");
		
		rawFrames = new Label(grpSetup, SWT.NONE);
		GridData gd_rawFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_rawFrames.widthHint = 65;
		rawFrames.setLayoutData(gd_rawFrames);
		rawFrames.setText("UNKNOWN");
		
		Label lblRunDuration = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblRunDuration = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblRunDuration.widthHint = 110;
		lblRunDuration.setLayoutData(gd_lblRunDuration);
		lblRunDuration.setText("Run Duration:");
		
		runDuration = new Label(grpSetup, SWT.NONE);
		GridData gd_runDuration = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_runDuration.widthHint = 65;
		runDuration.setLayoutData(gd_runDuration);
		runDuration.setText("UNKNOWN");
		
		Label lblCountsmev = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblCountsmev = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCountsmev.widthHint = 110;
		lblCountsmev.setLayoutData(gd_lblCountsmev);
		lblCountsmev.setText("Counts (MEV):");
		
		counts = new Label(grpSetup, SWT.NONE);
		GridData gd_counts = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_counts.widthHint = 65;
		counts.setLayoutData(gd_counts);
		counts.setText("UNKNOWN");
		
		Label lblTotalah = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblTotalah = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotalah.widthHint = 110;
		lblTotalah.setLayoutData(gd_lblTotalah);
		lblTotalah.setText("Total μAh:");
		
		totalCurrent = new Label(grpSetup, SWT.NONE);
		GridData gd_totalCurrent = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_totalCurrent.widthHint = 65;
		totalCurrent.setLayoutData(gd_totalCurrent);
		totalCurrent.setText("UNKNOWN");
		
		Label lblTimeChannels = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblTimeChannels = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTimeChannels.widthHint = 110;
		lblTimeChannels.setLayoutData(gd_lblTimeChannels);
		lblTimeChannels.setText("Time Channels:");
		
		timeChannels = new Label(grpSetup, SWT.NONE);
		GridData gd_timeChannels = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_timeChannels.widthHint = 65;
		timeChannels.setLayoutData(gd_timeChannels);
		timeChannels.setText("UNKNOWN");
		
		Label lblDaeMemoryUsed = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblDaeMemoryUsed = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDaeMemoryUsed.widthHint = 110;
		lblDaeMemoryUsed.setLayoutData(gd_lblDaeMemoryUsed);
		lblDaeMemoryUsed.setText("DAE Memory Used:");
		
		memoryUsed = new Label(grpSetup, SWT.NONE);
		GridData gd_memoryUsed = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_memoryUsed.widthHint = 65;
		memoryUsed.setLayoutData(gd_memoryUsed);
		memoryUsed.setText("UNKNOWN");
		
		Label lblCountRatemevh = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblCountRatemevh = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCountRatemevh.widthHint = 110;
		lblCountRatemevh.setLayoutData(gd_lblCountRatemevh);
		lblCountRatemevh.setText("Count Rate (MEV/h):");
		
		countRate = new Label(grpSetup, SWT.NONE);
		GridData gd_countRate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_countRate.widthHint = 65;
		countRate.setLayoutData(gd_countRate);
		countRate.setText("UNKNOWN");
		
		Label lblSpectra = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblSpectra = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblSpectra.widthHint = 110;
		lblSpectra.setLayoutData(gd_lblSpectra);
		lblSpectra.setText("Spectra:");
		
		spectra = new Label(grpSetup, SWT.NONE);
		GridData gd_spectra = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_spectra.widthHint = 65;
		spectra.setLayoutData(gd_spectra);
		spectra.setText("UNKNOWN");
		
		Label lblDaeTiming = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblDaeTiming = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDaeTiming.widthHint = 110;
		lblDaeTiming.setLayoutData(gd_lblDaeTiming);
		lblDaeTiming.setText("DAE Timing:");
		
		timing = new Label(grpSetup, SWT.NONE);
		GridData gd_timing = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_timing.widthHint = 65;
		timing.setLayoutData(gd_timing);
		timing.setText("UNKNOWN");
		
		Label lblEventMode = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblEventMode = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblEventMode.widthHint = 110;
		lblEventMode.setLayoutData(gd_lblEventMode);
		lblEventMode.setText("Event Mode:");
		
		eventMode = new Label(grpSetup, SWT.NONE);
		GridData gd_eventMode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_eventMode.widthHint = 65;
		eventMode.setLayoutData(gd_eventMode);
		eventMode.setText("UNKNOWN");
		
		Label lblIsisCycle = new Label(grpSetup, SWT.RIGHT);
		GridData gd_lblIsisCycle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblIsisCycle.widthHint = 110;
		lblIsisCycle.setLayoutData(gd_lblIsisCycle);
		lblIsisCycle.setText("ISIS Cycle:");
		
		isisCycle = new Label(grpSetup, SWT.NONE);
		GridData gd_isisCycle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_isisCycle.widthHint = 65;
		isisCycle.setLayoutData(gd_isisCycle);
		isisCycle.setText("UNKNOWN");
		
		Group titleGroup = new Group(this, SWT.NONE);
		titleGroup.setLayout(new GridLayout(2, false));
		titleGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTitle = new Label(titleGroup, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText("Title:");
		
		title = new Label(titleGroup, SWT.NONE);
		GridData gd_title = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_title.widthHint = 500;
		title.setLayoutData(gd_title);
		title.setText("UNKNOWN");
		
		Label lblUsers = new Label(titleGroup, SWT.NONE);
		lblUsers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsers.setText("User(s):");
		
		users = new Label(titleGroup, SWT.NONE);
		GridData gd_users = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_users.widthHint = 500;
		users.setLayoutData(gd_users);
		users.setText("UNKNOWN");
		
		Group grpPeriods = new Group(this, SWT.NONE);
		GridLayout gl_grpPeriods = new GridLayout(6, false);
		gl_grpPeriods.horizontalSpacing = 20;
		grpPeriods.setLayout(gl_grpPeriods);
		grpPeriods.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpPeriods.setText("Periods");
		
		Label lblPeriod = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblPeriod = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPeriod.widthHint = 110;
		lblPeriod.setLayoutData(gd_lblPeriod);
		lblPeriod.setText("Period:");
		
		period = new Label(grpPeriods, SWT.NONE);
		GridData gd_period = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_period.widthHint = 65;
		period.setLayoutData(gd_period);
		period.setText("UNKNOWN");
		
		Label lblNumberOfPeriods = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblNumberOfPeriods = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNumberOfPeriods.widthHint = 110;
		lblNumberOfPeriods.setLayoutData(gd_lblNumberOfPeriods);
		lblNumberOfPeriods.setText("Number of Periods:");
		
		numberOfPeriods = new Label(grpPeriods, SWT.NONE);
		GridData gd_numberOfPeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_numberOfPeriods.widthHint = 65;
		numberOfPeriods.setLayoutData(gd_numberOfPeriods);
		numberOfPeriods.setText("UNKNOWN");
		
		Label lblPeriodType = new Label(grpPeriods, SWT.RIGHT);
		lblPeriodType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodType.setText("Period Type:");
		
		periodType = new Label(grpPeriods, SWT.NONE);
		GridData gd_periodType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_periodType.widthHint = 65;
		periodType.setLayoutData(gd_periodType);
		periodType.setText("UNKNOWN");
		
		Label lblPeriodGoodFrames = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblPeriodGoodFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPeriodGoodFrames.widthHint = 110;
		lblPeriodGoodFrames.setLayoutData(gd_lblPeriodGoodFrames);
		lblPeriodGoodFrames.setText("Period Good Frames:");
		
		periodGoodFrames = new Label(grpPeriods, SWT.NONE);
		GridData gd_periodGoodFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_periodGoodFrames.widthHint = 65;
		periodGoodFrames.setLayoutData(gd_periodGoodFrames);
		periodGoodFrames.setText("UNKNOWN");
		
		Label lblPeriodRawFrames = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblPeriodRawFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPeriodRawFrames.widthHint = 110;
		lblPeriodRawFrames.setLayoutData(gd_lblPeriodRawFrames);
		lblPeriodRawFrames.setText("Period Raw Frames:");
		
		periodRawFrames = new Label(grpPeriods, SWT.NONE);
		GridData gd_periodRawFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_periodRawFrames.widthHint = 65;
		periodRawFrames.setLayoutData(gd_periodRawFrames);
		periodRawFrames.setText("UNKNOWN");
		
		Label lblPeriodSequence = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblPeriodSequence = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPeriodSequence.widthHint = 110;
		lblPeriodSequence.setLayoutData(gd_lblPeriodSequence);
		lblPeriodSequence.setText("Period Sequence:");
		
		periodSequence = new Label(grpPeriods, SWT.NONE);
		GridData gd_periodSequence = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_periodSequence.widthHint = 65;
		periodSequence.setLayoutData(gd_periodSequence);
		periodSequence.setText("UNKNOWN");
		
		Label lblPeriodDuration = new Label(grpPeriods, SWT.RIGHT);
		GridData gd_lblPeriodDuration = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPeriodDuration.widthHint = 110;
		lblPeriodDuration.setLayoutData(gd_lblPeriodDuration);
		lblPeriodDuration.setText("Period Duration:");
		
		periodDuration = new Label(grpPeriods, SWT.NONE);
		GridData gd_periodDuration = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_periodDuration.widthHint = 65;
		periodDuration.setLayoutData(gd_periodDuration);
		periodDuration.setText("UNKNOWN");
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		
		Group grpMonitor = new Group(this, SWT.NONE);
		GridLayout gl_grpMonitor = new GridLayout(6, false);
		gl_grpMonitor.horizontalSpacing = 20;
		grpMonitor.setLayout(gl_grpMonitor);
		grpMonitor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMonitor.setText("Monitor");
		
		Label lblMonitorSpectrum = new Label(grpMonitor, SWT.RIGHT);
		GridData gd_lblMonitorSpectrum = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblMonitorSpectrum.widthHint = 110;
		lblMonitorSpectrum.setLayoutData(gd_lblMonitorSpectrum);
		lblMonitorSpectrum.setText("Monitor Spectrum:");
		
		monitorSpectrum = new Label(grpMonitor, SWT.NONE);
		GridData gd_monitorSpectrum = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_monitorSpectrum.widthHint = 65;
		monitorSpectrum.setLayoutData(gd_monitorSpectrum);
		monitorSpectrum.setText("UNKNOWN");
		
		Label lblMonitorCounts = new Label(grpMonitor, SWT.RIGHT);
		GridData gd_lblMonitorCounts = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblMonitorCounts.widthHint = 110;
		lblMonitorCounts.setLayoutData(gd_lblMonitorCounts);
		lblMonitorCounts.setText("Monitor Counts:");
		
		monitorCounts = new Label(grpMonitor, SWT.NONE);
		GridData gd_monitorCounts = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_monitorCounts.widthHint = 65;
		monitorCounts.setLayoutData(gd_monitorCounts);
		monitorCounts.setText("UNKNOWN");
		
		Label lblNpRatio = new Label(grpMonitor, SWT.RIGHT);
		GridData gd_lblNpRatio = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNpRatio.widthHint = 110;
		lblNpRatio.setLayoutData(gd_lblNpRatio);
		lblNpRatio.setText("N/P Ratio:");
		
		npRatio = new Label(grpMonitor, SWT.NONE);
		GridData gd_npRatio = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_npRatio.widthHint = 65;
		npRatio.setLayoutData(gd_npRatio);
		npRatio.setText("UNKNOWN");
		
		Label lblFrom = new Label(grpMonitor, SWT.RIGHT);
		GridData gd_lblFrom = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblFrom.widthHint = 110;
		lblFrom.setLayoutData(gd_lblFrom);
		lblFrom.setText("From:");
		
		monitorFrom = new Label(grpMonitor, SWT.NONE);
		GridData gd_monitorFrom = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_monitorFrom.widthHint = 65;
		monitorFrom.setLayoutData(gd_monitorFrom);
		monitorFrom.setText("UNKNOWN");
		
		Label lblTo = new Label(grpMonitor, SWT.RIGHT);
		GridData gd_lblTo = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTo.widthHint = 110;
		lblTo.setLayoutData(gd_lblTo);
		lblTo.setText("To:");
		
		monitorTo = new Label(grpMonitor, SWT.NONE);
		GridData gd_monitorTo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_monitorTo.widthHint = 65;
		monitorTo.setLayoutData(gd_monitorTo);
		monitorTo.setText("UNKNOWN");
		new Label(grpMonitor, SWT.NONE);
		new Label(grpMonitor, SWT.NONE);
	}
	
	public void setModel(DaeViewModel viewModel) {
		bind(viewModel.runInformation());
	}
	
	private void bind(RunInformationViewModel viewModel) {
		bindingContext = new DataBindingContext();
		bindLabel(instrument, viewModel.instrument);
		bindLabel(runNumber, viewModel.runNumber);
		bindLabel(beamCurrent, viewModel.beamCurrent);
		bindLabel(runStatus, viewModel.runStatus);
		bindLabel(rbNumber, viewModel.rbNumber);
		bindLabel(startTime, viewModel.startTime);
		bindLabel(goodFrames, viewModel.goodFrames);
		bindLabel(rawFrames, viewModel.rawFrames);
		bindLabel(runDuration, viewModel.runDuration);
		bindLabel(counts, viewModel.totalCounts);
		bindLabel(totalCurrent, viewModel.totalCurrent);
		bindLabel(timeChannels, viewModel.timeChannels);
		bindLabel(memoryUsed, viewModel.memoryUsed);
		bindLabel(countRate, viewModel.countRate);
		bindLabel(spectra, viewModel.spectra);
		bindLabel(timing, viewModel.timingSource);
		bindLabel(eventMode, viewModel.eventMode);
		bindLabel(isisCycle, viewModel.isisCycle);
		bindLabel(title, viewModel.title);
		bindLabel(users, viewModel.users);
		bindLabel(period, viewModel.period);
		bindLabel(numberOfPeriods, viewModel.totalPeriods);
		bindLabel(periodType, viewModel.periodType);
		bindLabel(periodGoodFrames, viewModel.periodGoodFrames);
		bindLabel(periodRawFrames, viewModel.periodRawFrames);
		bindLabel(periodSequence, viewModel.periodSequence);
		bindLabel(periodDuration, viewModel.periodSequence);
		bindLabel(monitorSpectrum, viewModel.monitorSpectrum);
		bindLabel(monitorCounts, viewModel.monitorCounts);
		bindLabel(npRatio, viewModel.npRatio);
		bindLabel(monitorFrom, viewModel.monitorFrom);
		bindLabel(monitorTo, viewModel.monitorTo);
	}
	
	private void bindLabel(Label label, UpdatedValue<?> value) {
		bindingContext.bindValue(WidgetProperties.text().observe(label), BeanProperties.value("value").observe(value));
	}
}

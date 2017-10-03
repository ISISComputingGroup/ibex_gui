
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

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;

@SuppressWarnings("checkstyle:magicnumber")
public class RunInformationPanel {

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
    private DaeViewModel viewModel;

    public RunInformationPanel() {
        this.viewModel = DaeUI.getDefault().viewModel();
    }

    @PostConstruct
    public void createPart(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
		
        Group grpSetup = new Group(parent, SWT.NONE);
		grpSetup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSetup.setText("Setup");
		GridLayout glGrpSetup = new GridLayout(6, false);
		glGrpSetup.horizontalSpacing = 20;
		grpSetup.setLayout(glGrpSetup);
		
		Label lblInstrument = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblInstrument = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gdLblInstrument.widthHint = 110;
		lblInstrument.setLayoutData(gdLblInstrument);
		lblInstrument.setText("Instrument:");
		
		instrument = new Label(grpSetup, SWT.NONE);
		GridData gdInstrument = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdInstrument.widthHint = 65;
		instrument.setLayoutData(gdInstrument);
		instrument.setText("UNKNOWN");
		
		Label lblRunstatus = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblRunstatus = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblRunstatus.widthHint = 110;
		lblRunstatus.setLayoutData(gdLblRunstatus);
		lblRunstatus.setText("Run Status:");
		
		runStatus = new Label(grpSetup, SWT.NONE);
		GridData gdRunStatus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdRunStatus.widthHint = 65;
		runStatus.setLayoutData(gdRunStatus);
		runStatus.setText("UNKNOWN");
		
		Label lblRunNumber = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblRunNumber = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblRunNumber.widthHint = 110;
		lblRunNumber.setLayoutData(gdLblRunNumber);
		lblRunNumber.setText("Run Number:");
		
		runNumber = new Label(grpSetup, SWT.NONE);
		runNumber.setText("UNKNOWN");
		GridData gdRunNumber = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdRunNumber.widthHint = 65;
		runNumber.setLayoutData(gdRunNumber);
		
		Label lblBeamCurrent = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblBeamCurrent = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblBeamCurrent.widthHint = 110;
		lblBeamCurrent.setLayoutData(gdLblBeamCurrent);
		lblBeamCurrent.setText("Beam Current:");
		
		beamCurrent = new Label(grpSetup, SWT.NONE);
		GridData gdBeamCurrent = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBeamCurrent.widthHint = 65;
		beamCurrent.setLayoutData(gdBeamCurrent);
		beamCurrent.setText("UNKNOWN");
		
		Label lblRbNumber = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblRbNumber = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblRbNumber.widthHint = 110;
		lblRbNumber.setLayoutData(gdLblRbNumber);
		lblRbNumber.setText("RB Number:");
		
		rbNumber = new Label(grpSetup, SWT.NONE);
		GridData gdRbNumber = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdRbNumber.widthHint = 65;
		rbNumber.setLayoutData(gdRbNumber);
		rbNumber.setText("UNKNOWN");
		
		Label lblStartTime = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblStartTime = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblStartTime.widthHint = 110;
		lblStartTime.setLayoutData(gdLblStartTime);
		lblStartTime.setText("Start Time:");
		
		startTime = new Label(grpSetup, SWT.NONE);
		GridData gdStartTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdStartTime.widthHint = 150;
		startTime.setLayoutData(gdStartTime);
		startTime.setText("UNKNOWN");
		
		Label lblGoodFrames = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblGoodFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblGoodFrames.widthHint = 110;
		lblGoodFrames.setLayoutData(gdLblGoodFrames);
		lblGoodFrames.setText("Good Frames:");
		
		goodFrames = new Label(grpSetup, SWT.NONE);
		GridData gdGoodFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdGoodFrames.widthHint = 65;
		goodFrames.setLayoutData(gdGoodFrames);
		goodFrames.setText("UNKNOWN");
		
		Label lblRawFrames = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblRawFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblRawFrames.widthHint = 110;
		lblRawFrames.setLayoutData(gdLblRawFrames);
		lblRawFrames.setText("Raw Frames:");
		
		rawFrames = new Label(grpSetup, SWT.NONE);
		GridData gdRawFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdRawFrames.widthHint = 65;
		rawFrames.setLayoutData(gdRawFrames);
		rawFrames.setText("UNKNOWN");
		
		Label lblRunDuration = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblRunDuration = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblRunDuration.widthHint = 110;
		lblRunDuration.setLayoutData(gdLblRunDuration);
		lblRunDuration.setText("Run Duration:");
		
		runDuration = new Label(grpSetup, SWT.NONE);
		GridData gdRunDuration = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdRunDuration.widthHint = 65;
		runDuration.setLayoutData(gdRunDuration);
		runDuration.setText("UNKNOWN");
		
		Label lblCountsmev = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblCountsmev = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblCountsmev.widthHint = 110;
		lblCountsmev.setLayoutData(gdLblCountsmev);
		lblCountsmev.setText("Counts (MEV):");
		
		counts = new Label(grpSetup, SWT.NONE);
		GridData gdCounts = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdCounts.widthHint = 65;
		counts.setLayoutData(gdCounts);
		counts.setText("UNKNOWN");
		
		Label lblTotalah = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblTotalah = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblTotalah.widthHint = 110;
		lblTotalah.setLayoutData(gdLblTotalah);
		lblTotalah.setText("Total μAh:");
		
		totalCurrent = new Label(grpSetup, SWT.NONE);
		GridData gdTotalCurrent = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTotalCurrent.widthHint = 65;
		totalCurrent.setLayoutData(gdTotalCurrent);
		totalCurrent.setText("UNKNOWN");
		
		Label lblTimeChannels = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblTimeChannels = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblTimeChannels.widthHint = 110;
		lblTimeChannels.setLayoutData(gdLblTimeChannels);
		lblTimeChannels.setText("Time Channels:");
		
		timeChannels = new Label(grpSetup, SWT.NONE);
		GridData gdTimeChannels = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTimeChannels.widthHint = 65;
		timeChannels.setLayoutData(gdTimeChannels);
		timeChannels.setText("UNKNOWN");
		
		Label lblDaeMemoryUsed = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblDaeMemoryUsed = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblDaeMemoryUsed.widthHint = 110;
		lblDaeMemoryUsed.setLayoutData(gdLblDaeMemoryUsed);
		lblDaeMemoryUsed.setText("DAE Memory Used:");
		
		memoryUsed = new Label(grpSetup, SWT.NONE);
		GridData gdMemoryUsed = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdMemoryUsed.widthHint = 65;
		memoryUsed.setLayoutData(gdMemoryUsed);
		memoryUsed.setText("UNKNOWN");
		
		Label lblCountRatemevh = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblCountRatemevh = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblCountRatemevh.widthHint = 110;
		lblCountRatemevh.setLayoutData(gdLblCountRatemevh);
		lblCountRatemevh.setText("Count Rate (MEV/h):");
		
		countRate = new Label(grpSetup, SWT.NONE);
		GridData gdCountRate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdCountRate.widthHint = 65;
		countRate.setLayoutData(gdCountRate);
		countRate.setText("UNKNOWN");
		
		Label lblSpectra = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblSpectra = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblSpectra.widthHint = 110;
		lblSpectra.setLayoutData(gdLblSpectra);
		lblSpectra.setText("Spectra:");
		
		spectra = new Label(grpSetup, SWT.NONE);
		GridData gdSpectra = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpectra.widthHint = 65;
		spectra.setLayoutData(gdSpectra);
		spectra.setText("UNKNOWN");
		
		Label lblDaeTiming = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblDaeTiming = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblDaeTiming.widthHint = 110;
		lblDaeTiming.setLayoutData(gdLblDaeTiming);
		lblDaeTiming.setText("DAE Timing:");
		
		timing = new Label(grpSetup, SWT.NONE);
		GridData gdTiming = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTiming.widthHint = 65;
		timing.setLayoutData(gdTiming);
		timing.setText("UNKNOWN");
		
		Label lblEventMode = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblEventMode = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblEventMode.widthHint = 110;
		lblEventMode.setLayoutData(gdLblEventMode);
		lblEventMode.setText("Event Mode:");
		
		eventMode = new Label(grpSetup, SWT.NONE);
		GridData gdEventMode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdEventMode.widthHint = 65;
		eventMode.setLayoutData(gdEventMode);
		eventMode.setText("UNKNOWN");
		
		Label lblIsisCycle = new Label(grpSetup, SWT.RIGHT);
		GridData gdLblIsisCycle = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblIsisCycle.widthHint = 110;
		lblIsisCycle.setLayoutData(gdLblIsisCycle);
		lblIsisCycle.setText("ISIS Cycle:");
		
		isisCycle = new Label(grpSetup, SWT.NONE);
		GridData gdIsisCycle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdIsisCycle.widthHint = 65;
		isisCycle.setLayoutData(gdIsisCycle);
		isisCycle.setText("UNKNOWN");
		
        Group titleGroup = new Group(parent, SWT.NONE);
		titleGroup.setLayout(new GridLayout(2, false));
		titleGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTitle = new Label(titleGroup, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText("Title:");
		
		title = new Label(titleGroup, SWT.NONE);
		GridData gdTitle = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdTitle.widthHint = 500;
		title.setLayoutData(gdTitle);
		title.setText("UNKNOWN");
		
		Label lblUsers = new Label(titleGroup, SWT.NONE);
		lblUsers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsers.setText("User(s):");
		
		users = new Label(titleGroup, SWT.NONE);
		GridData gdUsers = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gdUsers.widthHint = 500;
		users.setLayoutData(gdUsers);
		users.setText("UNKNOWN");
		
        Group grpPeriods = new Group(parent, SWT.NONE);
		GridLayout glGrpPeriods = new GridLayout(6, false);
		glGrpPeriods.horizontalSpacing = 20;
		grpPeriods.setLayout(glGrpPeriods);
		grpPeriods.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpPeriods.setText("Periods");
		
		Label lblPeriod = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblPeriod = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblPeriod.widthHint = 110;
		lblPeriod.setLayoutData(gdLblPeriod);
		lblPeriod.setText("Period:");
		
		period = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriod = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriod.widthHint = 65;
		period.setLayoutData(gdPeriod);
		period.setText("UNKNOWN");
		
		Label lblNumberOfPeriods = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblNumberOfPeriods = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblNumberOfPeriods.widthHint = 110;
		lblNumberOfPeriods.setLayoutData(gdLblNumberOfPeriods);
		lblNumberOfPeriods.setText("Number of Periods:");
		
		numberOfPeriods = new Label(grpPeriods, SWT.NONE);
		GridData gdNumberOfPeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdNumberOfPeriods.widthHint = 65;
		numberOfPeriods.setLayoutData(gdNumberOfPeriods);
		numberOfPeriods.setText("UNKNOWN");
		
		Label lblPeriodType = new Label(grpPeriods, SWT.RIGHT);
		lblPeriodType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodType.setText("Period Type:");
		
		periodType = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriodType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriodType.widthHint = 65;
		periodType.setLayoutData(gdPeriodType);
		periodType.setText("UNKNOWN");
		
		Label lblPeriodGoodFrames = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblPeriodGoodFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblPeriodGoodFrames.widthHint = 110;
		lblPeriodGoodFrames.setLayoutData(gdLblPeriodGoodFrames);
		lblPeriodGoodFrames.setText("Period Good Frames:");
		
		periodGoodFrames = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriodGoodFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriodGoodFrames.widthHint = 65;
		periodGoodFrames.setLayoutData(gdPeriodGoodFrames);
		periodGoodFrames.setText("UNKNOWN");
		
		Label lblPeriodRawFrames = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblPeriodRawFrames = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblPeriodRawFrames.widthHint = 110;
		lblPeriodRawFrames.setLayoutData(gdLblPeriodRawFrames);
		lblPeriodRawFrames.setText("Period Raw Frames:");
		
		periodRawFrames = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriodRawFrames = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriodRawFrames.widthHint = 65;
		periodRawFrames.setLayoutData(gdPeriodRawFrames);
		periodRawFrames.setText("UNKNOWN");
		
		Label lblPeriodSequence = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblPeriodSequence = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblPeriodSequence.widthHint = 110;
		lblPeriodSequence.setLayoutData(gdLblPeriodSequence);
		lblPeriodSequence.setText("Period Sequence:");
		
		periodSequence = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriodSequence = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriodSequence.widthHint = 65;
		periodSequence.setLayoutData(gdPeriodSequence);
		periodSequence.setText("UNKNOWN");
		
		Label lblPeriodDuration = new Label(grpPeriods, SWT.RIGHT);
		GridData gdLblPeriodDuration = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblPeriodDuration.widthHint = 110;
		lblPeriodDuration.setLayoutData(gdLblPeriodDuration);
		lblPeriodDuration.setText("Period Duration:");
		
		periodDuration = new Label(grpPeriods, SWT.NONE);
		GridData gdPeriodDuration = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdPeriodDuration.widthHint = 65;
		periodDuration.setLayoutData(gdPeriodDuration);
		periodDuration.setText("UNKNOWN");
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		new Label(grpPeriods, SWT.NONE);
		
        Group grpMonitor = new Group(parent, SWT.NONE);
		GridLayout glGrpMonitor = new GridLayout(6, false);
		glGrpMonitor.horizontalSpacing = 20;
		grpMonitor.setLayout(glGrpMonitor);
		grpMonitor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMonitor.setText("Monitor");
		
		Label lblMonitorSpectrum = new Label(grpMonitor, SWT.RIGHT);
		GridData gdLblMonitorSpectrum = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblMonitorSpectrum.widthHint = 110;
		lblMonitorSpectrum.setLayoutData(gdLblMonitorSpectrum);
		lblMonitorSpectrum.setText("Monitor Spectrum:");
		
		monitorSpectrum = new Label(grpMonitor, SWT.NONE);
		GridData gdMonitorSpectrum = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdMonitorSpectrum.widthHint = 65;
		monitorSpectrum.setLayoutData(gdMonitorSpectrum);
		monitorSpectrum.setText("UNKNOWN");
		
		Label lblMonitorCounts = new Label(grpMonitor, SWT.RIGHT);
		GridData gdLblMonitorCounts = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblMonitorCounts.widthHint = 110;
		lblMonitorCounts.setLayoutData(gdLblMonitorCounts);
		lblMonitorCounts.setText("Monitor Counts:");
		
		monitorCounts = new Label(grpMonitor, SWT.NONE);
		GridData gdMonitorCounts = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdMonitorCounts.widthHint = 65;
		monitorCounts.setLayoutData(gdMonitorCounts);
		monitorCounts.setText("UNKNOWN");
		
		Label lblNpRatio = new Label(grpMonitor, SWT.RIGHT);
		GridData gdLblNpRatio = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblNpRatio.widthHint = 110;
		lblNpRatio.setLayoutData(gdLblNpRatio);
		lblNpRatio.setText("N/P Ratio:");
		
		npRatio = new Label(grpMonitor, SWT.NONE);
		GridData gdNpRatio = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdNpRatio.widthHint = 65;
		npRatio.setLayoutData(gdNpRatio);
		npRatio.setText("UNKNOWN");
		
		Label lblFrom = new Label(grpMonitor, SWT.RIGHT);
		GridData gdLblFrom = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblFrom.widthHint = 110;
		lblFrom.setLayoutData(gdLblFrom);
		lblFrom.setText("From:");
		
		monitorFrom = new Label(grpMonitor, SWT.NONE);
		GridData gdMonitorFrom = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdMonitorFrom.widthHint = 65;
		monitorFrom.setLayoutData(gdMonitorFrom);
		monitorFrom.setText("UNKNOWN");
		
		Label lblTo = new Label(grpMonitor, SWT.RIGHT);
		GridData gdLblTo = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblTo.widthHint = 110;
		lblTo.setLayoutData(gdLblTo);
		lblTo.setText("To:");
		
		monitorTo = new Label(grpMonitor, SWT.NONE);
		GridData gdMonitorTo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdMonitorTo.widthHint = 65;
		monitorTo.setLayoutData(gdMonitorTo);
		monitorTo.setText("UNKNOWN");
		new Label(grpMonitor, SWT.NONE);
		new Label(grpMonitor, SWT.NONE);

        setModel(viewModel);
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

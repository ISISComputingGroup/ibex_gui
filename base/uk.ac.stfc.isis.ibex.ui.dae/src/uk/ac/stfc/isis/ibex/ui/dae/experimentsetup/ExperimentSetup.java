
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;

/**
 * The panel that holds all information about setting up the experiment. Further
 * information is held in child panels.
 */
public class ExperimentSetup extends Composite {

	private ExperimentSetupViewModel viewModel;
	
	private TimeChannelsPanel timeChannels;
	private DataAcquisitionPanel dataAcquisition;
	private PeriodsPanel periods;
	
	private final int timeToDisplayDialog = 2;
	
    /**
     * SWT isn't good at computing the height of this control and so truncates
     * it if the screen gets too small. This is the number of pixels to add to
     * the computed size to make sure that doesn't happen.
     */

	private SendingChangesDialog sendingChanges = new SendingChangesDialog(getShell(), timeToDisplayDialog);
	
    /**
     * The constructor for the panel.
     * 
     * @param parent
     *            The parent composite which this panel belongs to.
     * @param style
     *            The SWT style flags for this panel.
     */
	public ExperimentSetup(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 5;
        gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmTimeChannels = new CTabItem(tabFolder, SWT.NONE);
		tbtmTimeChannels.setText("Time Channels");
		
		Composite timeChannelsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmTimeChannels.setControl(timeChannelsComposite);
		timeChannelsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		timeChannels = new TimeChannelsPanel(timeChannelsComposite, SWT.NONE);
		
		CTabItem tbtmDataAcquisition = new CTabItem(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setText("Data Acquisition");
		
		Composite dataAcquisitionComposite = new Composite(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setControl(dataAcquisitionComposite);
		dataAcquisitionComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		dataAcquisition = new DataAcquisitionPanel(dataAcquisitionComposite, SWT.NONE);
		
		CTabItem tbtmPeriods = new CTabItem(tabFolder, SWT.NONE);
		tbtmPeriods.setText("Periods");
		
		Composite periodsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmPeriods.setControl(periodsComposite);
		periodsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		periods = new PeriodsPanel(periodsComposite, SWT.NONE);
		
		tabFolder.setSelection(0);
        GridData tabFolderGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        // Don't let the tab folder shrink vertically or controls can start
        // disappearing
        tabFolderGridData.minimumHeight = tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        tabFolder.setLayoutData(tabFolderGridData);

        RunSummaryViewModel rsvm = DaeUI.getDefault().viewModel().runSummary();
        SendChangesButton btnSendChanges = new SendChangesButton(this, SWT.NONE, rsvm.actions().begin);
        btnSendChanges.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (viewModel != null) {
                    viewModel.updateDae();
                }
                sendingChanges.open();
            }
        });
        btnSendChanges.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        btnSendChanges.setText("Apply Changes");
	}
	
    /**
     * Binds this panel to a specific view model.
     * 
     * @param viewModel
     *            The view model to bind the panel information to.
     */
	public void bind(final ExperimentSetupViewModel viewModel) {
		this.viewModel = viewModel;
		
		timeChannels.setModel(viewModel.timeChannels());
		dataAcquisition.setModel(viewModel.daeSettings());
		periods.setModel(viewModel.periodSettings());
	}

    /**
     * Enable or disable the content / children panels of this composite.
     * 
     * @param enabled whether the contents should be enabled or not
     */
    public void setChildrenEnabled(boolean enabled) {
        Utils.recursiveSetEnabled(timeChannels, enabled);
        Utils.recursiveSetEnabled(dataAcquisition, enabled);
        Utils.recursiveSetEnabled(periods, enabled);
    }
}

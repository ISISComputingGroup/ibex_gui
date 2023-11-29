
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.UIUtils;
import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;

/**
 * The panel that holds all information about setting up the experiment. Further
 * information is held in child panels.
 */
public class ExperimentSetup {

    private DaeViewModel viewModel;
	
	private TimeChannelsPanel timeChannels;
	private DataAcquisitionPanel dataAcquisition;
	private PeriodsPanel periods;
	private ExperimentSetupViewModel experimentSetupViewModel;
	
	private DataBindingContext bindingContext = new DataBindingContext();
	private PropertyChangeListener experimentalChangeListener;
    private PropertyChangeListener resetChangeLabelsListener;

    private UpdatedValue<Boolean> modelIsRunningProperty;
    
    private Button btnSendChanges;

    private static final Display DISPLAY = Display.getCurrent();
    private static final int FIXED_WIDTH = 700;
    private static final int FIXED_HEIGHT = 600;
	
    PanelViewModel panelViewModel;
    /**
     * Constructor.
     */
    public ExperimentSetup() {
        viewModel = DaeUI.getDefault().viewModel();
        experimentSetupViewModel = viewModel.experimentSetup();
        panelViewModel = new PanelViewModel(this, DISPLAY, experimentSetupViewModel);
        updateIsChanged(false);
    }

    /**
     * Creates the controls for the experimental setup part.
     *
     * @param parent the parent
     */
    @PostConstruct
    @SuppressWarnings("checkstyle:magicnumber")
    public void createPart(final Composite parent) {
        
        ScrolledComposite scrolled = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrolled.setExpandHorizontal(true);
        scrolled.setExpandVertical(true);
        scrolled.setMinSize(FIXED_WIDTH, FIXED_HEIGHT);
        
        Composite content = new Composite(scrolled, SWT.NONE);
        
		GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginTop = 0;
		gridLayout.marginBottom = 10;
		gridLayout.horizontalSpacing = 0;
        content.setLayout(gridLayout);
        
        btnSendChanges = new Button(content, SWT.NONE);
        btnSendChanges.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    viewModel.experimentSetup().updateDae();
                    applyChangesToUI();
                } catch (Exception err) {
                    // Top level error handler. Catch anything and log it, and bring up an error dialog informing the user of the error.
                    IsisLog.getLogger(this.getClass()).error(err);
                    MessageDialog.openError(parent.getShell(), "Internal IBEX Error", 
                            "Please report this error to the IBEX team.\n\nException was: " + err.getMessage());
                }
            }
        });
        GridData gdBtnSendChanges = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdBtnSendChanges.heightHint = 50;
        btnSendChanges.setLayoutData(gdBtnSendChanges);
        String defaultFont = btnSendChanges.getFont().getFontData()[0].getName();
        Font biggerFont = new Font(btnSendChanges.getDisplay(), new FontData(defaultFont, 14, SWT.BOLD));
        btnSendChanges.setFont(biggerFont);
        btnSendChanges.setText("Apply Changes");
        
        /*
         * This part tells the DAE to update the cached values after an external change was detected
         */
        final UpdatedValue<Boolean> inSettingsChange = new UpdatedObservableAdapter<>(Dae.getInstance().observables().currentlyChangingSettings);
		inSettingsChange.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Boolean isUpdating = inSettingsChange.getValue();
				if (isUpdating != null) {
					if (!isUpdating) {
	                	Display.getDefault().asyncExec(new Runnable() {
	                	    public void run() {
	        	                try {
	        	                    applyChangesToUI();
	        	                } catch (Exception err) {
	        	                    // Expected to go off on startup before the xml file is properly loaded
	        	                }
	                	    }
	                	});
					}
				}
			}
		}, true);
		
        CTabFolder tabFolder = new CTabFolder(content, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        scrolled.setContent(content);
        
		CTabItem tbtmTimeChannels = new CTabItem(tabFolder, SWT.NONE);
		tbtmTimeChannels.setText("Time Channels");
		
		Composite timeChannelsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmTimeChannels.setControl(timeChannelsComposite);
		timeChannelsComposite.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		timeChannels = new TimeChannelsPanel(timeChannelsComposite, SWT.NONE, panelViewModel);
		
		CTabItem tbtmDataAcquisition = new CTabItem(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setText("Data Acquisition");
		
		Composite dataAcquisitionComposite = new Composite(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setControl(dataAcquisitionComposite);
		dataAcquisitionComposite.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		dataAcquisition = new DataAcquisitionPanel(dataAcquisitionComposite, SWT.NONE, panelViewModel);
		
		CTabItem tbtmPeriods = new CTabItem(tabFolder, SWT.NONE);
		tbtmPeriods.setText("Periods");
		
		Composite periodsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmPeriods.setControl(periodsComposite);
		periodsComposite.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		periods = new PeriodsPanel(periodsComposite, SWT.NONE, panelViewModel);
		
		tabFolder.setSelection(0);
        GridData tabFolderGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        // Don't let the tab folder shrink vertically or controls can start
        // disappearing
        tabFolderGridData.minimumHeight = tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        tabFolder.setLayoutData(tabFolderGridData);

        //Bind the send changes button to the begin action so that it is only available when write enabled and in SETUP
        RunSummaryViewModel rsvm = DaeUI.getDefault().viewModel().runSummary();
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSendChanges), BeanProperties.value("canExecute").observe(rsvm.actions().begin));
        
        resetChangeLabelsListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        resetChangeLabels();
                    }
                });
            }
        };
        
        experimentSetupViewModel.addPropertyChangeListener("resetLayout", resetChangeLabelsListener);
        
        bind(viewModel.experimentSetup());
        setModel(viewModel);
        if (panelViewModel.getIsChanged().containsValue(true)) {
            btnSendChanges.setEnabled(true);
            btnSendChanges.setBackground(panelViewModel.getColour("changedColour"));
        } else {
            btnSendChanges.setEnabled(false);
            btnSendChanges.setBackground(panelViewModel.getColour("unchangedColour"));
        }
        
        tabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tabFolder.getSelection().equals(tbtmPeriods)) {
                    periods.addFirstTableListener();
                    periods.ifTableValuesDifferentFromCachedValuesThenChangeLabel();
                }
            }
        });
        
        resetChangeLabels();
	}
    
    private void resetChangeLabels() {
    	updateChangeListeners(); 
    	addChangeListeners();
        changeLabelsIfDifferentFromCachedValues();
    }
	
    /**
     * Binds this panel to a specific view model.
     * 
     * @param viewModel
     *            The view model to bind the panel information to.
     */
    private void bind(final ExperimentSetupViewModel viewModel) {
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
        UIUtils.recursiveSetEnabled(timeChannels, enabled);
        UIUtils.recursiveSetEnabled(dataAcquisition, enabled);
        UIUtils.recursiveSetEnabled(periods, enabled);
    }

    /**
     * Sets the model for the DAE view.
     *
     * @param viewModel
     *            the new model
     */
    public void setModel(final DaeViewModel viewModel) {

        experimentalChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                configureExperimentSetupForRunState(viewModel.isRunning().getValue());
            }
        };
        modelIsRunningProperty = viewModel.isRunning();
        modelIsRunningProperty.addPropertyChangeListener(experimentalChangeListener, true);

    }

    private void configureExperimentSetupForRunState(final Boolean isRunning) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                // access to the Experiment Setup page is disabled if IBEX is not run on the localhost
                setChildrenEnabled(isRunning != null && !isRunning && Instrument.getInstance().isLocalInstrument());
            }
        });
    }
    
    /**
     * Updates the listeners on the widgets, tells the viewModel that the changes have been applied and if the widgets contain the applied values
     * then removes labels denoting a change not applied to the instrument.
     * 
     */
    private void applyChangesToUI() {
        updateIsChanged(false);
        btnSendChanges.setEnabled(false);
        updateChangeListeners();
        changeLabelsIfDifferentFromCachedValues();
    }
    
    /**
     * A method to check if the widgets in the different panels contain the applied values and removes labels denoting a change not applied to the 
     * instrument if the change has been applied.
     */
    public void changeLabelsIfDifferentFromCachedValues() {
        timeChannels.ifWidgetValueDifferentFromCachedValueThenChangeLabel();
        dataAcquisition.ifWidgetValueDifferentFromCachedValueThenChangeLabel();
        periods.ifWidgetValueDifferentFromCachedValueThenChangeLabel();
    }
    
    /**
     * Tells the viewModel if changes have been made in the experiment setup but haven't been applied.
     * 
     * @isChanged True if changes have been made in the experiment setup but haven't been applied.
     */
    private void updateIsChanged(boolean isChanged) {
        panelViewModel.clearIsChanged();
        viewModel.experimentSetup().setIsChanged(isChanged);
    }
    
    /**
     *  Allows to enable or disable the "send change" button.
     *  
     * @param enable
     *              True if the button should be enabled.
     */
    public void setSendChangeBtnEnableState(boolean enable) {
        btnSendChanges.setEnabled(enable);
        if (enable) {
            btnSendChanges.setBackground(panelViewModel.getColour("changedColour"));
        } else {
            btnSendChanges.setBackground(panelViewModel.getColour("unchangedColour"));
        }
    }
    
    /**
     * Adds the listeners used in the panels.
     */
    private void addChangeListeners() { 
        timeChannels.createInitialCachedValues();
        timeChannels.addListeners();
        dataAcquisition.createInitialCachedValues();
        periods.createInitialCachedValues();
        periods.addListeners();
    }
    
    /**
     * Updates the listeners used in the panels.
     */
    private void updateChangeListeners() {
        timeChannels.updateListeners();
        dataAcquisition.updateListeners();
        periods.updateListeners();
    }
    
}

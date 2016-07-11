
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.CalculationMethod;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;
import uk.ac.stfc.isis.ibex.ui.Utils;

public class TimeChannelsPanel extends Composite {
    private Composite tcbSettingsSwitchPanel;
    private Composite tcbFilePanel;
    private Composite timeRegimesPanel;
	private List<TimeRegimeView> timeRegimeViews = new ArrayList<TimeRegimeView>();
	
	private TimeChannelsViewModel viewModel;
	private Combo timeUnit;
    private DataBindingContext bindingContext;
    StackLayout stack;

    private Combo timeChannelFile;
    Button radioSpecifyParameters;
    Button radioUseTCBFile;
    private CalculationMethod calculationMethod;

    private FontData fontdata;
	private static final Display DISPLAY = Display.getCurrent();
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public TimeChannelsPanel(Composite parent, int style) {

        // Set window spacing parameters
		super(parent, style);
        GridLayout glParent = new GridLayout(1, false);
        glParent.verticalSpacing = 15;
        glParent.marginTop = 5;
        glParent.marginLeft = 5;
        setLayout(glParent);

//        Group calculationMethodSelector = new Group(this, SWT.NONE);
//        calculationMethodSelector.setText("Select Calculation Method");
//        calculationMethodSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
//        Label lblCalculationMethod = new Label(parameters, SWT.NONE);
//        lblCalculationMethod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//        lblCalculationMethod.setText("Calculation Method:");

        String[] calcMethodNames = CalculationMethod.allToString().toArray(new String[0]);

        Label lblSelectionMethod = new Label(this, SWT.NONE);
        lblSelectionMethod.setText("Select Calculation Method: ");
        fontdata = lblSelectionMethod.getFont().getFontData()[0];
        lblSelectionMethod.setFont(SWTResourceManager.getFont(fontdata.getName(), fontdata.getHeight(), SWT.BOLD));
        addMethodSelectionPanel(this);

        Group tcbSettings = new Group(this, SWT.NONE);
        tcbSettings.setText("Time Channel Settings");
        tcbSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tcbSettings.setLayout(new GridLayout(1, false));
        addTimeUnitPanel(tcbSettings);

        tcbSettingsSwitchPanel = new Composite(tcbSettings, SWT.NONE);
        tcbSettingsSwitchPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack = new StackLayout();
        tcbSettingsSwitchPanel.setLayout(stack);

        tcbFilePanel = new Composite(tcbSettingsSwitchPanel, SWT.NONE);
        tcbFilePanel.setLayout(new GridLayout(1, false));
        addTimeChannelFilePanel(tcbFilePanel);

        timeRegimesPanel = new Composite(tcbSettingsSwitchPanel, SWT.FILL);
        timeRegimesPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        timeRegimesPanel.setLayout(new GridLayout(3, false));
	}
	
	public void setModel(final TimeChannelsViewModel viewModel) {
		this.viewModel = viewModel;
//        SelectObservableValue test = new SelectObservableValue(CalculationMethod.class);
//
//        IObservableValue radioSpecifyParametersObserveSelection =
//                SWTObservables.observeSelection(radioSpecifyParameters);
//        test.addOption(CalculationMethod.SPECIFY_PARAMETERS, radioSpecifyParametersObserveSelection);
//
//        IObservableValue radioReadFromFileObserveSelection = SWTObservables.observeSelection(radioReadFromFile);
//        test.addOption(CalculationMethod.USE_TCB_FILE, radioReadFromFileObserveSelection);
//
        bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(timeUnit),
                BeanProperties.value("timeUnit").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(calculationMethod),
                BeanProperties.value("calculationMethod").observe(viewModel));
        bindingContext.bindList(WidgetProperties.items().observe(timeChannelFile),
                BeanProperties.list("timeChannelFileList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(timeChannelFile),
                BeanProperties.value("timeChannelFile").observe(viewModel));

		updateTimeRegimes();
		viewModel.addPropertyChangeListener("timeRegimes", new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateTimeRegimes();
			}
		});
	}
	
	private void updateTimeRegimes() {
		DISPLAY.asyncExec(new Runnable() {
			@Override
			public void run() {
				clearExistingTimeRegimeViews();

				GridData timeRegimeGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				int count = 1;
				
				for (TimeRegime timeRegime : viewModel.timeRegimes()) {
                    TimeRegimeView view = new TimeRegimeView(timeRegimesPanel, SWT.NONE);
					view.setLayoutData(timeRegimeGridData);					
					
					view.setModel(timeRegime);
					view.setTitle(String.format("Time regime %d", count));
					
					count++;
					timeRegimeViews.add(view);					
				}
				
                timeRegimesPanel.layout();
                Utils.recursiveSetEnabled(timeRegimesPanel, timeRegimesPanel.getEnabled());
			}
		});

	}

	private void clearExistingTimeRegimeViews() {
		for (TimeRegimeView view : timeRegimeViews) {
			view.dispose();
		}
		timeRegimeViews.clear();
	}

    private void displayFileDialog() {
        FileDialog dialog = new FileDialog(this.getShell(), SWT.SINGLE);
        String filePath = dialog.open();
        if (filePath != null) {
            timeChannelFile.setText(filePath);
        }
    }

    private void addMethodSelectionPanel(Composite parent) {

        Composite methodSelectionPanel = new Composite(parent, SWT.NONE);
        methodSelectionPanel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        methodSelectionPanel.setLayout(new GridLayout(1, false));
        GridLayout glCalcMethod = new GridLayout(1, false);
        glCalcMethod.horizontalSpacing = 100;
        glCalcMethod.marginTop = 5;

        radioSpecifyParameters = new Button(methodSelectionPanel, SWT.RADIO);
        radioSpecifyParameters.setText(CalculationMethod.SPECIFY_PARAMETERS.toString());
        radioUseTCBFile = new Button(methodSelectionPanel, SWT.RADIO);
        radioUseTCBFile.setText(CalculationMethod.USE_TCB_FILE.toString());

        SelectionListener listener = new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                switchSettingsPanel();
                tcbSettingsSwitchPanel.layout();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        };
        radioSpecifyParameters.addSelectionListener(listener);
        radioUseTCBFile.addSelectionListener(listener);
    }

    private void addTimeUnitPanel(Composite parent) {

        Composite timeUnitPanel = new Composite(parent, SWT.NONE);
        timeUnitPanel.setLayout(new GridLayout(2, false));
        timeUnitPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label lblTimeUnit = new Label(timeUnitPanel, SWT.NONE);
        lblTimeUnit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimeUnit.setText("Time Unit:");

        timeUnit = new Combo(timeUnitPanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        timeUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        timeUnit.setItems(TimeUnit.allToString().toArray(new String[0]));
    }

    private void addTimeChannelFilePanel(Composite parent) {
        
        Composite timeChannelFileContent = new Composite(parent, SWT.NONE);
        timeChannelFileContent.setLayout(new GridLayout(2, false));
        timeChannelFileContent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Label lblTimeChannelFile = new Label(timeChannelFileContent, SWT.NONE);
        lblTimeChannelFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimeChannelFile.setText("Time Channel File:");

        timeChannelFile = new Combo(timeChannelFileContent, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdTimeChannelFile = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdTimeChannelFile.widthHint = 400;
        timeChannelFile.setLayoutData(gdTimeChannelFile);
    }

    public void switchSettingsPanel() {
        if (radioUseTCBFile.getSelection()) {
            setCalculationMethod(CalculationMethod.USE_TCB_FILE);
            stack.topControl = tcbFilePanel;
        } else {
            setCalculationMethod(CalculationMethod.SPECIFY_PARAMETERS);
            stack.topControl = timeRegimesPanel;
        }
    }

    public void setCalculationMethod(CalculationMethod method) {
        calculationMethod = method;
    }

    public CalculationMethod getCalculationMethod() {
        return calculationMethod;
    }
}

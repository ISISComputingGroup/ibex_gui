
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;

public class PeriodsPanel extends Composite {

    private DataBindingContext bindingContext;

    private PeriodsViewModel model;

	private Combo cmbPeriodFile;
	private Combo cmbPeriodType;
	private Text txtSoftwarePeriods;
	private Text txtHardwarePeriods;
	private Text txtOutputDelay;
    private Label lblPeriodFileRB;
    private Button radioSpecifyParameters;
    private Button radioUsePeriodFile;
    private Label lblNote;

    private Group grpSettings;
    private Composite cmpSwitchTypeSoftware;
    private Composite cmpSwitchTypeHardware;
    private Composite cmpSource;
    private Composite cmpSwitchSourceFile;
    private Composite cmpSwitchSourceParam;
    private PeriodsTableView tblPeriods;

    private StackLayout stackType = new StackLayout();
    private StackLayout stackSource = new StackLayout();

	private static final Display DISPLAY = Display.getCurrent();
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public PeriodsPanel(Composite parent, int style) {
		super(parent, style);
        GridLayout gl_main = new GridLayout(1, false);
        gl_main.verticalSpacing = 15;
        setLayout(gl_main);
		
        GridLayout gl_noMargins = new GridLayout();
        gl_noMargins.marginWidth = 0;
        gl_noMargins.marginHeight = 0;

        // Overall Setup
        Composite cmpSetup = new Composite(this, SWT.NONE);
        cmpSetup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_setupPanel = new GridLayout(1, false);
        gl_setupPanel.verticalSpacing = 10;
        cmpSetup.setLayout(gl_setupPanel);

        Composite cmpPeriodType = new Composite(cmpSetup, SWT.NONE);
        GridLayout gl_cmpPeriodType = new GridLayout(2, false);
        gl_cmpPeriodType.marginWidth = 0;
        cmpPeriodType.setLayout(gl_cmpPeriodType);

        Label lblPeriodType = new Label(cmpPeriodType, SWT.NONE);
        lblPeriodType.setText("Period Type:");

        cmbPeriodType = new Combo(cmpPeriodType, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmbPeriodType.setItems(PeriodControlType.allToString().toArray(new String[0]));

        // Group dynamically displaying setup options
        grpSettings = new Group(this, SWT.NONE);
        grpSettings.setText("Period Settings");
        grpSettings.setLayout(stackType);
        grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel to set software periods
        cmpSwitchTypeSoftware = new Composite(grpSettings, SWT.NONE);
        GridLayout gl_cmpSwitchTypeSoftware = new GridLayout(2, false);
        gl_cmpSwitchTypeSoftware.marginWidth = 10;
        gl_cmpSwitchTypeSoftware.marginHeight = 10;
        cmpSwitchTypeSoftware.setLayout(gl_cmpSwitchTypeSoftware);
        cmpSwitchTypeSoftware.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Label lblNumberOfSoftware = new Label(cmpSwitchTypeSoftware, SWT.NONE);
        lblNumberOfSoftware.setText("Number of software periods:");

        txtSoftwarePeriods = new Text(cmpSwitchTypeSoftware, SWT.BORDER | SWT.RIGHT);
        GridData gd_txtSoftwarePeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_txtSoftwarePeriods.widthHint = 60;
        txtSoftwarePeriods.setLayoutData(gd_txtSoftwarePeriods);

        // Panel to set hardware periods
        cmpSwitchTypeHardware = new Composite(grpSettings, SWT.NONE);
        GridLayout gl_cmpSwitchTypeHardware = new GridLayout(1, false);
        gl_cmpSwitchTypeHardware.marginWidth = 10;
        gl_cmpSwitchTypeHardware.marginHeight = 10;
        gl_cmpSwitchTypeHardware.verticalSpacing = 15;
        cmpSwitchTypeHardware.setLayout(gl_cmpSwitchTypeHardware);
        cmpSwitchTypeHardware.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite cmpHardwareSettings = new Composite(cmpSwitchTypeHardware, SWT.NONE);
        GridLayout gl_cmpHardwareSettings = new GridLayout(5, false);
        gl_cmpHardwareSettings.marginWidth = 0;
        gl_cmpHardwareSettings.marginHeight = 0;
        cmpHardwareSettings.setLayout(gl_cmpHardwareSettings);
        cmpHardwareSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        Label lblHardwarePeriodSequences = new Label(cmpHardwareSettings, SWT.NONE);
        lblHardwarePeriodSequences.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHardwarePeriodSequences.setText("Hardware period sequences:");

        txtHardwarePeriods = new Text(cmpHardwareSettings, SWT.BORDER | SWT.RIGHT);
        GridData gd_txtHardwarePeriods = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gd_txtHardwarePeriods.widthHint = 80;
        txtHardwarePeriods.setLayoutData(gd_txtHardwarePeriods);
        new Label(cmpHardwareSettings, SWT.NONE);

        Label lblOutputDelayus = new Label(cmpHardwareSettings, SWT.NONE);
        lblOutputDelayus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblOutputDelayus.setText("Output delay (μs):");

        txtOutputDelay = new Text(cmpHardwareSettings, SWT.BORDER | SWT.RIGHT);
        GridData gd_txtOutputDelay = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
        gd_txtOutputDelay.widthHint = 80;
        txtOutputDelay.setLayoutData(gd_txtOutputDelay);

        Label lblSelectionMethod = new Label(cmpHardwareSettings, SWT.NONE);
        lblSelectionMethod.setText("Select Period Source: ");
        lblSelectionMethod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));

        radioSpecifyParameters = new Button(cmpHardwareSettings, SWT.RADIO);
        GridData gd_radioSpecifyParameters = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
        gd_radioSpecifyParameters.horizontalIndent = 5;
        radioSpecifyParameters.setLayoutData(gd_radioSpecifyParameters);
        radioSpecifyParameters.setText(PeriodSetupSource.PARAMETERS.toString());

        radioUsePeriodFile = new Button(cmpHardwareSettings, SWT.RADIO);
        GridData gd_radioUsePeriodFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
        gd_radioUsePeriodFile.horizontalIndent = 5;
        radioUsePeriodFile.setLayoutData(gd_radioUsePeriodFile);
        radioUsePeriodFile.setText(PeriodSetupSource.FILE.toString());

        SelectionListener sourceSelectionListener = new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (radioUsePeriodFile.getSelection()) {
                    model.setSetupSource(PeriodSetupSource.FILE);
                } else {
                    model.setSetupSource(PeriodSetupSource.PARAMETERS);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        };
        radioSpecifyParameters.addSelectionListener(sourceSelectionListener);
        radioUsePeriodFile.addSelectionListener(sourceSelectionListener);

        cmpSource = new Composite(cmpSwitchTypeHardware, SWT.NONE);
        cmpSource.setLayout(stackSource);
        cmpSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel with table for manually setting periods
        cmpSwitchSourceParam = new Composite(cmpSource, SWT.NONE);
        cmpSwitchSourceParam.setLayout(gl_noMargins);

        lblNote = new Label(cmpSwitchSourceParam, SWT.NONE);
        lblNote.setText("Frames are not used in external signal control mode");
        lblNote.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        tblPeriods = new PeriodsTableView(cmpSwitchSourceParam, SWT.NONE);
        tblPeriods.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel to load file with period settings
        cmpSwitchSourceFile = new Composite(cmpSource, SWT.NONE);
        GridLayout gl_cmpSwitchSourceFile = new GridLayout(3, false);
        gl_cmpSwitchSourceFile.marginWidth = 0;
        gl_cmpSwitchSourceFile.horizontalSpacing = 20;
        cmpSwitchSourceFile.setLayout(gl_cmpSwitchSourceFile);

        Label lblPeriod = new Label(cmpSwitchSourceFile, SWT.NONE);
        lblPeriod.setText("Period File:");

        Label lblPeriodRB = new Label(cmpSwitchSourceFile, SWT.NONE);
        lblPeriodRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPeriodRB.setText("Current:");

        lblPeriodFileRB = new Label(cmpSwitchSourceFile, SWT.NONE);
        lblPeriodFileRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblPeriodFileRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        Label lblPeriodSpacer = new Label(cmpSwitchSourceFile, SWT.NONE);

        Label lblPeriodChange = new Label(cmpSwitchSourceFile, SWT.NONE);
        lblPeriodChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPeriodChange.setText("Change:");

        cmbPeriodFile = new Combo(cmpSwitchSourceFile, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmbPeriodFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        cmbPeriodFile.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setNewPeriodFile(cmbPeriodFile.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        cmbPeriodType.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setPeriodType(cmbPeriodType.getSelectionIndex());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
	}

    /**
     * Binds model data to the relevant UI elements for automatic update.
     * 
     * @param viewModel the model holding the period settings.
     */
	public void setModel(final PeriodsViewModel viewModel) {
        this.model = viewModel;

		bindingContext = new DataBindingContext();	
		
		bindingContext.bindList(WidgetProperties.items().observe(cmbPeriodFile), BeanProperties.list("periodFilesList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(lblPeriodFileRB), BeanProperties.value("periodFile").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(cmbPeriodType), BeanProperties.value("periodType").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtSoftwarePeriods), BeanProperties.value("softwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtHardwarePeriods), BeanProperties.value("hardwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtOutputDelay), BeanProperties.value("outputDelay").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.enabled().observe(txtHardwarePeriods),
                BeanProperties.value("internalPeriod").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(radioSpecifyParameters),
                BeanProperties.value("hardwarePeriod").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(radioUsePeriodFile),
                BeanProperties.value("hardwarePeriod").observe(viewModel));

        viewModel.addPropertyChangeListener("internalPeriod", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lblNote.setVisible(!model.isInternalPeriod());
            }
        });

		viewModel.addPropertyChangeListener("periods", new PropertyChangeListener() {
			@Override
            public void propertyChange(PropertyChangeEvent evt) {
				setPeriods(viewModel.periods());
			}
		});

        viewModel.addPropertyChangeListener("periodType", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTypeStack(matchType((PeriodControlType) evt.getNewValue()));
            }

        });

        viewModel.addPropertyChangeListener("setupSource", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateSourceStack(matchSource((PeriodSetupSource) evt.getNewValue()));
            }
        });

        setPeriods(viewModel.periods());
        updateTypeStack(matchType(PeriodControlType.values()[model.getPeriodType()]));
        updateSourceStack(matchSource(model.getSetupSource()));
	}
	
	private void setPeriods(final List<Period> newPeriods) {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				tblPeriods.setPeriods(newPeriods);
			}
		});
	}

    private Control matchType(PeriodControlType type) {
        if (type == PeriodControlType.SOFTWARE) {
            return cmpSwitchTypeSoftware;
        } else {
            return cmpSwitchTypeHardware;
        }
    }

    private Control matchSource(PeriodSetupSource type) {
        if (type == PeriodSetupSource.FILE) {
            radioUsePeriodFile.setSelection(true);
            return cmpSwitchSourceFile;
        } else {
            radioSpecifyParameters.setSelection(true);
            return cmpSwitchSourceParam;
        }
    }

    private void updateTypeStack(final Control top) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                stackType.topControl = top;
                grpSettings.layout();
            }
        });
	}

    private void updateSourceStack(final Control top) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                stackSource.topControl = top;
                cmpSource.layout();
            }
        });
    }
}

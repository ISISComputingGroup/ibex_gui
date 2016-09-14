
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

	private Combo periodFileSelector;
	private Combo periodTypeSelector;
	private Text softwarePeriods;
	private Text hardwarePeriods;
	private Text outputDelay;
    private Label periodFileRB;
    private Button radioSpecifyParameters;
    private Button radioUsePeriodFile;

    private PeriodsTableView periods;
    private Composite tableSwitchPanel;
    private Composite fileSwitchPanel;
    private Composite softwareSwitchPanel;
    private Composite hardwareSwitchPanel;
    private Composite sourcePanel;
    private Group grpSettings;

    private PeriodControlType periodType;
    private PeriodSetupSource periodSource;

    private StackLayout typeStack = new StackLayout();
    private StackLayout sourceStack = new StackLayout();

	private static final Display DISPLAY = Display.getCurrent();
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public PeriodsPanel(Composite parent, int style) {
		super(parent, style);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.verticalSpacing = 15;
        setLayout(gridLayout);
		
        GridLayout gl_noMargins = new GridLayout();
        gl_noMargins.marginWidth = 0;
        gl_noMargins.marginHeight = 0;

        // Overall Setup
        Composite setupPanel = new Composite(this, SWT.NONE);
        setupPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_setupPanel = new GridLayout(1, false);
        gl_setupPanel.verticalSpacing = 10;
        setupPanel.setLayout(gl_setupPanel);

        Composite periodTypePanel = new Composite(setupPanel, SWT.NONE);
        GridLayout gl_periodTypePanel = new GridLayout(2, false);
        gl_periodTypePanel.marginWidth = 0;
        periodTypePanel.setLayout(gl_periodTypePanel);

        Label lblPeriodType = new Label(periodTypePanel, SWT.NONE);
        lblPeriodType.setText("Period Type:");

        periodTypeSelector = new Combo(periodTypePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        periodTypeSelector.setItems(PeriodControlType.allToString().toArray(new String[0]));

        radioSpecifyParameters = new Button(setupPanel, SWT.RADIO);
        radioSpecifyParameters.setText(PeriodSetupSource.PARAMETERS.toString());

        radioUsePeriodFile = new Button(setupPanel, SWT.RADIO);
        radioUsePeriodFile.setText(PeriodSetupSource.FILE.toString());

        SelectionListener listener = new SelectionListener() {
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
        radioSpecifyParameters.addSelectionListener(listener);
        radioUsePeriodFile.addSelectionListener(listener);

        grpSettings = new Group(this, SWT.NONE);
        grpSettings.setText("Period Settings");
        grpSettings.setLayout(typeStack);
        grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel to set software periods
        softwareSwitchPanel = new Composite(grpSettings, SWT.NONE);
        GridLayout gl_softwareSwitchPanel = new GridLayout(2, false);
        gl_softwareSwitchPanel.marginWidth = 10;
        gl_softwareSwitchPanel.marginHeight = 10;
        softwareSwitchPanel.setLayout(gl_softwareSwitchPanel);
        softwareSwitchPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Label lblNumberOfSoftware = new Label(softwareSwitchPanel, SWT.NONE);
        lblNumberOfSoftware.setText("Software periods:");

        softwarePeriods = new Text(softwareSwitchPanel, SWT.BORDER | SWT.RIGHT);
        GridData gd_softwarePeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_softwarePeriods.widthHint = 60;
        softwarePeriods.setLayoutData(gd_softwarePeriods);

        // Panel to set hardware periods
        hardwareSwitchPanel = new Composite(grpSettings, SWT.NONE);
        GridLayout gl_hardwareSwitchPanel = new GridLayout(1, false);
        gl_hardwareSwitchPanel.marginWidth = 10;
        gl_hardwareSwitchPanel.marginHeight = 10;
        gl_hardwareSwitchPanel.verticalSpacing = 15;
        hardwareSwitchPanel.setLayout(gl_hardwareSwitchPanel);
        hardwareSwitchPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite composite_2 = new Composite(hardwareSwitchPanel, SWT.NONE);
        GridLayout gl_composite_2 = new GridLayout(5, false);
        gl_composite_2.marginWidth = 0;
        gl_composite_2.marginHeight = 0;
        composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lblHardwarePeriodSequences = new Label(composite_2, SWT.NONE);
		lblHardwarePeriodSequences.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHardwarePeriodSequences.setText("Hardware period sequences:");
		
		hardwarePeriods = new Text(composite_2, SWT.BORDER | SWT.RIGHT);
		GridData gd_hardwarePeriods = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_hardwarePeriods.widthHint = 80;
		hardwarePeriods.setLayoutData(gd_hardwarePeriods);
		new Label(composite_2, SWT.NONE);
		
		Label lblOutputDelayus = new Label(composite_2, SWT.NONE);
		lblOutputDelayus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblOutputDelayus.setText("Output delay (μs):");
		
		outputDelay = new Text(composite_2, SWT.BORDER | SWT.RIGHT);
		GridData gd_outputDelay = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_outputDelay.widthHint = 80;
		outputDelay.setLayoutData(gd_outputDelay);
		
        sourcePanel = new Composite(hardwareSwitchPanel, SWT.NONE);
        sourcePanel.setLayout(sourceStack);
        sourcePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel with table for manually setting periods
        tableSwitchPanel = new Composite(sourcePanel, SWT.NONE);
        tableSwitchPanel.setLayout(gl_noMargins);

        periods = new PeriodsTableView(tableSwitchPanel, SWT.NONE);
        periods.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Panel to load file with period settings
        fileSwitchPanel = new Composite(sourcePanel, SWT.NONE);
        GridLayout glfileSwitchPanel = new GridLayout(3, false);
        glfileSwitchPanel.marginWidth = 0;
        glfileSwitchPanel.horizontalSpacing = 20;
        fileSwitchPanel.setLayout(glfileSwitchPanel);

        Label lblPeriod = new Label(fileSwitchPanel, SWT.NONE);
        lblPeriod.setText("Period File:");

        Label lblPeriodRB = new Label(fileSwitchPanel, SWT.NONE);
        lblPeriodRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPeriodRB.setText("Current:");

        periodFileRB = new Label(fileSwitchPanel, SWT.NONE);
        periodFileRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        periodFileRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        Label lblPeriodSpacer = new Label(fileSwitchPanel, SWT.NONE);

        Label lblPeriodChange = new Label(fileSwitchPanel, SWT.NONE);
        lblPeriodChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPeriodChange.setText("Change:");

        periodFileSelector = new Combo(fileSwitchPanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        periodFileSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        periodFileSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setNewPeriodFile(periodFileSelector.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        periodTypeSelector.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setPeriodType(periodTypeSelector.getSelectionIndex());
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
		
		bindingContext.bindList(WidgetProperties.items().observe(periodFileSelector), BeanProperties.list("periodFilesList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(periodFileRB), BeanProperties.value("periodFile").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(periodTypeSelector), BeanProperties.value("periodType").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(softwarePeriods), BeanProperties.value("softwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(hardwarePeriods), BeanProperties.value("hardwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(outputDelay), BeanProperties.value("outputDelay").observe(viewModel));

		setPeriods(viewModel.periods());
		viewModel.addPropertyChangeListener("periods", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setPeriods(viewModel.periods());
			}
		});

        viewModel.addPropertyChangeListener("setupSource", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                periodSource = model.getSetupSource();
                updateWindow();
            }
        });

        viewModel.addPropertyChangeListener("periodType", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                periodType = PeriodControlType.values()[model.getPeriodType()];
                updateWindow();
            }
        });
        periodSource = model.getSetupSource();
        periodType = PeriodControlType.values()[model.getPeriodType()];
        updateWindow();
	}
	
	private void setPeriods(final List<Period> newPeriods) {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				periods.setPeriods(newPeriods);
			}
		});
	}

    private void updateWindow() {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                radioSpecifyParameters.setEnabled(false);
                radioUsePeriodFile.setEnabled(false);
                typeStack.topControl = softwareSwitchPanel;
                if ((periodType == PeriodControlType.HARDWARE_DAE)
                        || (periodType == PeriodControlType.HARDWARE_EXTERNAL)) {
                    radioSpecifyParameters.setEnabled(true);
                    radioUsePeriodFile.setEnabled(true);
                    typeStack.topControl = hardwareSwitchPanel;
                }
                if (periodSource == PeriodSetupSource.PARAMETERS) {
                    radioUsePeriodFile.setSelection(false);
                    radioSpecifyParameters.setSelection(true);
                    sourceStack.topControl = tableSwitchPanel;
                } else {
                    radioSpecifyParameters.setSelection(false);
                    radioUsePeriodFile.setSelection(true);
                    sourceStack.topControl = fileSwitchPanel;
                }
                grpSettings.layout();
                sourcePanel.layout();
            }
        });
    }
}

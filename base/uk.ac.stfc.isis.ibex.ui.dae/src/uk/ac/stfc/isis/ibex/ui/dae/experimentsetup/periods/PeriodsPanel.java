
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
	
	private Combo setupSource;
	private Combo periodFile;
	private Combo periodType;
	private Text softwarePeriods;
	private Text hardwarePeriods;
	private Text outputDelay;
    private Label periodFileRB;
	private PeriodsTableView periods;
    private PeriodsViewModel model;
	
	private static final Display DISPLAY = Display.getCurrent();
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public PeriodsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group grpSetup = new Group(this, SWT.NONE);
		grpSetup.setText("Setup");
		grpSetup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSetup.setLayout(new GridLayout(5, false));
		
		Label lblPeriodSetupSource = new Label(grpSetup, SWT.NONE);
		lblPeriodSetupSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodSetupSource.setSize(107, 15);
		lblPeriodSetupSource.setText("Period setup source:");
		
		setupSource = new Combo(grpSetup, SWT.DROP_DOWN | SWT.READ_ONLY);
		setupSource.setSize(177, 23);
		setupSource.setItems(PeriodSetupSource.allToString().toArray(new String[0]));
		new Label(grpSetup, SWT.NONE);
		new Label(grpSetup, SWT.NONE);
		new Label(grpSetup, SWT.NONE);
		
		Label lblPeriodFile = new Label(grpSetup, SWT.NONE);
		lblPeriodFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodFile.setSize(58, 15);
		lblPeriodFile.setText("Period File:");
		
        periodFile = new Combo(grpSetup, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd_periodFile = new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1);
		gd_periodFile.widthHint = 500;
		periodFile.setLayoutData(gd_periodFile);
		periodFile.setSize(412, 25);

        Label spacer = new Label(grpSetup, SWT.None);

        Composite currentFileRBPanel = new Composite(grpSetup, SWT.NONE);
        GridLayout glCurrentFileRBPanel = new GridLayout(2, false);
        glCurrentFileRBPanel.marginLeft = 0;
        glCurrentFileRBPanel.marginTop = 0;
        glCurrentFileRBPanel.marginBottom = 10;
        currentFileRBPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        currentFileRBPanel.setLayout(glCurrentFileRBPanel);

        Label lblCrntDetector = new Label(currentFileRBPanel, SWT.None);
        lblCrntDetector.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lblCrntDetector.setText("Current:");

        periodFileRB = new Label(currentFileRBPanel, SWT.NONE);
        periodFileRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblPeriodType = new Label(grpSetup, SWT.NONE);
		lblPeriodType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodType.setSize(66, 15);
		lblPeriodType.setText("Period Type:");
		
		periodType = new Combo(grpSetup, SWT.DROP_DOWN | SWT.READ_ONLY);
		periodType.setSize(177, 23);
		periodType.setItems(PeriodControlType.allToString().toArray(new String[0]));
		new Label(grpSetup, SWT.NONE);
		
		Label lblNumberOfSoftware = new Label(grpSetup, SWT.NONE);
		lblNumberOfSoftware.setSize(91, 15);
		lblNumberOfSoftware.setText("Software periods:");
		
		softwarePeriods = new Text(grpSetup, SWT.BORDER | SWT.RIGHT);
		GridData gd_softwarePeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_softwarePeriods.widthHint = 60;
		softwarePeriods.setLayoutData(gd_softwarePeriods);
		softwarePeriods.setSize(76, 23);
		
		Group periodsComposite = new Group(this, SWT.NONE);
		periodsComposite.setText("Hardware Periods");
		periodsComposite.setLayout(new GridLayout(1, false));
		periodsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_2 = new Composite(periodsComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(5, false));
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
		
		periods = new PeriodsTableView(periodsComposite, SWT.NONE);
		periods.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        periodFile.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setPeriodFile(periodFile.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
	}

	public void setModel(final PeriodsViewModel viewModel) {
        this.model = viewModel;

		bindingContext = new DataBindingContext();	
		
		bindingContext.bindList(WidgetProperties.items().observe(periodFile), BeanProperties.list("periodFilesList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(periodFileRB),
                BeanProperties.value("periodFile").observe(viewModel));

		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(setupSource), BeanProperties.value("setupSource").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(periodType), BeanProperties.value("periodType").observe(viewModel));
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
	}
	
	private void setPeriods(final List<Period> newPeriods) {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				periods.setPeriods(newPeriods);
			}
		});
	}
}

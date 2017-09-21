
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

package uk.ac.stfc.isis.ibex.ui.dae.run;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.widgets.LogMessageBox;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservingTextBox;

@SuppressWarnings("checkstyle:magicnumber")
public class RunSummary {

	private Label instrument;
	private Label runStatus;
	private Label runNumber;
	private Label isisCycle;
	private WritableObservingTextBox title;
    private Button btnDisplayTitle;
	private LogMessageBox messageBox;
	
	private DaeActionButtonPanel daeButtonPanel;
    private RunSummaryViewModel model;
	
    /**
     * Creates a view that shows a summary of the current run.
     */
    public RunSummary() {
        model = DaeUI.getDefault().viewModel().runSummary();
    }

    @PostConstruct
    public void createPart(Composite parent) {

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);
		
        Composite lhsComposite = new Composite(parent, SWT.NONE);
		lhsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        GridLayout gl = new GridLayout(1, false);
        gl.verticalSpacing = 25;
        lhsComposite.setLayout(gl);
        
        Composite infoComposite = new Composite(lhsComposite, SWT.NONE);
        infoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        infoComposite.setLayout(new GridLayout(5, false));

		Label lblInstrument = new Label(infoComposite, SWT.NONE);
		lblInstrument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInstrument.setText("Instrument:");
		
		instrument = new Label(infoComposite, SWT.NONE);
		GridData gdInstrument = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdInstrument.minimumWidth = 100;
		gdInstrument.widthHint = 100;
		instrument.setLayoutData(gdInstrument);
		instrument.setText("UNKNOWN");
		Label lblRunStatus = new Label(infoComposite, SWT.NONE);
		lblRunStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunStatus.setText("Run Status:");
		
		runStatus = new Label(infoComposite, SWT.NONE);
		GridData gdRunStatus = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gdRunStatus.widthHint = 100;
		gdRunStatus.minimumWidth = 100;
		runStatus.setLayoutData(gdRunStatus);
		runStatus.setText("UNKNOWN");
		
		Label spacer = new Label(infoComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblRunNumber = new Label(infoComposite, SWT.NONE);
		lblRunNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunNumber.setText("Run Number:");
		
		runNumber = new Label(infoComposite, SWT.NONE);
		GridData gdRunNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdRunNumber.minimumWidth = 100;
		gdRunNumber.widthHint = 100;
		runNumber.setLayoutData(gdRunNumber);
		runNumber.setText("UNKNOWN");
		
		Label lblIsisCycle = new Label(infoComposite, SWT.NONE);
		lblIsisCycle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIsisCycle.setText("ISIS Cycle:");
		
		isisCycle = new Label(infoComposite, SWT.NONE);
		GridData gdIsisCycle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdIsisCycle.minimumWidth = 100;
		gdIsisCycle.widthHint = 100;
		isisCycle.setLayoutData(gdIsisCycle);
		isisCycle.setText("UNKNOWN");
		
        Label spacer2 = new Label(infoComposite, SWT.NONE);
        spacer2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lblTitle = new Label(infoComposite, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText("Title:");
		
		title = new WritableObservingTextBox(infoComposite, SWT.NONE, model.title());
		title.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		GridData gdTitle = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gdTitle.widthHint = 180;
		title.setLayoutData(gdTitle);

        new Label(infoComposite, SWT.NONE);
        new Label(infoComposite, SWT.NONE);

        btnDisplayTitle = new Button(infoComposite, SWT.CHECK);
        btnDisplayTitle.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        btnDisplayTitle.setText("Show Title in Dataweb Dashboard Page");
        btnDisplayTitle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                model.displayTitle().uncheckedSetValue(btnDisplayTitle.getSelection());
            }
        });

		messageBox = new LogMessageBox(lhsComposite, SWT.NONE);
		messageBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
        daeButtonPanel = new DaeActionButtonPanel(parent, SWT.NONE, model.actions());
		daeButtonPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        setModel(model);
	}

    /**
     * Binds run model properties to GUI elements.
     * 
     * @param viewModel the model containing the run information
     */
    private void setModel(RunSummaryViewModel viewModel) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(instrument), BeanProperties.value("value").observe(viewModel.instrument()));
		bindingContext.bindValue(WidgetProperties.text().observe(runStatus), BeanProperties.value("value").observe(viewModel.runStatus()));
		bindingContext.bindValue(WidgetProperties.text().observe(runNumber), BeanProperties.value("value").observe(viewModel.runNumber()));
		bindingContext.bindValue(WidgetProperties.text().observe(isisCycle), BeanProperties.value("value").observe(viewModel.isisCycle()));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnDisplayTitle),
                BeanProperties.value("value").observe(viewModel.displayTitle().value()));
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDisplayTitle),
                BeanProperties.value("value").observe(viewModel.displayTitle().canSetValue()));

		messageBox.setModel(viewModel.logMessageSource());		
	}
}

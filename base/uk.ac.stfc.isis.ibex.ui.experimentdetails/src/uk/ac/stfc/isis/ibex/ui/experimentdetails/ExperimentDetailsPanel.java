
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

package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup.RBLookupDialog;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup.RBLookupViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservingTextBox;

@SuppressWarnings("checkstyle:magicnumber")
public class ExperimentDetailsPanel extends Composite {
	
	private final ViewModel viewModel = new ViewModel();
	
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
	private final ParametersTable sampleParameters;
	private final ParametersTable beamParameters;
	private final UserDetailsTable userDetails;
	private Label lblRbNumber;
	private Label lblSampleParameters;
	private Label lblBeamlineParameters;
	private Label lblExperimentTeam;
	private WritableObservingTextBox rbNumber;
	private Button btnUpdateUserDetails;
	private Button btnAddUserDetails;
	private Composite experimentTeamButtons;
	private Button btnClearUserDetails;
	private Button btnRemoveUserDetails;
	private Button btnRBLookup;
	
	public ExperimentDetailsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		
		lblRbNumber = new Label(composite, SWT.NONE);
		lblRbNumber.setText("RB Number");
		
		rbNumber = new WritableObservingTextBox(composite, SWT.NONE, viewModel.rbNumber);
		GridData gdRbNumber = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdRbNumber.minimumWidth = 100;
		gdRbNumber.widthHint = 100;
		rbNumber.setLayoutData(gdRbNumber);
		
		btnRBLookup = new Button(composite, SWT.NONE);
		btnRBLookup.setText("Search");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		btnRBLookup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RBLookupViewModel lookupViewModel = new RBLookupViewModel();
				RBLookupDialog lookupDialog = new RBLookupDialog(shell, lookupViewModel);
				if (lookupDialog.open() == Window.OK) {
					try {
                        viewModel.rbNumber.setText(lookupViewModel.getSelectedUser().getAssociatedExperimentID());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
				}
			}
		});
		
		lblExperimentTeam = new Label(composite, SWT.NONE);
		lblExperimentTeam.setText("Experiment Team");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		userDetails = new EditableUserDetailsTable(composite, SWT.NONE, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gdUserDetails = new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1);
		gdUserDetails.heightHint = 110;
		gdUserDetails.minimumHeight = 110;
        gdUserDetails.widthHint = 600;
        gdUserDetails.minimumWidth = 600;
		userDetails.setLayoutData(gdUserDetails);
		
		updateUserDetails();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateUserDetails();				
			}
		});
		
		experimentTeamButtons = new Composite(composite, SWT.NONE);
		experimentTeamButtons.setLayout(new GridLayout(1, false));
		
		GridData gdDetailsButtons = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gdDetailsButtons.widthHint = 50;
		
		btnAddUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnAddUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.addDefaultUser();
				viewModel.model.sendUserDetails();
			}
		});
		btnAddUserDetails.setText("Add");
		btnAddUserDetails.setLayoutData(gdDetailsButtons);

		btnRemoveUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnRemoveUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.removeUsers(userDetails.selectedRows());
                viewModel.model.sendUserDetails();
			}
		});		
		
		btnRemoveUserDetails.setText("Remove");
		btnRemoveUserDetails.setLayoutData(gdDetailsButtons);		
		
		btnClearUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnClearUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.clearUserDetails();
                viewModel.model.sendUserDetails();
			}
		});
		btnClearUserDetails.setText("Clear");
		btnClearUserDetails.setLayoutData(gdDetailsButtons);		
		
		btnUpdateUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnUpdateUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                viewModel.model.sendUserDetails();
			}
		});
        btnUpdateUserDetails.setText("Set");
		btnUpdateUserDetails.setLayoutData(gdDetailsButtons);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblSampleParameters = new Label(composite, SWT.NONE);
		lblSampleParameters.setText("Sample Parameters");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		sampleParameters = new ParametersTable(composite, SWT.NONE, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gdSampleParameters = new GridData(SWT.LEFT, SWT.FILL, false, true, 4, 1);
        gdSampleParameters.widthHint = 600;
        gdSampleParameters.minimumWidth = 600;
		gdSampleParameters.minimumHeight = 150;
		gdSampleParameters.heightHint = 150;
		sampleParameters.setLayoutData(gdSampleParameters);
		sampleParameters.enableEditing(viewModel.rbNumber.canSetText().getValue());
		
		updateSampleParameters();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateSampleParameters();
				
			}
		});
		new Label(composite, SWT.NONE);
		
		lblBeamlineParameters = new Label(composite, SWT.NONE);
		lblBeamlineParameters.setText("Beamline Parameters");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		beamParameters = new ParametersTable(composite, SWT.NONE, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		GridData gdBeamParameters = new GridData(SWT.LEFT, SWT.FILL, true, true, 4, 1);
        gdBeamParameters.widthHint = 600;
        gdBeamParameters.minimumWidth = 600;
		gdBeamParameters.minimumHeight = 200;
		gdBeamParameters.heightHint = 200;
		beamParameters.setLayoutData(gdBeamParameters);
		beamParameters.enableEditing(viewModel.rbNumber.canSetText().getValue());
		
		updateBeamParameters();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateBeamParameters();
			}
		});
	
		bind();
	}
	
	private void bind() {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnRBLookup), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnAddUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnClearUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnUpdateUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		
		viewModel.rbNumber.canSetText().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				boolean canSet = (boolean) event.getNewValue();
				beamParameters.enableEditing(canSet);
				sampleParameters.enableEditing(canSet);
			}
		});
	}
	
	private void updateUserDetails() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				userDetails.setRows(viewModel.model.getUserDetails());
			}	
		});
	}

	protected void updateSampleParameters() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				sampleParameters.setRows(viewModel.model.getSampleParameters());
			}	
		});
	}
	
	protected void updateBeamParameters() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				beamParameters.setRows(viewModel.model.getBeamParameters());
			}	
		});
	}
}


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
	
    private final ViewModel viewModel = ViewModel.getInstance();
	
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    private UserDetailsTable userDetails;
	private Label lblRbNumber;


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
		
        experimentTeam(composite);

        bind();
    }

    /**
     * @param parent
     */
    private void experimentTeam(Composite parent) {
        lblRbNumber = new Label(parent, SWT.NONE);
		lblRbNumber.setText("RB Number");
		
        rbNumber = new WritableObservingTextBox(parent, SWT.NONE, viewModel.rbNumber);
		GridData gdRbNumber = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdRbNumber.minimumWidth = 100;
		gdRbNumber.widthHint = 100;
		rbNumber.setLayoutData(gdRbNumber);
		
        btnRBLookup = new Button(parent, SWT.NONE);
		btnRBLookup.setText("Search");
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
		btnRBLookup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RBLookupViewModel lookupViewModel = new RBLookupViewModel();
				RBLookupDialog lookupDialog = new RBLookupDialog(shell, lookupViewModel);
				if (lookupDialog.open() == Window.OK) {
                    viewModel.rbNumber.uncheckedSetText(lookupViewModel.getSelectedUser().getAssociatedExperimentID());
				}
			}
		});
		
        lblExperimentTeam = new Label(parent, SWT.NONE);
		lblExperimentTeam.setText("Experiment Team");
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
		
        userDetails = new EditableUserDetailsTable(parent, SWT.NONE, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
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
		
        experimentTeamButtons = new Composite(parent, SWT.NONE);
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
		
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
    }
	
	private void bind() {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnRBLookup), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnAddUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnClearUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnUpdateUserDetails), BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
	}
	
	private void updateUserDetails() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				userDetails.setRows(viewModel.model.getUserDetails());
			}	
		});
	}
}

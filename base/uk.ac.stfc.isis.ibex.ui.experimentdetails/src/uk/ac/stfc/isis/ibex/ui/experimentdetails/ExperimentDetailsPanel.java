
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

import javax.inject.Inject;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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

import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup.RBLookupDialog;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup.RBLookupViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.HelpButton;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservingTextBox;

/**
 * A panel containing details of the experiment such as RB number and the experiment team.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ExperimentDetailsPanel extends ScrolledComposite {
	
    private static final String HIDDEN_USERS_TEXT = "The experiment details view is not available on this platform.";

	private static final String RB_NUM_INPUT_TIP_MESSAGE = "You can input your RB number for"
            + " your scheduled or Xpress run directly here!";
    
    private static final String MANUAL_USER_ENTRY_MESSAGE = "No users found! Please enter users "
            + "manually!";
    
    private final ExperimentDetailsViewModel viewModel = ExperimentDetailsViewModel.getInstance();
	
	private final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    private UserDetailsTable userDetails;
	private Label lblRbNumber;

	private Label lblExperimentTeam;
	private WritableObservingTextBox rbNumberTextBox;
	private Button btnSetRBNumber;
	private Button btnRBLookup;
	private Label manualUserEntryWarning;
	private Button btnAddUserDetails;
	private Composite experimentTeamButtons;
	private Button btnClearUserDetails;
	private Button btnRemoveUserDetails;
	private Button btnDisplayTitle;
	
	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Experiment-Details";
	private static final String DESCRIPTION = "Experiment Details View";
	
	@SuppressWarnings("unused")
	private HelpButton helpButton;
	
	/**
	 * Create an instance of this panel.
	 * @param parent the parent composite
	 */
    @Inject
    public ExperimentDetailsPanel(Composite parent) {
        super(parent, SWT.H_SCROLL | SWT.V_SCROLL);

        setMinSize(300, 300);
        setExpandHorizontal(true);
        setExpandVertical(true);
        setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));

        Composite parentComposite = new Composite(this, SWT.NONE);
        parentComposite.setLayout(new GridLayout(3, false));
        makeExperimentDetailsPanel(parentComposite);
        setContent(parentComposite);

        bind();
    }

    /**
     * Adds all the graphical elements such as labels and buttons to the parent 
     * composite of the panel.
     * @param parent The root composite of the panel.
     */
    private void makeExperimentDetailsPanel(Composite parent) {
    	if (Utils.SHOULD_HIDE_USER_INFORMATION) {
    		var label = new Label(parent, SWT.NONE);
    		label.setText(HIDDEN_USERS_TEXT);
    		return;
    	}
    	
        lblRbNumber = new Label(parent, SWT.NONE);
		lblRbNumber.setText("RB Number:");
		
        rbNumberTextBox = new WritableObservingTextBox(parent, SWT.NONE, viewModel.rbNumber);
        rbNumberTextBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        rbNumberTextBox.setToolTip(RB_NUM_INPUT_TIP_MESSAGE);
        
        btnRBLookup = new Button(parent, SWT.NONE);
		btnRBLookup.setText("Search");
        
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
		lblExperimentTeam.setText("Experiment Team:");
		
		manualUserEntryWarning = new Label(parent, SWT.NONE);
        manualUserEntryWarning.setText(MANUAL_USER_ENTRY_MESSAGE);
        manualUserEntryWarning.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
        manualUserEntryWarning.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
        manualUserEntryWarning.setVisible(false);
		
        userDetails = new EditableUserDetailsTable(parent, SWT.NONE, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
        userDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		updateUserDetails();
		viewModel.model.addPropertyChangeListener(propertyChangeEvent -> {
		    updateUserDetails();					
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
		
		btnSetRBNumber = new Button(experimentTeamButtons, SWT.NONE);
		btnSetRBNumber.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                viewModel.model.sendUserDetails();
			}
		});
		
        btnSetRBNumber.setText("Set");
		btnSetRBNumber.setLayoutData(gdDetailsButtons);
		btnDisplayTitle = new Button(experimentTeamButtons, SWT.CHECK);
		btnDisplayTitle.setText("Show Title and Users in Dataweb Dashboard Page");
		btnDisplayTitle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				viewModel.displayTitle.uncheckedSetValue(btnDisplayTitle.getSelection());
			}
		});
		
		helpButton = new HelpButton(parent, HELP_LINK, DESCRIPTION);
    }
	
	private void bind() {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnRBLookup), 
		        BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnAddUserDetails), 
		        BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnClearUserDetails), 
		        BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnSetRBNumber), 
		        BeanProperties.value("value").observe(viewModel.rbNumber.canSetText()));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnDisplayTitle),
				BeanProperties.value("value").observe(viewModel.displayTitle.value()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnDisplayTitle), 
		        BeanProperties.value("value").observe(viewModel.displayTitle.canSetValue()));
	    bindingContext.bindValue(WidgetProperties.visible().observe(manualUserEntryWarning), 
	            BeanProperties.value("userDetailsWarningVisible").observe(viewModel));
	}
	
	/**Gets information from model and updates the user details GUI table. Also
	 * makes the warning label visible or not based on whether the table is 
	 * empty or not.*/
	private void updateUserDetails() {
		Display.getDefault().asyncExec(() -> {
		    userDetails.setRows(viewModel.model.getUserDetails());
		});
	}
}

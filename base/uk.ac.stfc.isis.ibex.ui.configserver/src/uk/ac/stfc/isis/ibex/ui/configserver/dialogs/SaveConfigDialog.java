
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

package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;
import uk.ac.stfc.isis.ibex.validators.SummaryDescriptionValidator;

/**
 * Dialogue for saving a configuration.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SaveConfigDialog extends TitleAreaDialog {

	private static final String COMPONENT = "Component";
	private static final String CONFIGURATION = "Configuration";
	
	private Text txtName;	
	private Text txtDesc;		
	private Button btnAsComponent;
	
	private String newName = "";
	private String currentName = "";
	private String newDesc = "";
	private String currentDesc = "";
	private List<String> existingConfigs;
	private List<String> existingComponents;
	private boolean isConfig;

	private boolean hasComponents;
	private boolean asComponent = false;
	
    /**
     * The name of the current configuration.
     */
    private final String currentConfigName;

    /**
     * Instantiates a new save configuration dialog.
     *
     * @param parent the parent
     * @param currentName the original name of the config if editing an existing
     *            one, otherwise blank
     * @param currentDesc the original description of the config if editing an
     *            existing one, otherwise blank
     * @param existingConfigs the existing configuration names (so not to
     *            duplicate)
     * @param existingComponents the existing component names (so not to
     *            duplicate)
     * @param isConfig current configuration
     * @param hasComponents true if this configuration has components; false
     *            otherwise
     * @param currentConfigName The name of the current configuration
     */
	public SaveConfigDialog(
			Shell parent, 
			String currentName, 
			String currentDesc,
			Collection<String> existingConfigs, 
			Collection<String> existingComponents, 
			boolean isConfig,
            boolean hasComponents, String currentConfigName) {
		super(parent);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.currentName = currentName;
		this.currentDesc = currentDesc;
		this.existingConfigs = new ArrayList<>(existingConfigs);
		this.existingComponents = new ArrayList<>(existingComponents);
		this.isConfig = isConfig;
		this.hasComponents = hasComponents;
        this.currentConfigName = currentConfigName;
	}
	
    /**
     * @return the new name set in this dialogue
     */
	public String getNewName() {
		return newName;
	}
	
    /**
     * @return the new description set
     */
	public String getNewDescription() {
		return newDesc;
	}
	
    /**
     * @return true, if when saved this will be a component; false otherwise
     */
	public boolean willBeComponent() {
		return !isConfig || asComponent;
	}
	
	@Override
	protected void configureShell(Shell shell) {
	      super.configureShell(shell);
	      shell.setText(String.format("Save %s As", getTypeName()));
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {		
		setTitle(String.format("Save %s", getTypeName()));
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout glContainer = new GridLayout(1, false);
		container.setLayout(glContainer);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gdComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gdComposite.widthHint = 95;
		composite.setLayoutData(gdComposite);
		
		Label lblConfigurationName = new Label(composite, SWT.NONE);
		lblConfigurationName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfigurationName.setText("Name:");
		
		txtName = new Text(composite, SWT.BORDER);
		GridData gdTxtName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gdTxtName.widthHint = 383;
		txtName.setLayoutData(gdTxtName);
		txtName.setBounds(0, 0, 76, 21);
		new Label(composite, SWT.NONE);
		
		Label lblConfigurationNameCannot = new Label(composite, SWT.NONE);
		lblConfigurationNameCannot.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblConfigurationNameCannot.setText("Name must start with a letter and contain only [A-Z], [0-9] and \"_\"");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblConfigurationDesc = new Label(composite, SWT.NONE);
		lblConfigurationDesc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfigurationDesc.setText("Description:");
		
		txtDesc = new Text(composite, SWT.BORDER);
		GridData gdTxtDesc = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gdTxtDesc.widthHint = 383;
		txtDesc.setLayoutData(gdTxtDesc);
		txtDesc.setBounds(0, 0, 76, 21);
		new Label(composite, SWT.NONE);		
		new Label(composite, SWT.NONE);
		
		if (isConfig) { 
			btnAsComponent = new Button(composite, SWT.CHECK);
			btnAsComponent.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			btnAsComponent.setText("Save the configuration as a component");
			btnAsComponent.setVisible(true);
		
			new Label(composite, SWT.NONE);
			btnAsComponent.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateConfigType();
					update();
				}
			});
		}
	
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				update();
			}
		});
		
		txtDesc.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				update();
			}
		});
		
		if (isConfig) {
			txtName.setText(currentName);
		}
		
		txtDesc.setText(currentDesc);
		
		update();
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, 
				IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

        // after creating buttons update their state
        update();
	}
	
	@Override
	  protected void okPressed() {
		if (validate(name())) {
			newName = name();
			newDesc = description();
            // Warn about overwriting if already exists
            if (isDuplicate(newName)) {
                boolean userCancelled = askUserWhetherToOverwrite();
                if (userCancelled) {
                    return;
				}
            }
			super.okPressed();
			close();
		}
		// else ignore the click
	}

	private boolean askUserWhetherToOverwrite() {
		MessageBox box = new MessageBox(getShell(), SWT.YES | SWT.NO);
		box.setMessage(String.format("The specified %s name already exists - do you want to replace it? ", getTypeName().toLowerCase()));
		
		//Message boxes return the ID of the button to close, so need to check that value..
		return box.open() != SWT.YES;
	}
	
	private String name() {
		return txtName.getText().trim();
	}
	
	private String description() {
		return txtDesc.getText().trim();
	}
	
	private String getTypeName() {
		return willBeComponent() ? COMPONENT : CONFIGURATION;
	}
		
	private void update() {
		checkInput(name(), description());
	}
	
	private void updateConfigType() {
		asComponent = btnAsComponent.getSelection();
		setTitle(String.format("Save %s", getTypeName()));
	}
	
	private void allowSaving(boolean canSave) {
		Button ok = getButton(IDialogConstants.OK_ID);
		if (ok != null) {
			ok.setEnabled(canSave);
		}
	}
	
	private boolean savingComponentWithComponents() {
		return willBeComponent() && hasComponents;
	}
	
	private boolean isDuplicate(final String name) {
		List<String> prexisting = willBeComponent() ? existingComponents : existingConfigs;	
		return Iterables.any(prexisting, new Predicate<String>() {
			@Override
			public boolean apply(String existing) {
				return compareIgnoringCase(existing, name);
			}
		});
	}

	private boolean compareIgnoringCase(String text, String other) {
		return text.toUpperCase().equals(other.toUpperCase());
	}
	
	private Boolean validate(String name) {		
		//Must start with a letter and contain no spaces	
		return name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
	}

	private void checkInput(String name, String desc) {
		setErrorMessage(null);
		setMessage(null);
		
		String error = getErrorMessage(name, desc);
		if (error.length() > 0) {
			setErrorMessage(error);
			allowSaving(false);
			return;
		}
		
		allowSaving(true);
		String warning = getWarningMessage(name);
		if (warning.length() > 0) {
			setMessage(warning);
		}
	}
	
	private String getErrorMessage(String name, String description) {
		if (savingComponentWithComponents()) {
			return "To be saved as a component, the configuration must have no components of its own.";
		}
		
		if (name.length() == 0) {
			return "Name cannot be blank";
		}
		
		if (!validate(name)) {
			return "Name contains invalid characters";
		}
		
        if (nameMatchesCurrentConfig(name)) {
            return "Name matches the current configuration";
        }

        BlockServerNameValidator configDescritpionRules =
                Configurations.getInstance().variables().configDescriptionRules.getValue();
        SummaryDescriptionValidator descriptionValidator =
                new SummaryDescriptionValidator(null, configDescritpionRules);
		IStatus status = descriptionValidator.validate(description);
		if (!status.isOK()) {
		    return status.getMessage();
		}
		
		return "";
	}

	private String getWarningMessage(String name) {	
		if (isDuplicate(name)) {
			return String.format("A %s with this name already exists", getTypeName().toLowerCase());
		}
		
		return "";
	}

    /**
     * @return The requested name matches that of the current configuration
     */
	private boolean nameMatchesCurrentConfig(String name) {
        return compareIgnoringCase(name, currentConfigName);
	}
}

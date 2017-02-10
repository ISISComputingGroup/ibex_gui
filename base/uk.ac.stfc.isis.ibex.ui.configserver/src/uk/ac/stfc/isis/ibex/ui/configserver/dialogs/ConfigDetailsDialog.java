
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.ConfigEditorPanel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog for viewing more details of a configuration.
 */
public class ConfigDetailsDialog extends TitleAreaDialog implements
		MessageDisplayer {

	private static final Point INITIAL_SIZE = new Point(650, 750);
	private final String title;
	private final String subTitle;

    /** Error messages that are displayed. <Source, message> */
	private Map<String, String> errorMessages = new HashMap<String, String>();

	protected EditableConfiguration config;

	private ConfigEditorPanel editor;
	protected boolean doAsComponent = false;
	protected boolean isBlank;

    private ConfigurationViewModels configurationViewModels;

    /**
     * @wbp.parser.constructor Constructor
     * 
     * @param parentShell parent shell to run dialogue
     * @param title title of dialogue
     * @param subTitle action being taken, e.g. editing current configuration
     * @param config configuration being edited
     * @param isBlank is blank
     * @param configurationViewModels view model for the configuration
     */
    public ConfigDetailsDialog(Shell parentShell, String title, String subTitle, EditableConfiguration config,
            boolean isBlank, ConfigurationViewModels configurationViewModels) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.subTitle = subTitle;
		this.config = config;
		this.isBlank = isBlank;
        this.configurationViewModels = configurationViewModels;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
        editor = new ConfigEditorPanel(parent, SWT.NONE, this, config.isComponent(), configurationViewModels);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setTitle(subTitle);
        editor.setConfigToEdit(config);
		return editor;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}

	@Override
	public void setErrorMessage(String source, String error) {
		errorMessages.put(source, error);
		showErrorMessage();
	}

    /**
     * Show the current error messages.
     */
	protected void showErrorMessage() {
		StringBuilder sb = new StringBuilder();
		for (String key : errorMessages.keySet()) {
			if (errorMessages.get(key) != null) {
				sb.append(errorMessages.get(key));
				sb.append("  ");
			}
		}

		if (sb.length() > 0) {
			setErrorMessage(sb.toString());
			// Don't allow save until errors are cleared
		} else {
			setErrorMessage(null);
		}
	}

    /**
     * 
     * @return the configuration
     */
	public Configuration getConfig() {
		return config.asConfiguration();
	}

    /**
     * 
     * @return the component being edited
     */
	public Configuration getComponent() {
		return config.asComponent();
	}

    /**
     * 
     * @return whether dialogue is for editing a component
     */
	public boolean doAsComponent() {
		return doAsComponent;
	}
    
    @Override
	protected void createButtonsForButtonBar(Composite parent) {
    	// Only create the Cancel button
    	createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
    }
}

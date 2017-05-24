
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

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;

/**
 * Dialog for editing a configuration (top dialogue that contains save and save
 * as buttons).
 */
public class EditConfigDialog extends ConfigDetailsDialog {

	private Button saveAsBtn;
    private boolean editBlockFirst;
	
    private ConfigServer server = Configurations.getInstance().server();

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
    public EditConfigDialog(Shell parentShell, String title, String subTitle, EditableConfiguration config,
            boolean isBlank, ConfigurationViewModels configurationViewModels, boolean editBlockFirst) {
        super(parentShell, title, subTitle, config, isBlank, configurationViewModels);
        this.editBlockFirst = editBlockFirst;
	}

    @Override
    protected Control createDialogArea(Composite parent) {
        // We need to create the dialog first before selecting the blocks tab
        Control control = super.createDialogArea(parent);
        if (editBlockFirst) {
            selectBlocksTab();
        }
        return control;
    }

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (!isBlank && !this.config.getName().isEmpty()) { 
            createButton(parent, IDialogConstants.OK_ID, "Save", true);
		}

		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                Collection<String> configNames = server.configNames();
                Collection<String> componentNames = server.componentNames();
				boolean hasComponents = !config.getEditableComponents()
						.getSelected().isEmpty();
                String currentConfigName = server.currentConfig().getValue().name();
				SaveConfigDialog dlg = new SaveConfigDialog(null, config
						.getName(), config.getDescription(), configNames,
                        componentNames, !config.getIsComponent(), hasComponents, currentConfigName);
				if (dlg.open() == Window.OK) {
					if (dlg.getNewName() != config.getName()) {
						config.setName(dlg.getNewName());
					}

					config.setDescription(dlg.getNewDescription());
					doAsComponent = dlg.willBeComponent();

					okPressed();
				}
			}
		});

		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);

        // Show any error message and set buttons after creating those buttons
        super.showErrorMessage();
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		if (newErrorMessage == null) {
			setOKEnabled(true);
			saveAsBtn.setEnabled(true);
		} else {
			setOKEnabled(false);
			saveAsBtn.setEnabled(false);
		}
		super.setErrorMessage(newErrorMessage);
	}

    public void selectBlocksTab() {
        editor.selectBlocksTab();
    }

	private void setOKEnabled(boolean value) {
		Button okButton = getButton(IDialogConstants.OK_ID);
		if (okButton != null) {
			okButton.setEnabled(value);
		}
	}
}

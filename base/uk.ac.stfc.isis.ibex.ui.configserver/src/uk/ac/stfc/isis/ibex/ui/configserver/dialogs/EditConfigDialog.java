
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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
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
import uk.ac.stfc.isis.ibex.configserver.editing.BlockFactory;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.EditBlockDialog;
import uk.ac.stfc.isis.ibex.ui.widgets.HelpButton;

/**
 * Dialog for editing a configuration (top dialogue that contains save and save
 * as buttons).
 */
public class EditConfigDialog extends ConfigDetailsDialog {

	private Button saveAsBtn;
	private Button saveButton;
	private Button showMoxasButton;
	private boolean editBlockFirst;
	private boolean switchConfigOnSaveAs;
	private boolean calledSwitchConfigOnSaveAs = false;
	private String pvName;
	private ConfigServer server = Configurations.getInstance().server();
	
	private static final String CONFIG_HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Create-And-Manage-Configurations";
	private static final String COMPONENT_HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Create-and-Manage-Components";
	private final String description;
	private MoxaDetailsDialog moxaDetailsDialog;
	/**
	 * Constructor.
	 * 
	 * @param parentShell             parent shell to run dialogue
	 * @param title                   title of dialogue
	 * @param subTitle                action being taken, e.g. editing current
	 *                                configuration
	 * @param config                  configuration being edited
	 * @param isBlank                 is blank
	 * @param configurationViewModels view model for the configuration
	 * @param editBlockFirst          Open the dialog with blocks tab open
	 */
	public EditConfigDialog(Shell parentShell, String title, String subTitle, EditableConfiguration config,
			boolean isBlank, ConfigurationViewModels configurationViewModels, boolean editBlockFirst) {
		super(parentShell, title, subTitle, config, isBlank, configurationViewModels);
		this.editBlockFirst = editBlockFirst;
		this.description = title;
	}

	/**
	 * Creates a new edit-configuration dialog.
	 * @param parentShell the parent shell
	 * @param title the title
	 * @param subTitle the subtitle
	 * @param config the config to edit
	 * @param isBlank whether this is a blank config
	 * @param configurationViewModels the viewModel
	 * @param editBlockFirst whether we need to edit a block first
	 * @param openEditBlockDialog whether we need to open the edit block dialog
	 * @param pvName a PV name
	 */
	public EditConfigDialog(Shell parentShell, String title, String subTitle, EditableConfiguration config,
			boolean isBlank, ConfigurationViewModels configurationViewModels, boolean editBlockFirst,
			boolean openEditBlockDialog, String pvName) {
		super(parentShell, title, subTitle, config, isBlank, configurationViewModels);
		this.editBlockFirst = editBlockFirst;
		this.pvName = pvName;
		this.description = title;

	}

	/**
	 * Add a new block to this config.
	 * @param config the editable configuration
	 * @throws IOException on failure
	 */
	public void addNew(EditableConfiguration config) throws IOException {
		BlockFactory blockFactory = new BlockFactory(config);
		EditableBlock added = blockFactory.createNewBlock(Optional.of(pvName));
		EditBlockDialog dialog = new EditBlockDialog(getShell(), added, config);
		dialog.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// We need to create the dialog first before selecting the blocks tab
		Control control = super.createDialogArea(parent);
		if (editBlockFirst) {
			selectBlocksTab();
		}
		
		// Set link according to whether this is a component or a config.
		if (!config.getIsComponent()) {
			new HelpButton(parent, CONFIG_HELP_LINK, description);
		} else {
			new HelpButton(parent, COMPONENT_HELP_LINK, description);
		}
		
		this.moxaDetailsDialog = new MoxaDetailsDialog(this.getShell());
		
		return control;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		showMoxasButton = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Show Moxa ports", false);
		showMoxasButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("moxas button hit");
				
				if (moxaDetailsDialog.open() == Window.OK) {
					
				}
			}
		});	
		
		if (!isBlank && !this.config.getName().isEmpty()) {
			saveButton = createButton(parent, IDialogConstants.OK_ID, "Save", true);
		}

		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String, Boolean> configNamesFlags = server.configNamesWithFlags();
				Map<String, Boolean> componentsNamesFlags = server.compNamesWithFlags();

				Collection<String> configNames = server.configNames();
				Collection<String> componentNames = server.componentNames();

				boolean hasComponents = !config.getEditableComponents().getSelected().isEmpty();
				String currentConfigName = server.currentConfig().getValue().name();

				SaveConfigDialog dlg = new SaveConfigDialog(null, config.getName(), config.getDescription(),
						configNames, componentNames, !config.getIsComponent(), hasComponents, currentConfigName,
						configNamesFlags, componentsNamesFlags);
				if (dlg.open() == Window.OK) {
					switchConfigOnSaveAs = dlg.shouldSwitchConfig();
					calledSwitchConfigOnSaveAs = true;
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
		bind();
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

	private void selectBlocksTab() {
		editor.selectBlocksTab();
	}

	private void setOKEnabled(boolean value) {
		Button okButton = getButton(IDialogConstants.OK_ID);
		if (okButton != null) {
			okButton.setEnabled(value);
		}
	}

	/**
	 * 
	 * @return true if we should switch to the new config
	 */
	public boolean switchConfigOnSaveAs() {
		return switchConfigOnSaveAs;
	}

	/**
	 *
	 * @return true if switchConfigOnSaveAs() has been called.
	 */
	public boolean calledSwitchConfigOnSaveAs() {
		return calledSwitchConfigOnSaveAs;
	}

	/**
	 * Binding for save and save as button.
	 */
	public void bind() {
		DataBindingContext bindingContext = new DataBindingContext();
		if (saveButton != null) {
			bindingContext.bindValue(WidgetProperties.enabled().observe(saveButton),
					BeanProperties.value("enableOrDisableSaveButton").observe(config));
		}

		bindingContext.bindValue(WidgetProperties.enabled().observe(saveAsBtn),
				BeanProperties.value("enableSaveAsButton").observe(config));

	}

}

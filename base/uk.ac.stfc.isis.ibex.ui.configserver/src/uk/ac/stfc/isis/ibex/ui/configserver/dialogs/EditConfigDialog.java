
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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.ConfigEditorPanel;

public class EditConfigDialog extends TitleAreaDialog implements
		MessageDisplayer {

	private static final Point INITIAL_SIZE = new Point(650, 750);
	private final String title;
	private final String subTitle;

	private Map<String, String> errorMessages = new HashMap<String, String>();

	private EditableConfiguration config;

	private ConfigEditorPanel editor;
	private boolean doSaveAs = false;
	private boolean doAsComponent = false;
	private boolean isComponent;
	private boolean isBlank;
	private boolean firstShowing = true;
    private String blockToEdit;
	private Button saveAsBtn;

	public EditConfigDialog(Shell parentShell, String title, String subTitle,
			EditableConfiguration config, boolean isComponent, boolean isBlank) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
		this.title = title;
		this.subTitle = subTitle;
		this.config = config;
		this.isComponent = isComponent;
		this.isBlank = isBlank;
	}

    public EditConfigDialog(Shell parentShell, String title, String subTitle, EditableConfiguration config,
            boolean isComponent, boolean isBlank, String blockName) {
        this(parentShell, title, subTitle, config, isComponent, isBlank);
        this.blockToEdit = blockName;
        
    }

	@Override
	protected Control createDialogArea(Composite parent) {
		editor = new ConfigEditorPanel(parent, SWT.NONE, this, isComponent);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setTitle(subTitle);
		editor.setConfigToEdit(config);
		
        if (!Strings.isNullOrEmpty(blockToEdit)) {
	        getShell().addShellListener(new ShellAdapter() {
				@Override
				public void shellActivated(ShellEvent e) {
					if (firstShowing) {
						Shell s = (Shell) e.getSource();
	                    s.setVisible(true);
			            openBlocksTab();
			            openEditBlockDialog();
			            firstShowing = false;
					}
				}
			});
        }
		
		return editor;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (isBlank == false && !this.config.getName().isEmpty()){ 
			createButton(parent, IDialogConstants.OK_ID, "Save", true);
		}
        
		saveAsBtn = createButton(parent, IDialogConstants.CLIENT_ID + 1, "Save as ...", false);
		saveAsBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Collection<String> configNames = Configurations.getInstance()
						.server().configNames();
				Collection<String> componentNames = Configurations
						.getInstance().server().componentNames();
				boolean hasComponents = !config.getEditableComponents()
						.getSelected().isEmpty();
				SaveConfigDialog dlg = new SaveConfigDialog(null, config
						.getName(), config.getDescription(), configNames,
						componentNames, !isComponent, hasComponents);
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
        openEditBlockDialog();
	}

	// Show the current error messages
	private void showErrorMessage() {
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
			setOKEnabled(false);
			saveAsBtn.setEnabled(false);
		} else {
			setErrorMessage(null);
			setOKEnabled(true);
			saveAsBtn.setEnabled(true);
		}
	}

	private void setOKEnabled(boolean value) {
		Button okButton = getButton(IDialogConstants.OK_ID);
		if (okButton != null) {
			okButton.setEnabled(value);
		}
	}

	public Configuration getConfig() {
		return config.asConfiguration();
	}

	public Configuration getComponent() {
		return config.asComponent();
	}

	public boolean doSaveAs() {
		return doSaveAs;
	}

	public boolean doAsComponent() {
		return doAsComponent;
	}

    public void openBlocksTab() {
        editor.openTab(ConfigEditorPanel.BLOCK_TAB_NAME);
    }

    public void openEditBlockDialog() {
        editor.openEditBlockDialog(this.blockToEdit);
    }
}

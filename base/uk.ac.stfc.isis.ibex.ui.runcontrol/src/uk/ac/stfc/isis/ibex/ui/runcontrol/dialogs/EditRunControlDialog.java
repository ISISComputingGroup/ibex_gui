
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

package uk.ac.stfc.isis.ibex.ui.runcontrol.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.runcontrol.RunControlViewModel;
import uk.ac.stfc.isis.ibex.ui.runcontrol.commands.RunControlHandler;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

/**
 * A dialog for editing the run control.
 */
public class EditRunControlDialog extends TitleAreaDialog {

	private static final Point INITIAL_SIZE = new Point(1000, 500);
	private final String title;
	private final ConfigServer configServer;
	private final RunControlServer runControlServer;
	private RunControlSettingsPanel editor;
	private final RunControlViewModel viewModel;
	private RunControlHandler handler;
	
	/**
	 * Creates a dialog for configuring the run-control settings.
	 * 
	 * @param parentShell the parent SWT Shell
	 * @param title the title of the dialog window
	 * @param configServer the Config Server 
	 * @param runControlServer the Run Control server
	 */
	public EditRunControlDialog(Shell parentShell, String title, ConfigServer configServer, RunControlServer runControlServer) {
		super(parentShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.title = title;
		this.configServer = configServer;
		this.runControlServer = runControlServer;
        this.viewModel =
                new RunControlViewModel(Configurations.getInstance().display().getDisplayBlocks(), runControlServer);
		viewModel.addPropertyChangeListener("error", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ErrorMessage newError = (ErrorMessage) evt.getNewValue();
				if (newError.isError()) {
					setErrorMessage(newError.getMessage());
				} else {
					setErrorMessage(null);
				}
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
        setTitle("Configure Run Control");
		
		editor = new RunControlSettingsPanel(this, parent, SWT.NONE, configServer, runControlServer, viewModel);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return editor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}

}

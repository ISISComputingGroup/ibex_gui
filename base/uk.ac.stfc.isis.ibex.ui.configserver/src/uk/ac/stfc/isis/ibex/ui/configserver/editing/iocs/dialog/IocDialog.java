/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonFactory;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog window for adding and editing IOCs.
 */
public class IocDialog extends TitleAreaDialog implements MessageDisplayer {
	/**
	 * The configuration to be edited.
	 */
	protected EditableConfiguration config;

	/**
	 * Button to return to previous view.
	 */
	protected Button btnPrev;

	/**
	 * Button to complete adding Ioc.
	 */
	protected Button btnOk;

	/**
	 * The composite to hold the content to display.
	 */
	protected Composite content;

	/**
	 * The panel for editing an Ioc.
	 */
	protected EditPanel editIocPanel;

	/**
	 * A temporarily editable Ioc.
	 */
	protected TempEditableIoc tempIoc;

	/** Error messages that are displayed. <Source, message> */
	protected Map<String, String> errorMessages = new HashMap<String, String>();

	/**
	 * Whether the current Ioc is editable.
	 */
	protected final boolean readOnly;

	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Create-And-Manage-Configurations#iocs-tab";
	private static final String DESCRIPTION = "Add/Edit IOC dialog";

	/**
	 * The current display.
	 */
	protected static final Display DISPLAY = Display.getCurrent();
	private static final Point INITIAL_SIZE = new Point(1500, 670);
	private static final Point MINIMUM_SIZE = new Point(450, 400);

	/**
	 * Constructor for the IOC dialog.
	 * 
	 * @param parent The parent composite.
	 * @param config The configuration currently being edited.
	 * @param ioc    The IOC to add.
	 */
	public IocDialog(Shell parent, EditableConfiguration config, EditableIoc ioc) {
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.config = config;
		this.tempIoc = new TempEditableIoc(ioc);
		this.readOnly = !ioc.isEditable();
	}

	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setMinimumSize(MINIMUM_SIZE);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// Only edit page is relevant if pre-existing IOC.
		btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		btnOk.setFocus();
		if (readOnly) {
			btnOk.setVisible(false);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		content.setLayout(new GridLayout(1, false));

		editIocPanel = new EditPanel(content, SWT.NONE, this);
		editIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		editIocPanel.setIOC(tempIoc);

		String titlePrefix = readOnly ? "View" : "Edit";
		this.setTitle(titlePrefix + " IOC: " + tempIoc.getName());

		content.layout();

		IBEXButtonFactory.helpButton(parent, HELP_LINK, DESCRIPTION);

		return editIocPanel;
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
			btnOk.setEnabled(false);
		} else {
			setErrorMessage(null);
			btnOk.setEnabled(true);
		}
	}

	/**
	 * Update IOC and add to configuration when confirming changes.
	 */
	@Override
	protected void okPressed() {
		tempIoc.saveIoc();
		super.okPressed();
	}
}

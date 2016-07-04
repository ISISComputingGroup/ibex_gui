
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog for creating a new script to send to the script server.
 */
public class CreateScriptDialog extends Dialog {

	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	private CreateScriptPanel creator;
    private Button sendBtn;

	/**
	 * The constructor for this class.
	 * @param parentShell The shell that this dialog is created from.
	 */
    public CreateScriptDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        creator = new CreateScriptPanel(parent, SWT.NONE);
        creator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return creator;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        sendBtn = createButton(parent, IDialogConstants.OK_ID, "Send", false);
        sendBtn.addSelectionListener(new SelectionAdapter() {
        	//TODO
        });
		
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}	
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Create Script");
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}


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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.IocControl;

/**
 * Class to display a dialog to start/stop IOCs
 */
public class IocControlDialog extends TitleAreaDialog {

	private static final String MAIN_TITLE = "Control IOCs";
	private static final String SUB_TITLE = "Start or stop IOCs";
	
	private static final Point INITIAL_SIZE = new Point(500, 600);
	
	private final IocControl control;

	public IocControlDialog(Shell parentShell, IocControl control) {
		super(parentShell);
		this.control = control;
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(MAIN_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(SUB_TITLE);

		IocPanel iocPanel = new IocPanel(parent, control, SWT.NONE);
		iocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return iocPanel;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}

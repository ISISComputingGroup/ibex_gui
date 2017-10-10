
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

package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog box that contains information about the IBEX client and server
 * versions.
 */
public class AboutDialogBox extends TitleAreaDialog {

    /** Dialog width. */
    public static final int WIDTH = 300;
    /** Dialog height. */
    private static final int HEIGHT = 240;

    /**
     * Construct a new about Ibex dialog box.
     * 
     * @param parentShell The parent shell in which in the dialog will be loaded
     */
	public AboutDialogBox(Shell parentShell) {
		super(parentShell);		
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setTitle("About IBEX");
		
	}
	
	@Override
	protected Point getInitialSize() {
        return new Point(WIDTH, HEIGHT);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("About IBEX");
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
			
		new VersionPanel(container, SWT.NONE);

		return container;
	}	

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID) {
			return null;
		}
		return super.createButton(parent, id, label, defaultButton);
	}
}

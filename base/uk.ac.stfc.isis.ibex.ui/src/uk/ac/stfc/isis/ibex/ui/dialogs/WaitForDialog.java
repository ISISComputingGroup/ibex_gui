
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

package uk.ac.stfc.isis.ibex.ui.dialogs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.e4.ui.services.IServiceConstants;

/**
 * The Class WaitForDialog which shows a dialogue which indicates the GUI is waiting for the instrument.
 */
public class WaitForDialog extends Dialog {
	
	private static final Point INITIAL_SIZE = new Point(250, 100);
	
	/**
	 * Instantiates a new wait for dialog.
	 *
	 * @param parentShell the parent shell
	 */
	@Inject
	public WaitForDialog(@Named (IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		super(parentShell);
	    super.setShellStyle(SWT.ALPHA);
	}

	@Override
	protected Control createDialogArea(Composite parent) {	
		Composite panel = new WaitPanel(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return panel;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// No buttons
	}
	
	@Override
	public int open() {
		
		Shell parentShell = getParentShell();
		if (parentShell != null) {
			parentShell.setEnabled(false);
		}
		return super.open();
	}
	
	@Override
	public boolean close() {
		Shell parentShell = getParentShell();
		if (parentShell != null) {
			getParentShell().setEnabled(true);
		}
		return super.close();
	}

	/**
	 * Sets the cursor.
	 *
	 * @param cursorType the new cursor
	 */
	public void setCursor(int cursorType) {
		Cursor cursor = Display.getDefault().getSystemCursor(cursorType);
		
		Shell activeShell = getParentShell();
		if (activeShell != null) {
			activeShell.setCursor(cursor);
		}
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}

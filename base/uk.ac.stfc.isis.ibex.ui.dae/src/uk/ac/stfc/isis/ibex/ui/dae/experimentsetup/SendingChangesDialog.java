
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("checkstyle:magicnumber")
public class SendingChangesDialog extends Dialog {

	private int maxSeconds;

	/**
     * Create the dialog.
     * 
     * @param parentShell
     *            The shell of the parent dialog
     * @param secondsToShowFor
     *            The maximum number of seconds to show the dialog for
     */
	public SendingChangesDialog(Shell parentShell, int secondsToShowFor) {
		super(parentShell);
		this.maxSeconds = secondsToShowFor;
		setBlockOnOpen(true);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		final Display display = container.getShell().getDisplay();
		final Label lblSendingChanges = new Label(container, SWT.NONE);
		lblSendingChanges.setText("Sending Changes...");
		
		// Set a timer going to dismiss this after a certain time
		final Runnable timer = new Runnable() {
			private int counter = 0;
			private final int oneSecondInMilliseconds = 1000;
			
			@Override
            public void run() {
				if (!lblSendingChanges.isDisposed()) {
					if (counter >= maxSeconds) {
						SendingChangesDialog.this.close();
					} else {
						display.timerExec(oneSecondInMilliseconds, this);
						counter++;
					}
				}
			}
		};
		
		display.timerExec(0, timer);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		button.setText("Dismiss");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(184, 120);
	}
}

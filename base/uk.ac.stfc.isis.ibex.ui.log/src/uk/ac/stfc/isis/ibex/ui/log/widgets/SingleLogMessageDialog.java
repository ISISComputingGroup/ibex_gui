
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
package uk.ac.stfc.isis.ibex.ui.log.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

@SuppressWarnings("checkstyle:magicnumber")
public class SingleLogMessageDialog extends Dialog {
	/**
	 * Ordered list of fields to display in the dialog. Doesn't include contents
	 * as that is treated differently.
	 */
	private static final LogMessageFields[] FIELDS = {LogMessageFields.CLIENT_NAME, 
			LogMessageFields.CLIENT_HOST, LogMessageFields.APPLICATION_ID,
			LogMessageFields.SEVERITY, LogMessageFields.TYPE,
			LogMessageFields.EVENT_TIME, LogMessageFields.CREATE_TIME, };

	private LogMessage message;

	public SingleLogMessageDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public SingleLogMessageDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		setText("Log Message Detail");
	}

	public void setLogMessage(LogMessage message) {
		this.message = message;
	}

	public void open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());

		createContents(shell);

		shell.pack();
		shell.open();

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));

		// Set layout data
		GridData lblData = new GridData(GridData.FILL_BOTH);
		lblData.widthHint = 80;
		GridData txtData = new GridData(GridData.FILL_HORIZONTAL);
		txtData.widthHint = 500;

		// Create message Contents field
		Label lblContents = new Label(shell, SWT.RIGHT);
		lblContents.setText("Message");
		lblContents.setLayoutData(lblData);

		GridData txtContentsData = new GridData(GridData.FILL_HORIZONTAL);
		txtContentsData.widthHint = 500;
		txtContentsData.heightHint = 100;

		Text txtContents = new Text(shell, SWT.BORDER | SWT.MULTI
				| SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		txtContents.setLayoutData(txtContentsData);

		String contents = message.getProperty(LogMessageFields.CONTENTS);
		if (contents != null) {
			txtContents.setText(contents);
		}

		// Create all other message fields
		for (LogMessageFields field : FIELDS) {
			Label label = new Label(shell, SWT.RIGHT);
			label.setText(field.toString());
			label.setLayoutData(lblData);

			Text text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
			text.setLayoutData(txtData);

			String value = message.getProperty(field);
			if (value != null) {
				text.setText(value);
			}
		}
	}
}

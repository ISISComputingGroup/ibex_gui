
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
package uk.ac.stfc.isis.ibex.ui.logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.logger.IRecentLog;

public class LoggerDialog extends TitleAreaDialog {
	private Text text;
	private IRecentLog model;

	public LoggerDialog(Shell parentShell, IRecentLog model) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.model = model;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Console");
	}

	@Override
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	protected Control createDialogArea(Composite parent) {
		setTitle("Console Log");
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
		container.setLayout(gl_container);

		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.minimumWidth = 800;
		gd_text.minimumHeight = 600;
		text.setLayoutData(gd_text);

		text.setText("Loading recent log messages...");

		// Load recent log messages
		Job logJob = new Job("Log Message Loader") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				setText(model.getLogText());
				return Status.OK_STATUS;
			}
		};
		logJob.schedule();

		return container;
	}

	private void setText(final String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				text.setText(str);
			}
		});
	}

	/**
	 * Override to get rid of the cancel button
	 */
	@Override
	public void createButtonsForButtonBar(Composite parent) {
	}
}

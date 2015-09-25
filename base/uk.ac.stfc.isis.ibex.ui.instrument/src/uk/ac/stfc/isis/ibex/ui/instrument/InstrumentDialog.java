
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

package uk.ac.stfc.isis.ibex.ui.instrument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class InstrumentDialog extends Dialog {
	
	private final String TITLE = "Select an Instrument";
	
	private InstrumentInfo selectedInstrument;
	private InstrumentSelectionPanel selector;
	
	public InstrumentInfo selectedInstrument() {
		return selectedInstrument;
	}
	
	protected InstrumentDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(TITLE);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 150);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		selector = new InstrumentSelectionPanel(parent, SWT.NONE, InstrumentUI.INSTRUMENT.instruments());
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		selectedInstrument = selector.getSelected();
		super.okPressed();
	}
}

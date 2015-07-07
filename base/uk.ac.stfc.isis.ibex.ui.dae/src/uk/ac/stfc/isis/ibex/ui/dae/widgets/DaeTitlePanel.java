
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

package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.RecordLabel;
import uk.ac.stfc.isis.ibex.ui.widgets.RecordSetter;
import uk.ac.stfc.isis.ibex.ui.widgets.styles.RecordSetterStyle;

public class DaeTitlePanel extends Composite {

	private RecordLabel runTitle;
	private RecordSetter newRunTitle;
	
	public DaeTitlePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblRunTitle = new Label(this, SWT.NONE);
		lblRunTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunTitle.setText("Run title:");
		
		runTitle = new RecordLabel(this, SWT.NONE);
		runTitle.setAlignment(SWT.CENTER);
		runTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runTitle.setText("UNKNOWN");
		
		Label lblNewTitle = new Label(this, SWT.NONE);
		lblNewTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewTitle.setText("New Title:");
		
		newRunTitle = new RecordSetter(this, RecordSetterStyle.FULL);
		newRunTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	public void bind(RunSummaryViewModel viewModel) {
		//runTitle.bindModel(viewModel.title());
		//newRunTitle.setModel(viewModel.titleSetter());
	}
}

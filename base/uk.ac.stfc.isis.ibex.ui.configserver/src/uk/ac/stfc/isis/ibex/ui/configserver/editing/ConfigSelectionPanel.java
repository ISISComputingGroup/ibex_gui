
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;

@SuppressWarnings("checkstyle:magicnumber")
public class ConfigSelectionPanel extends Composite {

	public ConfigSelectionPanel(Composite parent, int style, ConfigSelection selection) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblEditingConfiguration = new Label(this, SWT.NONE);
		lblEditingConfiguration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEditingConfiguration.setText("Configuration to edit:");
		
		Combo configurationSelector = new Combo(this, SWT.NONE);
		GridData gdConfigurationSelector = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdConfigurationSelector.widthHint = 150;
		configurationSelector.setLayoutData(gdConfigurationSelector);
	}
}


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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * Provides settings to control the script generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TestView {

	@PostConstruct
	public void createPartControl(Composite parent) {
		
		Group grpSettings = new Group(parent, SWT.NULL);
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		grpSettings.setLayout(new RowLayout(SWT.VERTICAL));
		grpSettings.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));

		//new Label(grpSettings, SWT.CENTER);
		
		//Label labelSettings = new Label(grpSettings, SWT.RIGHT);
		
		
		Label lblOrder = new Label(grpSettings, SWT.CENTER);
		//lblOrder.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		lblOrder.setText("test");
		
		
		Button button = new Button(grpSettings, SWT.PUSH);
		button.setText("Press me!");
	}

}
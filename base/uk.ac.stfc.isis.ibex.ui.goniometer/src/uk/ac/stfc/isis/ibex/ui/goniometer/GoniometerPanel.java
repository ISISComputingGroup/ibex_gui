
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

package uk.ac.stfc.isis.ibex.ui.goniometer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.goniometer.views.SetpointsTableView;

@SuppressWarnings("checkstyle:magicnumber")
public class GoniometerPanel extends ScrolledComposite {
	private SetpointsTableView goniometerSettings;
	
	public void setModel(GoniometerViewModel model) {
		goniometerSettings.setRows(model.settings());
	}
	
	public GoniometerPanel(Composite parent, int style) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setExpandHorizontal(true);
		setExpandVertical(true);
		
		GridLayout glParent = new GridLayout(1, false);
		glParent.horizontalSpacing = 20;
		parent.setLayout(glParent);
		
		goniometerSettings = new SetpointsTableView(parent, SWT.NONE);
		//goniometerSettings.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gdGoniometerSettings = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gdGoniometerSettings.heightHint = 210;
		gdGoniometerSettings.widthHint = 500;
		goniometerSettings.setLayoutData(gdGoniometerSettings);
		
		setModel(Activator.getDefault().viewModel());
	}
}

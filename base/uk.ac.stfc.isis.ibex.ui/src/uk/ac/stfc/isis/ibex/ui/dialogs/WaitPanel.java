
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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

/*
 * The panel inside the "Instrument Updating" dialogue box 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class WaitPanel extends Composite {

/**
 * The panel inside the pop-up dialogue box informing the user that the instrument is updating. 
 * 
 * @param parent parent
 * @param style style
 */
	public WaitPanel(Composite parent, int style) {
		super(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		CLabel lblWaitingForServer = new CLabel(this, SWT.CENTER);
		lblWaitingForServer.setAlignment(SWT.LEFT);
		lblWaitingForServer.setTopMargin(75);
		lblWaitingForServer.setBottomMargin(5);
		lblWaitingForServer.setRightMargin(5);
		lblWaitingForServer.setLeftMargin(5);
		lblWaitingForServer.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblWaitingForServer.setBackground(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "src/uk/ac/stfc/isis/ibex/ui/dialogs/WaitPanel_Background.png"));
		lblWaitingForServer.setText("Waiting for the instrument to update...");
		lblWaitingForServer.setImage(null);
		lblWaitingForServer.setSize(320, 200);
		lblWaitingForServer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
}

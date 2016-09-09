
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The estimated script time composite.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class EstimatePanel extends Composite {
	
	/**
	 * The default constructor.
	 * @param parent the parent that the EstimatePanel will be placed in
	 * @param style the style of the parent
	 */
	public EstimatePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		
		Group grpEstimate = new Group(this, SWT.NULL);
		grpEstimate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout glGrpEstimate = new GridLayout(2, false);
		grpEstimate.setLayout(glGrpEstimate);
		glGrpEstimate.horizontalSpacing = 10;
		glGrpEstimate.verticalSpacing = 6;
		
		Label lblCountRate = new Label(grpEstimate, SWT.RIGHT);
		lblCountRate.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblCountRate.setText("Est. count rate:");

		Text txtCountRate = new Text(grpEstimate, SWT.BORDER);
		txtCountRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtCountRate.setText("40");
				
		Label lblTimeBetween = new Label(grpEstimate, SWT.RIGHT);
		lblTimeBetween.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));  
		lblTimeBetween.setText("Est. time between moves:");
		
		Text txtTimeBetween = new Text(grpEstimate, SWT.BORDER);
		txtTimeBetween.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTimeBetween.setText("5");
		
		Label lblScriptTime = new Label(grpEstimate, SWT.RIGHT);
		lblScriptTime.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblScriptTime.setText("Est. script time:");

		Label lblTimeValue = new Label(grpEstimate, SWT.RIGHT);
		lblTimeValue.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));  
		lblTimeValue.setText("00:00:00");
		
		Button btnCalculate = new Button(grpEstimate, SWT.NONE);
		GridData gdButtonCalculate = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
		btnCalculate.setLayoutData(gdButtonCalculate);
		gdButtonCalculate.minimumWidth = 80;
		btnCalculate.setText("Calculate");
	}
}

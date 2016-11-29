
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

/**
 * Panel containing the various save/load/export buttons.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SaveLoadPanel extends Composite {
	
	/**
     * The default constructor.
     * 
     * @param parent the parent that the EstimatePanel will be placed in
     * @param style the style of the parent
     */
	public SaveLoadPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite saveLoad = new Composite(this, SWT.NONE);
		saveLoad.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout glSaveLoad = new GridLayout(1, false);
		saveLoad.setLayout(glSaveLoad);
		glSaveLoad.horizontalSpacing = 10;
		glSaveLoad.verticalSpacing = 6;
		
		Button btnSaveCsv = new Button(saveLoad, SWT.NONE);
		btnSaveCsv.setText("Save as CSV");
		GridData gdButtonSaveCsv = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonSaveCsv.minimumWidth = 120;
		btnSaveCsv.setLayoutData(gdButtonSaveCsv);
		
		Button btnLoadExcel = new Button(saveLoad, SWT.NONE);
		btnLoadExcel.setText("Load Excel File");
		GridData gdButtonLoadExcel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonLoadExcel.minimumWidth = 120;
		btnLoadExcel.setLayoutData(gdButtonLoadExcel);
		
		Composite separator = new Composite(saveLoad, SWT.NONE);
		GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gdSeparator.heightHint = 5;
		separator.setLayoutData(gdSeparator);
		
		Button btnExportExcel = new Button(saveLoad, SWT.NONE);
		btnExportExcel.setText("Export Table to Excel");
		GridData gdButtonExportExcel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonExportExcel.minimumWidth = 120;
		btnExportExcel.setLayoutData(gdButtonExportExcel);
		
	}

}

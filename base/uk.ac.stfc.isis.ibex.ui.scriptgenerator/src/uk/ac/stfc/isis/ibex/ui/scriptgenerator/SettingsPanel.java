
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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class SettingsPanel extends Composite {
	 
	public SettingsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Group grpSettings = new Group(this, SWT.NULL);
		GridData gdSettings = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		grpSettings.setLayoutData(gdSettings);
		GridLayout glGrpSettings = new GridLayout(5, false);
		grpSettings.setLayout(glGrpSettings);
		glGrpSettings.horizontalSpacing = 10;

		
		Label lblOrder = new Label(grpSettings, SWT.RIGHT);
		lblOrder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrder.setText("Order:");
		
		Combo comboOrder = new Combo(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] comboOrderItems = {"All TRANS first", "Alternate - TRANS first", "All SANS first", "Alternate - SANS first"};
		comboOrder.setItems(comboOrderItems);
		comboOrder.select(0);
		
		CLabel lblSeparator = new CLabel(grpSettings, SWT.CENTER);
		lblSeparator.setMargins(100, 0, 0, 0);
		
		Label lblSampleGeometry = new Label(grpSettings, SWT.RIGHT);
		lblSampleGeometry.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSampleGeometry.setText("Sample Geometry:");
		
		Combo comboSampleGeometry = new Combo(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboSampleGeometry.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] comboSampleGeometryItems = {"Disc", "Cylindrical", "Flat Plate", "Single Crystal"};
		comboSampleGeometry.setItems(comboSampleGeometryItems);
		comboSampleGeometry.select(0);
		
		Label lblDoSans = new Label(grpSettings, SWT.RIGHT);
		lblDoSans.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblDoSans.setText("Do SANS:");
		
		Text txtDoSans = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtDoSans = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtDoSans.setLayoutData(gdTxtDoSans);
		gdTxtDoSans.widthHint = 30;
		txtDoSans.setText("1");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblSampleHeight = new Label(grpSettings, SWT.RIGHT);
		lblSampleHeight.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSampleHeight.setText("Sample Height:");
		
		Text txtSampleHeight = new Text(grpSettings, SWT.BORDER);
		txtSampleHeight.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		txtSampleHeight.setText("7");
		
		Label lblDoTrans = new Label(grpSettings, SWT.RIGHT);
		lblDoTrans.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblDoTrans.setText("Do TRANS:");
		
		Text txtDoTrans = new Text(grpSettings, SWT.BORDER);
		txtDoTrans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		txtDoTrans.setText("1");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblSampleWidth = new Label(grpSettings, SWT.RIGHT);
		lblSampleWidth.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSampleWidth.setText("Sample Width:");
		
		Text txtSampleWidth = new Text(grpSettings, SWT.BORDER);
		txtSampleWidth.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		txtSampleWidth.setText("7");
		
		new Label(grpSettings, SWT.CENTER);
		
		Button btnLoopOver = new Button(grpSettings, SWT.CHECK);
		btnLoopOver.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnLoopOver.setText("Loop over each run?");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblCollectionMode = new Label(grpSettings, SWT.RIGHT);
		lblCollectionMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionMode.setText("Collection Mode:");
		
		Combo comboCollectionMode = new Combo(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboCollectionMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] comboCollectionModeItems = {"Histogram", "Events"};
		comboCollectionMode.setItems(comboCollectionModeItems);
		comboCollectionMode.select(0);
		
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		
		Label lblApertureSettings = new Label(grpSettings, SWT.RIGHT);
		lblApertureSettings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblApertureSettings.setText("A1, S1 Setting:");

		Combo comboApertureSans = new Combo(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureSans.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		String[] comboApertureSansItems = {"Large = ?, ? mm", "Medium = 20, 14 mm", "Small = ?, ? mm"};
		comboApertureSans.setItems(comboApertureSansItems);
		comboApertureSans.select(1);
		
		Label lblApertureSans = new Label(grpSettings, SWT.RIGHT);
		lblApertureSans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblApertureSans.setText("SANS");
		
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
			
		Combo comboApertureTrans = new Combo(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureTrans.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		String[] comboApertureTransItems = {"Large = ?, ? mm", "Medium = 20, 14 mm", "Small = ?, ? mm"};
		comboApertureTrans.setItems(comboApertureTransItems);
		comboApertureTrans.select(1);
		
		Label lblApertureTrans = new Label(grpSettings, SWT.RIGHT);
		lblApertureTrans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblApertureTrans.setText("TRANS");
	}

	public void bind() {
	}
}

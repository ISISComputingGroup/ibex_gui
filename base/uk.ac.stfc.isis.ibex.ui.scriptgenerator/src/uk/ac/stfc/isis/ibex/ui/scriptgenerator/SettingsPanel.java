
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.scriptgenerator.ApertureSans;
import uk.ac.stfc.isis.ibex.scriptgenerator.ApertureTrans;
import uk.ac.stfc.isis.ibex.scriptgenerator.CollectionMode;
import uk.ac.stfc.isis.ibex.scriptgenerator.Order;
import uk.ac.stfc.isis.ibex.scriptgenerator.SampleGeometry;
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
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		GridLayout glGrpSettings = new GridLayout(5, false);
		grpSettings.setLayout(glGrpSettings);
		glGrpSettings.horizontalSpacing = 10;
		glGrpSettings.verticalSpacing = 6;

		Label lblOrder = new Label(grpSettings, SWT.RIGHT);
		lblOrder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrder.setText("Order:");
		
		ComboViewer comboOrder = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboOrder.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOrder.setContentProvider(new ArrayContentProvider());
		comboOrder.setInput(Order.values());	
		ISelection orderSelection = new StructuredSelection(Order.TRANS);
		comboOrder.setSelection(orderSelection);
		
		CLabel lblSeparator = new CLabel(grpSettings, SWT.CENTER);
		lblSeparator.setMargins(100, 0, 0, 0);
		
		Label lblSampleGeometry = new Label(grpSettings, SWT.RIGHT);
		lblSampleGeometry.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSampleGeometry.setText("Sample Geometry:");
		
		ComboViewer comboSampleGeometry = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboSampleGeometry.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSampleGeometry.setContentProvider(new ArrayContentProvider());
		comboSampleGeometry.setInput(SampleGeometry.values());	
		ISelection selectionGeometrySelection = new StructuredSelection(SampleGeometry.DISC);
		comboSampleGeometry.setSelection(selectionGeometrySelection);
		
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
		GridData gdTxtSampleHeight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtSampleHeight.setLayoutData(gdTxtSampleHeight);
		gdTxtSampleHeight.widthHint = 30;
		txtSampleHeight.setText("7");
		
		Label lblDoTrans = new Label(grpSettings, SWT.RIGHT);
		lblDoTrans.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblDoTrans.setText("Do TRANS:");
		
		Text txtDoTrans = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtDoTrans = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtDoTrans.setLayoutData(gdTxtDoTrans);
		gdTxtDoTrans.widthHint = 30;
		txtDoTrans.setText("1");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblSampleWidth = new Label(grpSettings, SWT.RIGHT);
		lblSampleWidth.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSampleWidth.setText("Sample Width:");
		
		Text txtSampleWidth = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtSampleWidth = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtSampleWidth.setLayoutData(gdTxtSampleWidth);
		gdTxtSampleWidth.widthHint = 30;
		txtSampleWidth.setText("7");
		
		new Label(grpSettings, SWT.CENTER);
		
		Button btnLoopOver = new Button(grpSettings, SWT.CHECK);
		btnLoopOver.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnLoopOver.setText("Loop over each run?");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblCollectionMode = new Label(grpSettings, SWT.RIGHT);
		lblCollectionMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionMode.setText("Collection Mode:");
		
		ComboViewer comboCollectionMode = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboCollectionMode.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboCollectionMode.setContentProvider(new ArrayContentProvider());
		comboCollectionMode.setInput(CollectionMode.values());	
		ISelection selectionCollectionMode = new StructuredSelection(CollectionMode.HISTOGRAM);
		comboCollectionMode.setSelection(selectionCollectionMode);
		
		Composite separator = new Composite(grpSettings, SWT.NONE);
		GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gdSeparator.heightHint = 5;
		separator.setLayoutData(gdSeparator);

		Label lblApertureSettings = new Label(grpSettings, SWT.RIGHT);
		lblApertureSettings.setText("A1, S1 Setting:");

		ComboViewer comboApertureSans = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureSans.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboApertureSans.setContentProvider(new ArrayContentProvider());
		comboApertureSans.setInput(ApertureSans.values());	
		ISelection selectionApertureSans = new StructuredSelection(ApertureSans.MEDIUM);
		comboApertureSans.setSelection(selectionApertureSans);
		
		Label lblApertureSans = new Label(grpSettings, SWT.RIGHT);
		lblApertureSans.setText("SANS");
		
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
			
		ComboViewer comboApertureTrans = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureTrans.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboApertureTrans.setContentProvider(new ArrayContentProvider());
		comboApertureTrans.setInput(ApertureTrans.values());	
		ISelection selectionApertureTrans = new StructuredSelection(ApertureTrans.MEDIUM);
		comboApertureTrans.setSelection(selectionApertureTrans);
		
		Label lblApertureTrans = new Label(grpSettings, SWT.RIGHT);
		lblApertureTrans.setText("TRANS");
		new Label(grpSettings, SWT.NONE);
		new Label(grpSettings, SWT.NONE);
	}

	public void bind() {
	}
}

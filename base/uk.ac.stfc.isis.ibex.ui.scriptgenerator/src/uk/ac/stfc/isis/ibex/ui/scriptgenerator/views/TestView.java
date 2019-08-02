
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.ToyModel;
import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * Provides settings to control the script generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TestView {

	private ToyModel toyModel;
	private Label lblOrder;
	private DataBindingContext bindingContext;
	
	@PostConstruct
	/**
	 * A basic framework to hold the toy interface.
	 */
	public void createPartControl(Composite parent) {
		
		this.toyModel = Activator.getModel();
		
		Group grpSettings = new Group(parent, SWT.NULL);
		grpSettings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		grpSettings.setLayout(new RowLayout(SWT.VERTICAL));
		grpSettings.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));

		this.lblOrder = new Label(parent, SWT.NONE);
		this.lblOrder.setText("test");
		
		
		Button button = new Button(grpSettings, SWT.NONE);
		button.setText("Press me!");
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toyModel.iterateNumber();
			}
		});
		
		bind();
	}

	private void bind() {
		bindingContext = Utils.getNewDatabindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(lblOrder), 
				BeanProperties.value("iteratedNumber").observe(toyModel));
	}
	

}

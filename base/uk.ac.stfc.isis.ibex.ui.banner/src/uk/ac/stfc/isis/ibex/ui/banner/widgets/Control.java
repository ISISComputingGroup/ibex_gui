
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;

/**
 * Holds a button which can be pressed to send a value to a PV.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Control extends Composite {

	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private Button btnLoremIpsum;
	
	/**
	 * Create a button which can be pressed to send a value to a PV.
	 * @param parent Parent of control
	 * @param style SWT Style.
	 * @param model Control Model
	 */
	public Control(Composite parent, int style, ControlModel model) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		btnLoremIpsum = new Button(this, SWT.NONE);
		btnLoremIpsum.setText("Lorem Ipsum");
		btnLoremIpsum.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		
		if (model != null) {
			btnLoremIpsum.setText(model.text());
			btnLoremIpsum.setForeground(new Color(Display.getCurrent(), model.textColour()));
			btnLoremIpsum.setBackground(new Color(Display.getCurrent(), model.buttonColour()));
			btnLoremIpsum.setFont(SWTResourceManager.getFont("Segoe UI", model.fontSize(), SWT.BOLD));
			bind(model);
		}
	}

	private void bind(final ControlModel model) {
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnLoremIpsum), BeanProperties.value("value").observe(model.enabled()));
		
		btnLoremIpsum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.click();
			}
		});
	}
}

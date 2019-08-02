
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

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;

@SuppressWarnings("checkstyle:magicnumber")
public class ReadableComponentView extends Composite {
	
	private Label propertyName;
	private StyledText value;
	
	private final DataBindingContext bindingContext = new DataBindingContext();

	public ReadableComponentView(Composite parent, ReadableComponentProperty property) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		propertyName = new Label(this, SWT.NONE);		
		propertyName.setAlignment(SWT.CENTER);
		propertyName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		propertyName.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
        value = new StyledText(this, SWT.READ_ONLY);
		value.setDoubleClickEnabled(false);
		value.setEditable(false);
		value.setBackground(SWTResourceManager.getColor(240, 240, 240));
		value.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		value.setAlignment(SWT.RIGHT);
		
        this.setTabList(new Control[0]); // Remove tabbing as read only

		setProperty(property);
	}

	private void setProperty(ReadableComponentProperty property) {
		propertyName.setText(property.displayName());
		bindingContext.bindValue(WidgetProperties.text().observe(value), BeanProperties.value("value").observe(property.value()));
	}
}

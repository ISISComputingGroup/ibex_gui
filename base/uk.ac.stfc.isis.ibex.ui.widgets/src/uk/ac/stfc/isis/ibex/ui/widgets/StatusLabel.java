
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.widgets.models.LabelModel;

public class StatusLabel extends CLabel {
	
	private DataBindingContext bindingContext;
	private LabelModel model;
	
	public StatusLabel(Composite parent, int style) {
		super(parent, style);

		setAlignment(SWT.RIGHT);
		setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));	
		
		this.pack();
	}	
		
	public LabelModel getModel() {
		return model;
	}
	
	public void setModel(LabelModel model) {
		this.model = model;
		bindModel(model);
	}
	
	public void bindModel(LabelModel model) {
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(this), BeanProperties.value("description").observe(model));
		bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("color").observe(model));
		bindingContext.bindValue(WidgetProperties.image().observe(this), BeanProperties.value("image").observe(model));
	}
}

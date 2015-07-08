
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

package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

public class Indicator extends Composite {

	private final StyledText text;
	
	public Indicator(Composite parent, int style, IndicatorModel model, Font font) {
		super(parent, style);
		setEnabled(false);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new StyledText(this, SWT.READ_ONLY | SWT.SINGLE);
		text.setMargins(0, 1, 0, 2);
		text.setText("Lorem Ipsum");
		text.setAlignment(SWT.CENTER);
		text.setEnabled(false);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		text.setFont(font);
		text.setVisible(true);
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(IndicatorModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(text), BeanProperties.value("value").observe(model.text()));
		bindingContext.bindValue(WidgetProperties.foreground().observe(text), BeanProperties.value("value").observe(model.color()));
		bindingContext.bindValue(WidgetProperties.visible().observe(text), BeanProperties.value("value").observe(model.availability()));
	}
}

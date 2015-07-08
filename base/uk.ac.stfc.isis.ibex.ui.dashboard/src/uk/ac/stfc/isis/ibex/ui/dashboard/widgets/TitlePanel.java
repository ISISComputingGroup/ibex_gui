
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.ui.dashboard.models.TitlePanelModel;

public class TitlePanel extends Composite {

	private final Label title;
	private final Label users;
	
	public TitlePanel(Composite parent, int style, TitlePanelModel model, Font font) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setFont(font);
		lblTitle.setText("Title:");
		
		title = new Label(this, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		title.setFont(font);
		title.setText("Experiment title");
		title.setToolTipText("Experiment title");
		
		Label lblUsers = new Label(this, SWT.NONE);
		lblUsers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsers.setFont(font);
		lblUsers.setText("Users:");
		
		users = new Label(this, SWT.NONE);
		users.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		users.setFont(font);
		users.setText("Experiment users");
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(TitlePanelModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(title), BeanProperties.value("value").observe(model.title()));
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(title), BeanProperties.value("value").observe(model.title()));
		
		UsersConverter deJsoner = new UsersConverter(String.class, String.class);
		bindingContext.bindValue(WidgetProperties.text().observe(users), BeanProperties.value("value").observe(model.users()), null, new UpdateValueStrategy().setConverter(deJsoner));	
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(users), BeanProperties.value("value").observe(model.users()), null, new UpdateValueStrategy().setConverter(deJsoner));
	}
}

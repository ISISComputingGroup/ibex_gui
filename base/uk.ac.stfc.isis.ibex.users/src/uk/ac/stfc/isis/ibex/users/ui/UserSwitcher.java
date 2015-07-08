
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

package uk.ac.stfc.isis.ibex.users.ui;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class UserSwitcher extends Composite {
	private Combo switchToCombo;
	private Text password;

	@SuppressWarnings("checkstyle:localvariablename")
	public UserSwitcher(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblSwitchToUser = new Label(this, SWT.NONE);
		lblSwitchToUser.setText("User:");
		
		switchToCombo = new Combo(this, SWT.READ_ONLY);
		GridData gd_switchToCombo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_switchToCombo.widthHint = 120;
		switchToCombo.setLayoutData(gd_switchToCombo);
		
		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password:");
		
		password = new Text(this, SWT.BORDER | SWT.PASSWORD);
		GridData gd_password = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_password.minimumWidth = 100;
		gd_password.widthHint = 115;
		password.setLayoutData(gd_password);
	}
	
	public void setModel(final UserSwitcherModel model) {
		List<String> userNames = model.userNames();
		switchToCombo.setItems(userNames.toArray(new String[userNames.size()]));
		switchToCombo.select(userNames.indexOf(model.currentUserName()));
		switchToCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setSelectedUserName(switchToCombo.getText());
			}
		});
		
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(password), BeanProperties.value("passwordRequired").observe(model));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(password), BeanProperties.value("password").observe(model));
	}
}

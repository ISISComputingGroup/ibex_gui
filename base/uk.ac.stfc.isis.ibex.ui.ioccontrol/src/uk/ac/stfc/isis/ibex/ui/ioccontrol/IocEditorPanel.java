
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.StatusColorConverter;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.StatusTextConverter;

@SuppressWarnings("checkstyle:magicnumber")
public class IocEditorPanel extends Composite {
	
	private final Label name;
	private final Label status;	
	private final Text description;
	private final Button update;
	private final IocButtonPanel buttons;

    private IocState ioc;
	private final IocControl control;

	private DataBindingContext bindingContext;
	
	private static final UpdateValueStrategy STATUS_TEXT_UPDATE_STRATEGY = new UpdateValueStrategy();
	private static final UpdateValueStrategy STATUS_COLOR_UPDATE_STRATEGY = new UpdateValueStrategy();
	static {
		STATUS_TEXT_UPDATE_STRATEGY.setConverter(new StatusTextConverter());
		STATUS_COLOR_UPDATE_STRATEGY.setConverter(new StatusColorConverter());
	};
	
	private SelectionAdapter sendDescription = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (ioc != null) {
				ioc.setDescription(description.getText());
			}
			
			update.setEnabled(false);
		}
	};
	
	public IocEditorPanel(Composite parent, int style, IocControl control) {
		super(parent, style);
		this.control = control;
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Group grpSelectedIoc = new Group(this, SWT.NONE);
		grpSelectedIoc.setText("IOC");
		grpSelectedIoc.setLayout(new GridLayout(5, false));
		
		Label lblName = new Label(grpSelectedIoc, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedIoc, SWT.NONE);
		GridData gdName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdName.widthHint = 150;
		name.setLayoutData(gdName);
		
		Label lblStatus = new Label(grpSelectedIoc, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("Status:");
		
		status = new Label(grpSelectedIoc, SWT.NONE);
		GridData gdStatus = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdStatus.widthHint = 50;
		status.setLayoutData(gdStatus);
		new Label(grpSelectedIoc, SWT.NONE);
		
		Label lblDescription = new Label(grpSelectedIoc, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setSize(63, 15);
		lblDescription.setText("Description:");
		
		description = new Text(grpSelectedIoc, SWT.BORDER);
		GridData gdText = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gdText.widthHint = 120;
		description.setLayoutData(gdText);
		description.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent arg0) {
				update.setEnabled(true);
			}
		});
		
		update = new Button(grpSelectedIoc, SWT.NONE);
		update.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		update.setText("Update");
		update.addSelectionListener(sendDescription);
		update.setEnabled(false);
		
		new Label(grpSelectedIoc, SWT.NONE);
		new Label(grpSelectedIoc, SWT.NONE);
		new Label(grpSelectedIoc, SWT.NONE);
		new Label(grpSelectedIoc, SWT.NONE);
		new Label(grpSelectedIoc, SWT.NONE);
		new Label(grpSelectedIoc, SWT.NONE);
		
		buttons = new IocButtonPanel(grpSelectedIoc, SWT.NONE, control);
		GridData gdButtons = new GridData(SWT.LEFT, SWT.FILL, false, false, 3, 1);
		gdButtons.widthHint = 220;
		buttons.setLayoutData(gdButtons);
		
		setIoc(null);
	}

    public void setIoc(IocState ioc) {
		this.ioc = ioc;
		buttons.setIoc(ioc);

		if (ioc == null) {
			name.setText("");
			name.setEnabled(false);
			status.setText("");
			status.setEnabled(false);
			description.setText("");
			description.setEnabled(false);
			update.setEnabled(false);

			bindingContext = null;
			return;
		}
		
		name.setEnabled(true);
		status.setEnabled(true);
		description.setEnabled(true);

		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(name), BeanProperties.value("name").observe(ioc));
		bindingContext.bindValue(WidgetProperties.text().observe(status), BeanProperties.value("isRunning").observe(ioc), null, STATUS_TEXT_UPDATE_STRATEGY);
		bindingContext.bindValue(WidgetProperties.foreground().observe(status), BeanProperties.value("isRunning").observe(ioc), null, STATUS_COLOR_UPDATE_STRATEGY);
		bindingContext.bindValue(WidgetProperties.text().observe(description), BeanProperties.value("description").observe(ioc));
		bindingContext.bindValue(WidgetProperties.enabled().observe(description), BeanProperties.value("canSend").observe(control.startIoc()));
		
		update.setEnabled(false);
	}
}

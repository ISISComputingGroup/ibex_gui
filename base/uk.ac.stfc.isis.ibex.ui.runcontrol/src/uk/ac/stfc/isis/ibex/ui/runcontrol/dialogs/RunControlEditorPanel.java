
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

package uk.ac.stfc.isis.ibex.ui.runcontrol.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.runcontrol.EditableRunControlSetting;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

@SuppressWarnings({"checkstyle:magicnumber"})
public class RunControlEditorPanel extends Composite {

	private final RunControlServer runControlServer;
	private final ConfigServer configServer;
	private final Label name;
	private final Text txtLowLimit;
	private final Text txtHighLimit;
	private final Button chkEnabled;
	private final Button btnSend;
	private DisplayBlock block;
	private boolean canSend;
	
	private DataBindingContext bindingContext;
	
	private SelectionAdapter sendChanges = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (block != null) {
				EditableRunControlSetting setting = new EditableRunControlSetting(block.getName(), runControlServer);
				setting.setLowLimit(txtLowLimit.getText());
				setting.setHighLimit(txtHighLimit.getText());
				setting.setEnabled(chkEnabled.getSelection());
			}
			
			btnSend.setEnabled(false);
		}
	};
	
	/**
	 * Disable the send button if does not have permission to edit configs.
	 */
	protected final SameTypeWriter<Configuration> configService = new SameTypeWriter<Configuration>() {	
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			canSend = canWrite;
		};	
	};
	
	public RunControlEditorPanel(Composite parent, int style, ConfigServer configServer, RunControlServer runControlServer) {
		super(parent, style);
		
		this.configServer = configServer;
		this.runControlServer = runControlServer;
		
		// A bit of a work-around to see if we have write permissions
		// by seeing if we are able to edit the config.
		this.configServer.saveAs().subscribe(configService);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Group grpSelectedSetting = new Group(this, SWT.NONE);
		grpSelectedSetting.setText("Edit Settings");
		grpSelectedSetting.setLayout(new GridLayout(5, false));
		
		Label lblName = new Label(grpSelectedSetting, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Label(grpSelectedSetting, SWT.NONE);
		GridData gdLblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gdLblName.widthHint = 150;
		name.setLayoutData(gdLblName);
		
		Label lblLowLimit = new Label(grpSelectedSetting, SWT.NONE);
		lblLowLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLowLimit.setText("Low Limit:");
		
		txtLowLimit = new Text(grpSelectedSetting, SWT.BORDER);
		GridData gdTxtLow = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdTxtLow.widthHint = 30;
		txtLowLimit.setLayoutData(gdTxtLow);
		txtLowLimit.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent arg0) {
				btnSend.setEnabled(true);
			}
		});
		
		Label lblHighLimit = new Label(grpSelectedSetting, SWT.NONE);
		lblHighLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHighLimit.setText("High Limit:");
		
		txtHighLimit = new Text(grpSelectedSetting, SWT.BORDER);
		GridData gdTxtHigh = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdTxtHigh.widthHint = 30;
		txtHighLimit.setLayoutData(gdTxtHigh);
		txtHighLimit.addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent arg0) {
				btnSend.setEnabled(true);
			}
		});
		
		chkEnabled = new Button(grpSelectedSetting, SWT.CHECK);
		chkEnabled.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		chkEnabled.setText("Enabled");
		chkEnabled.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnSend.setEnabled(true);
			}
			
		});
		
		btnSend = new Button(grpSelectedSetting, SWT.NONE);
		btnSend.setText("Apply Changes");
		GridData gdBtnSend = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 5, 1);
		btnSend.setLayoutData(gdBtnSend);
		btnSend.addSelectionListener(sendChanges);
		
		setBlock(null);
	}
	
	public void setBlock(DisplayBlock block) {
		this.block = block;

		if (block == null) {
			name.setText("");
			txtLowLimit.setText("");
			txtLowLimit.setEnabled(false);
			txtHighLimit.setText("");
			txtHighLimit.setEnabled(false);	
			chkEnabled.setEnabled(false);
			btnSend.setEnabled(false);
			bindingContext = null;
			return;
		}

		if (canSend) {
			txtLowLimit.setEnabled(true);
			txtHighLimit.setEnabled(true);
			chkEnabled.setEnabled(true);
		} else {
			txtLowLimit.setEnabled(false);
			txtHighLimit.setEnabled(false);
			chkEnabled.setEnabled(false);
		}
		
		// Okay to use current values
		txtLowLimit.setText(block.getLowLimit().trim());
		txtHighLimit.setText(block.getHighLimit().trim());
		chkEnabled.setSelection(block.getEnabled());

		// Bind the name as that in non-editable
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(name), BeanProperties.value("name").observe(block));
		
		btnSend.setEnabled(false);
	}

}


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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.PvSelectorDialog;

public class BlockDetailsPanel extends Composite {
	
	private final Text name;
	private final Text pvAddress;
	private final Button visible;
	private final Button local;
	private final Button btnPickPV;
		
	private EditableConfiguration config;

	public BlockDetailsPanel(Composite parent, int style, EditableBlock block, EditableConfiguration config) {
		super(parent, style);
		
		this.config = config;
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpBlock = new Group(this, SWT.NONE);
		grpBlock.setText("Selected block");
		grpBlock.setLayout(new GridLayout(7, false));
		
		Label lblName = new Label(grpBlock, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Text(grpBlock, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = 100;
		name.setLayoutData(gd_name);
		new Label(grpBlock, SWT.NONE);
		
		visible = new Button(grpBlock, SWT.CHECK);
		visible.setText("Visible");
		new Label(grpBlock, SWT.NONE);
		
		local = new Button(grpBlock, SWT.CHECK);
		local.setText("Local");
		new Label(grpBlock, SWT.NONE);
		
		Label lblPvAddress = new Label(grpBlock, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("PV address:");
		
		pvAddress = new Text(grpBlock, SWT.BORDER);
		GridData gd_pvAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_pvAddress.widthHint = 250;
		pvAddress.setLayoutData(gd_pvAddress);
		
		btnPickPV = new Button(grpBlock, SWT.NONE);
		btnPickPV.setText("Select PV");
		btnPickPV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openPvDialog();
			}
		});
		
		setEnabled(false);
		
		setBlock(block);
	}
	
	public void setBlock(EditableBlock block) {	
		if (block == null) {
			setEnabled(false);
			name.setText("");
			pvAddress.setText("");
			visible.setSelection(false);
			local.setSelection(false);
	
			return;
		}
		
		name.setText(block.getName());
		pvAddress.setText(block.getPV());
		local.setSelection(block.getIsLocal());
		visible.setSelection(block.getIsVisible());
			
		setEnabled(block.isEditable());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		name.setEnabled(enabled);
		pvAddress.setEnabled(enabled);
		visible.setEnabled(enabled);
		local.setEnabled(enabled);
		btnPickPV.setEnabled(enabled);
	}
	
	private void openPvDialog() {
		PvSelectorDialog pvDialog = new PvSelectorDialog(null, config, pvAddress.getText());	
		if (pvDialog.open() == Window.OK) {
			pvAddress.setText(pvDialog.getPVAddress());
		}
	}
	
	public String getBlockName() {
		return name.getText();
	}
	
	public String getPV() {
		return pvAddress.getText();
	}
	
	public boolean getIsLocal() {
		return local.getSelection();
	}
	
	public boolean getIsVisible() {
		return visible.getSelection();
	}
	
	public void addNameModifyListener(ModifyListener modifyListener) {
	    name.addModifyListener(modifyListener);
	}
}

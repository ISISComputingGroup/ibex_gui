
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksTable;

/**
 * A composite for selecting a block.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BlockSelectorPanel extends Composite {

	private final Text blockName;
	private final Text pvAddress;
	private final BlocksTable blockTable;
	private DataBindingContext bindingContext;
	
	public BlockSelectorPanel(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpPV = new Group(this, SWT.NONE);
		grpPV.setText("Block Selector");
		
		GridLayout gdGrpPV = new GridLayout(2, false);
		grpPV.setLayout(gdGrpPV);
		
		Label lblBlockName = new Label(grpPV, SWT.NONE);
		lblBlockName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBlockName.setText("Block Name:");
		
		blockName = new Text(grpPV, SWT.BORDER);
		GridData gdBlockName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdBlockName.widthHint = 250;
		blockName.setLayoutData(gdBlockName);
		
		Label lblPvAddress = new Label(grpPV, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("Block PV address:");
		
		pvAddress = new Text(grpPV, SWT.BORDER);
		GridData gdPvAddress = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdPvAddress.widthHint = 250;
		pvAddress.setLayoutData(gdPvAddress);
		pvAddress.setEditable(false);
		
		blockName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				blockTable.setSearch(blockName.getText());
			}
		});
		
		blockTable = new BlocksTable(grpPV, SWT.NONE, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION, false);
		GridData gdPvTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gdPvTable.heightHint = 300;
		blockTable.setLayoutData(gdPvTable);
		
		blockTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					EditableBlock block = (EditableBlock) selection.getFirstElement();
					blockName.setText(block.getName());
					pvAddress.setText(block.getPV());
				}
			}
		});
		
		blockTable.setRows(Configurations.getInstance().edit().currentConfig().getValue().getEditableBlocks());
		blockTable.refresh();
	}
	
	public void setConfig(EditableConfiguration config, Block block) {		
		//Set up the binding here
		bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(blockName), BeanProperties.value("name").observe(block), null, null);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(pvAddress), BeanProperties.value("PV").observe(block), null, null);
	}

}

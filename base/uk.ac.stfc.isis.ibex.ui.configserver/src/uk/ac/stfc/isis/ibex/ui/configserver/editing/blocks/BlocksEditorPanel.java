
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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class BlocksEditorPanel extends Composite {

	private final BlocksTable table;
	private final Button add;
	private final Button remove;
	private final BlockDetailsPanel details;
	
	private EditableConfiguration config;
	
	public BlocksEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		table = new BlocksTable(this, SWT.NONE, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 200;
		table.setLayoutData(gd_table);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		add = new Button(composite, SWT.NONE);
		GridData gd_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_add.widthHint = 70;
		add.setLayoutData(gd_add);
		add.setText("Add");
		add.setEnabled(false);		
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableBlock added = config.addNewBlock();
				setBlocks(config);
				setSelectedBlock(added);
				table.setSelected(added);
			}
		});
		
		remove = new Button(composite, SWT.NONE);
		remove.setEnabled(false);
		GridData gd_remove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_remove.widthHint = 70;
		remove.setLayoutData(gd_remove);
		remove.setText("Remove");
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableBlock toRemove = table.firstSelectedRow();
				int index = table.getSelectionIndex();
				config.removeBlock(toRemove);
				setBlocks(config);
				
				// Update new selection
				int newIndex = index > 0 ? index - 1 : index;
				table.setSelectionIndex(newIndex);
				setSelectedBlock(table.firstSelectedRow());
			}
		});
		
		details = new BlockDetailsPanel(this, SWT.NONE, messageDisplayer);
		GridData gd_details = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		//gd_details.heightHint = 200;
		details.setLayoutData(gd_details);
		
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				EditableBlock selected = table.firstSelectedRow();
				setSelectedBlock(selected);
			}
		});
	}

	public void setConfig(EditableConfiguration config) {	
		this.config = config;
		
		add.setEnabled(true);
		setBlocks(config);
		details.setConfig(config);
	}
	
	private void setBlocks(EditableConfiguration config) {
		table.setRows(config.getEditableBlocks());
		table.refresh();
	}
	
	private void setSelectedBlock(EditableBlock selected) {
		remove.setEnabled(removeEnabled(selected));
		details.setBlock(selected);
	}
	
	private boolean removeEnabled(EditableBlock block) {
		return block != null && block.isEditable();
	}
}

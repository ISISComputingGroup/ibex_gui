
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename"})
public class BlocksEditorPanel extends Composite {

	private final BlocksTable table;
	private final Button add;
	private final Button edit;
	private final Button remove;
	
	private EditableConfiguration config;
    private RunControlServer runControl;
	
	public BlocksEditorPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		table = new BlocksTable(this, SWT.NONE, SWT.V_SCROLL | SWT.MULTI | SWT.NO_SCROLL | SWT.FULL_SELECTION, true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 90;
		table.setLayoutData(gd_table);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		add = new Button(composite, SWT.NONE);
		GridData gd_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_add.widthHint = 110;
		
		add.setLayoutData(gd_add);
		add.setText("Add Block");	
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableBlock added = config.addNewBlock();
                EditBlockDialog dialog = new EditBlockDialog(getShell(), added, config);
				dialog.open();
				setBlocks(config);
				setSelectedBlocks(new ArrayList<EditableBlock>(Arrays.asList(added)));
				table.setSelected(added);
			}
		});
		
		edit = new Button(composite, SWT.NONE);
		GridData gd_edit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_edit.widthHint = 110;
		edit.setLayoutData(gd_edit);
		edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditableBlock toEdit = table.firstSelectedRow();
                EditBlockDialog dialog = new EditBlockDialog(getShell(), toEdit, config);
				dialog.open();
			}
		});
		edit.setText("Edit Block");
		edit.setEnabled(false);
		
		remove = new Button(composite, SWT.NONE);
		remove.setEnabled(false);
		GridData gd_remove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_remove.widthHint = 110;
		remove.setLayoutData(gd_remove);
		remove.setText("Delete Block");
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteSelected();
			}
		});
		
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				List<EditableBlock> selected = table.selectedRows();
				setSelectedBlocks(selected);
			}
		});
	}

	public void setConfig(EditableConfiguration config) {	
		this.config = config;
		
		add.setEnabled(true);
		setBlocks(config);
	}

	private void setBlocks(EditableConfiguration config) {
		table.setRows(config.getEditableBlocks());
		table.refresh();
	}
	
	private void setSelectedBlocks(List<EditableBlock> selected) {
		if (selected.size() > 1) {
			edit.setEnabled(false);
		} else {
			edit.setEnabled(editEnabled(selected));
		}
		remove.setEnabled(editEnabled(selected));
	}
	
	private boolean editEnabled(List<EditableBlock> blocks) {
		boolean output = true;
		for (EditableBlock block : blocks) {
			output &= block != null && block.isEditable();
		}
		return output;
	}
	
	private void deleteSelected() {
		List<EditableBlock> toRemove = table.selectedRows();
		String dialogTitle = "Delete Block";
		String dialogText = "Do you really want to delete the block";
		
		if (toRemove.size() == 1) {
			dialogText += " " + toRemove.get(0).getName() + "?";
		} else {
			dialogTitle = "Delete Blocks";
			dialogText += "s " + blockNamesToString(toRemove) + "?";
		}
				
		MessageBox dialog = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
		dialog.setText(dialogTitle);
		dialog.setMessage(dialogText);
		int returnCode = dialog.open();
		
		if (returnCode == SWT.OK) {
			int index = table.getSelectionIndex();
			config.removeBlocks(toRemove);
			setBlocks(config);
		
			// Update new selection
			int newIndex = index > 0 ? index - 1 : index;
			table.setSelectionIndex(newIndex);
			setSelectedBlocks(table.selectedRows());
		}
	}
	
	private String blockNamesToString(List<EditableBlock> blocks) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<blocks.size(); i++) {
			EditableBlock block = blocks.get(i);
			sb.append(block.getName());
			if (i == blocks.size() - 2) {
				sb.append(" and ");
			} else if (i != blocks.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

    public void openEditBlockDialog(String blockName) {
        for (EditableBlock block : config.getEditableBlocks()) {
            if (block.getName().equals(blockName)) {
                EditBlockDialog dialog = new EditBlockDialog(getShell(), block, config);
                dialog.open();
                return;
            }
        }

    }
}

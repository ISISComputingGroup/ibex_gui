
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockFactory;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateBlockNameException;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DeleteTableItemHelper;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;


/**
 * The class responsible for creating the block editor panel.
 *
 */
@SuppressWarnings({"magicnumber", "unused" })
public class BlocksEditorPanel extends Composite {

	private final BlocksTable table;
	private final Button add;
	private final Button edit;
	private final Button remove;
	private final Button copy;
	private final BlocksEditorViewModel viewModel;
	
	private EditableConfiguration config;
	
	/**
	 * The constructor for the block editor panel.
	 *
	 * @param parent
	 *                 The composite that holds this panel.
	 *      
	 * @param style
	 *                 An integer giving the panel style using SWT style flags.
	 * 
	 * @param viewModel
	 *                 The view model for this panel.        
	 */
	public BlocksEditorPanel(Composite parent, int style, BlocksEditorViewModel viewModel) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		this.viewModel = viewModel;
		
		table = new BlocksTable(this, SWT.NONE, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION, true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 90;
		table.setLayoutData(gd_table);
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (viewModel.getEditEnabled()) {
	                if (e.keyCode == SWT.DEL) {
	                    deleteSelected();
	                //copies selected blocks if press "Ctrl + D".
	                } else if (e.character == 0x4) {
	                    copySelected();
	                //edits first selected block if press "Ctrl +E". 
	                } else if (e.character == 0x5) {
                        openEditBlockDialog(table.firstSelectedRow());
                    }
                }
	                
	            //adds new block if press "Ctrl + A".
	            if (e.character == 0x01) {
	            	addNew();
	            }              
            }
        });
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		GridData gd_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_add.widthHint = 110;
		
		add = new IBEXButton(composite, SWT.NONE, evt -> {
			addNew();
		})
				.layoutData(gd_add)
				.text("&Add Block")
				.get();
		
		GridData gd_copy = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_copy.widthHint = 110;
		
		copy = new IBEXButton(composite, SWT.NONE, evt -> {
			copySelected();
		})
				.layoutData(gd_add)
				.text("&Duplicate Block")
				.get();
		
		GridData gd_edit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_edit.widthHint = 110;
		
		edit = new IBEXButton(composite, SWT.NONE, evt -> {
			openEditBlockDialog(table.firstSelectedRow());
		})
				.layoutData(gd_add)
				.text("&Edit Block")
				.get();

		GridData gd_remove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_remove.widthHint = 110;
		
		remove = new IBEXButton(composite, SWT.NONE, evt -> {
			deleteSelected();
		})
				.layoutData(gd_add)
				.text("Delete Block")
				.get();
		
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				List<EditableBlock> selected = table.selectedRows();
				viewModel.setSelectedBlocks(selected);
			}
		});
        table.viewer().addDoubleClickListener(new IDoubleClickListener() {
		
			@Override
			public void doubleClick(DoubleClickEvent event) {
				EditableBlock block = table.firstSelectedRow();
                if (!block.inComponent()) {
                    openEditBlockDialog(block);
                }
			}
		});
        
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.enabled().observe(edit),
                BeanProperties.value("editEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(copy),
                BeanProperties.value("copyDeleteEnabled").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(remove),
                BeanProperties.value("copyDeleteEnabled").observe(viewModel));
        
        viewModel.setSelectedBlocks(new ArrayList<>());
	}
	
	/**
     * This method is responsible for setting the config to which the blocks will be added.
     * 
     * @param config the configuration the blocks are to be registered with.
     */
	public void setConfig(EditableConfiguration config) {	
		this.config = config;
		
		config.addPropertyChangeListener("blocks", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setBlocks(config);
			}
		});
		
		setBlocks(config);
	}
	
	private void copySelected() {
	    for (Block block : table.selectedRows()) {
	        EditableBlock added = new EditableBlock(block);
	        added.setName(viewModel.getUniqueName(added.getName()));
                
	        try {
	            config.addNewBlock(added);
	        } catch (DuplicateBlockNameException e1) {
	            MessageDialog error = new MessageDialog(this.getShell(), "Error", null,
	                    "Failed to add block " + added.getName() + ":\nBlock with this name already exists.",
	                    MessageDialog.ERROR, new String[] {"OK"}, 0);
	            error.open();
	        }
	        table.setSelected(added);
	    }
	}

	private void addNew() {
	    BlockFactory blockFactory = new BlockFactory(config);
        EditableBlock added = blockFactory.createNewBlock(Optional.empty());
        EditBlockDialog dialog = new EditBlockDialog(getShell(), added, config);
        dialog.open();
        table.setSelected(added);
	}
	
	private void setBlocks(EditableConfiguration config) {
		table.setRows(config.getAllBlocks());
		table.refresh();
	}

	private void deleteSelected() {
    	List<EditableBlock> toRemove = table.selectedRows();
    	
    	DeleteTableItemHelper<EditableBlock> helper = new DeleteTableItemHelper<>();
        int returnCode = helper.createDeleteDialog("Block", toRemove);
		
		if (returnCode == SWT.OK) {
			int index = table.getSelectionIndex();
			config.removeBlocks(toRemove);
			
			// Update new selection
			int newIndex = index > 0 ? index - 1 : index;
			table.setSelectionIndex(newIndex);
		}
	}

    private void openEditBlockDialog(EditableBlock toEdit) {
        EditBlockDialog dialog = new EditBlockDialog(getShell(), toEdit, config);
        dialog.open();
    }

    /**
     * Show the mneumonic underlining to the user.
     */
    public void showMnemonics() {
        Event event = new Event();
        event.keyCode = SWT.ALT;
        event.type = SWT.KeyDown;
        Display.getCurrent().post(event);
        event.type = SWT.KeyUp;
        Display.getCurrent().post(event);

    }
}

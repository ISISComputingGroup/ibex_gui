
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

package uk.ac.stfc.isis.ibex.ui.goniometer.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class HomingLabelProvider extends ColumnLabelProvider {

	private Map<ViewerCell, Button> buttons = new HashMap<>();
	private Map<ViewerCell, TableEditor> editors = new HashMap<>();
	
	@Override
    public void update(ViewerCell cell) {
		if (buttons.containsKey(cell)) {
			return;
		}
		
        addButton(cell);               
        addEditor(cell);
        
        // Intention is to enable button if not homing, however, changes to HOMR
        // seems not to be picked up currently
//
//        button.setEnabled(row.canHome());
//        row.addPropertyChangeListener(new PropertyChangeListener() {		
//			@Override
//			public void propertyChange(PropertyChangeEvent event) {
//				button.setEnabled(row.canHome());
//			}
//		});
        
    }

	private void addEditor(ViewerCell cell) {
		Button button = buttons.get(cell);
        TableItem item = (TableItem) cell.getItem();
		TableEditor editor = new TableEditor(item.getParent());
        editor.grabHorizontal  = true;
        editor.grabVertical = true;
        editor.setEditor(button, item, cell.getColumnIndex());
        editor.layout();
        
        editors.put(cell, editor);
	}

	private void addButton(ViewerCell cell) {
		Button button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
        button.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.goniometer", "icons/crosshair.png"));
        button.setText("Home");

        buttons.put(cell, button);
        
        final MotorSetpoint row = (MotorSetpoint) cell.getElement();
        button.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		row.home();
        	}
        });          
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (Button button : buttons.values()) {
			button.dispose();
		}
		
		for (TableEditor editor : editors.values()) {
			editor.dispose();
		}
	}
}

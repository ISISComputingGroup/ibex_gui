
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;

public abstract class ControlCellLabelProvider {
	
	private final Map<ViewerCell, Control> cellControls = new HashMap<>();
	private final Map<ViewerCell, TableEditor> cellEditors = new HashMap<>();
	
	protected ControlCellLabelProvider() {
	}
	
	protected Control getControl(ViewerCell cell, int style) {
		return cellControls.containsKey(cell) ? cellControls.get(cell) : create(cell, style);
	}
	
	public void dispose() {
		for (Control control : cellControls.values()) {
			control.dispose();
		}
		
		for (TableEditor editor : cellEditors.values()) {
			editor.dispose();
		}
		
		cellControls.clear();
	}
	
	protected abstract Control createControl(final ViewerCell cell, int style);
	
	private Control create(final ViewerCell cell, int style) {
		Control control = createControl(cell, style);
		
		setEditor(cell, control);
		cellControls.put(cell, control);
		
		return control;
	}
	
	private Composite composite(ViewerCell cell) {
		return (Composite) cell.getControl();
	}
	
	private void setEditor(ViewerCell cell, Control control) {
		TableItem item = (TableItem) cell.getItem();
		TableEditor editor = new TableEditor(item.getParent());
		editor.horizontalAlignment = SWT.FILL;
		editor.verticalAlignment = SWT.FILL;
	    editor.grabHorizontal  = true;
	    editor.grabVertical = true;
	    editor.setEditor(control, item, cell.getColumnIndex());
	    editor.layout();
	    
	    cellEditors.put(cell, editor);
		
	}
}

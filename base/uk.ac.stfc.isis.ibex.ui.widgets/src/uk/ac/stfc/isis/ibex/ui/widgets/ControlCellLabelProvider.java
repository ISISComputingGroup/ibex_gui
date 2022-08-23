
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
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.ui.tables.SortableObservableMapCellLabelProvider;

/**
 * A cell provider that puts any type of control into a table's cell.
 *
 * @param <T> the type of control to put in the cell
 * @param <TRow> the type of the data in the row
 */
public abstract class ControlCellLabelProvider<T extends Control, TRow> extends SortableObservableMapCellLabelProvider<TRow> {
	
	private final Map<ViewerRow, T> cellControls = new HashMap<>();
	private final Map<ViewerCell, TableEditor> cellEditors = new HashMap<>();
	
	/**
	 * Constructor for the control cell label provider.
	 * 
	 * @param attributeMaps A map of the attributes that this cell will observe.
	 */
	protected ControlCellLabelProvider(IObservableMap<TRow, ?>[] attributeMaps) {
		super(attributeMaps);
	}
	
	/**
	 * Gets the control out of the cell, adds one if none exist already.
	 * @param cell The cell to add the control to
	 * @param style The style to create the new control with
	 * @return The control that the cell contains
	 */
	protected T getControl(ViewerCell cell, int style) {
	    return cellControls.containsKey(cell.getViewerRow()) ? cellControls.get(
	            cell.getViewerRow()) : create(cell, style);
	}
	
	/**
	 * Dispose of the cell along with it's control.
	 */
	@Override
    public void dispose() {
		for (Control control : cellControls.values()) {
			control.dispose();
		}
		
		for (TableEditor editor : cellEditors.values()) {
			editor.dispose();
		}
		
		cellControls.clear();
	}
	
	/**
	 * Creates  the control for a cell.
	 * @param cell The cell to add the control to
	 * @param style The style to create the new control with
	 * @return The control created
	 */
    protected abstract T createControl(ViewerCell cell, int style);
	
	private T create(final ViewerCell cell, int style) {
		T control = createControl(cell, style);
		
		setEditor(cell, control);
		cellControls.put(cell.getViewerRow(), control);
		
		return control;
	}
	
	/**
	 * Get the control from a cell as a composite.
	 * @param cell - The cell to get the control of
	 * @return The control as a composite.
	 */
	protected Composite composite(ViewerCell cell) {
		return (Composite) cell.getControl();
	}
	
	/**
	 * Set the editor for a cell.
	 * @param cell - The cell to edit.
	 * @param control - The control for the cell.
	 */
	protected void setEditor(ViewerCell cell, Control control) {
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

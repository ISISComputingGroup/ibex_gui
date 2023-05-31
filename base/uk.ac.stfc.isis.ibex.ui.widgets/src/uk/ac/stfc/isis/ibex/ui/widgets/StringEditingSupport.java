
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Editing support for string-type values.
 *
 * @param <TRow> the type of row being edited
 */
public abstract class StringEditingSupport<TRow> extends GenericEditingSupport<TRow, String> {

	private final ResetSelectionTextCellEditor editor;
	/**
	 * Tracks whether editing has been disabled.
	 */
	protected boolean canEdit = true;

	/**
	 * Create this editing support.
	 * @param viewer the column viewer
	 * @param rowType the row type
	 */
	public StringEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, String.class);
		editor = createTextCellEditor(viewer);
	}
	
	/**
	 * Sets whether this editor is enabled.
	 * @param enabled true to enable; false otherwise
	 */
	public void setEnabled(boolean enabled) {
		canEdit = enabled;
	}
	
    private ResetSelectionTextCellEditor createTextCellEditor(ColumnViewer viewer) {
        return new ResetSelectionTextCellEditor((Composite) viewer.getControl()) {
            @Override
            protected void onModifyEvent(ModifyEvent e, String newValue) { 
                onModify(e, newValue);
            }
        };
    }

    /**
     * Return to the selection the editor was at when it lost focus.
     */
    public void resetSelectionAfterFocus() {
        editor.resetSelectionAfterFocus();
    }
    
    /**
     * Called when the editor is modified.
     * @param e The modification event
     * @param newValue The new text of the editor
     */
    protected void onModify(ModifyEvent e, String newValue) { }
    
	@Override
	protected boolean canEdit(Object element) {
		return canEdit;
	}
	
	@Override
	protected String valueFromString(String value) {
		return value;
	}

	@Override
	protected CellEditor getCellEditor(Object arg0) {
		return editor;
	}
}

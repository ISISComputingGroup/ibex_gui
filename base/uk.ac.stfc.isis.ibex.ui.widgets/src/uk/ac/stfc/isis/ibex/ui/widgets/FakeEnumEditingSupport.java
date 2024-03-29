
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

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Editing support for enum types.
 *
 * @param <TRow> the type of row which is edited
 */
public abstract class FakeEnumEditingSupport<TRow> extends EditingSupport {
	private ComboBoxViewerCellEditor cellEditor;	
	private final Class<TRow> rowType;
	/**
	 * Create new editing support for an enum type.
	 * @param viewer the column viewer
	 * @param rowType the type of row
	 * @param enumValues the list of values the dropdown should have
	 */
	public FakeEnumEditingSupport(ColumnViewer viewer, Class<TRow> rowType, List<String> enumValues) {
		super(viewer);
		this.rowType = rowType;
		cellEditor = new ComboBoxViewerCellEditor((Composite) viewer.getControl());
		cellEditor.setActivationStyle(ComboBoxViewerCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);	
		cellEditor.setStyle(SWT.DROP_DOWN);


        cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContentProvider(new ArrayContentProvider());

        List<String> options = enumValues;

        cellEditor.setInput(options);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}
	
	@Override
	protected Object getValue(Object element) {
		TRow row = rowType.cast(element);
		return getEnumValueForRow(row);
	}
	
	@Override
	protected void setValue(Object element, Object value) {
		if (rowType.isInstance(element)) {
			TRow row = rowType.cast(element);
			String newValue = (String) value;
			
			if (getEnumValueForRow(row) !=  newValue) {
				setEnumForRow(row,  newValue);
				this.getViewer().update(element, null);
			}
		}
	}
	
	/**
	 * Gets the enum value for a given row.
	 * @param row the row
	 * @return the enum value
	 */
	protected abstract String getEnumValueForRow(TRow row);

	/**
	 * Sets the enum value for a given row.
	 * @param row the row
	 * @param value the enum value
	 */
	protected abstract void setEnumForRow(TRow row, String value);
}

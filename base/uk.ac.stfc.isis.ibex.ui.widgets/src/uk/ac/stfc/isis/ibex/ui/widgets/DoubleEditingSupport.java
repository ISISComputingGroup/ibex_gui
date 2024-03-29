
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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Provides support for editing double values within a table.
 * 
 * @param <TRow> the row type
 */
public abstract class DoubleEditingSupport<TRow> extends GenericEditingSupport<TRow, Double> {

	/**
	 * The cell editor.
	 */
	protected CellEditor editor;
	
	/**
	 * Create a new instance of this class.
	 * @param viewer the column viewer
	 * @param rowType the row type
	 */
	public DoubleEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, Double.class);
		createEditor(viewer);
	}
	
	private void createEditor(ColumnViewer viewer) { 		
	// Override TextCellEditor to handle doubles better
		editor = new TextCellEditor((Composite) viewer.getControl()) {
			@Override
			protected void doSetValue(final Object value) {
				if (value == null) {
					// If value is null set to zero instead
					super.doSetValue(String.valueOf(Double.valueOf(0)));
				} else {
					super.doSetValue(String.valueOf(value.toString()));
				}
			}
		};
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}
	
	@Override
	protected Double valueFromString(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
}

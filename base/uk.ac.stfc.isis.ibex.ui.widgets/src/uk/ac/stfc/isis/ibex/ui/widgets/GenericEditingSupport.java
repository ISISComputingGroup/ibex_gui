
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

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

public abstract class GenericEditingSupport<TRow, T> extends EditingSupport {

	private final ColumnViewer viewer;
	private final Class<TRow> rowType;
	private final Class<T> valueType;
	
	public GenericEditingSupport(ColumnViewer viewer, Class<TRow> rowType, Class<T> valueType) {
		super(viewer);
		this.viewer = viewer;
		this.rowType = rowType;
		this.valueType = valueType;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (!rowType.isInstance(element)) {
			return null;
		}
		
		TRow row = rowType.cast(element);
		return valueFromRow(row);		
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (!rowType.isInstance(element)) {
			return;
		}
		
		TRow row = rowType.cast(element);
		T newValue = null;
		if (valueType.isInstance(value)) {
			newValue = valueType.cast(value);
		} else if (value instanceof String) {
			newValue = valueFromString((String) value);
		} else {
			return;
		}		
		
		if (newValue == null) {
			return;
		}
		
		T rowValue = valueFromRow(row);
		if (rowValue == null || rowValue.equals(newValue)) {
			return;
		}
		
		setValueForRow(row, newValue);
		viewer.update(element, null);
	}
	
	protected abstract T valueFromString(String value);

	protected abstract T valueFromRow(TRow row);

	protected abstract void setValueForRow(TRow row, T value);

}

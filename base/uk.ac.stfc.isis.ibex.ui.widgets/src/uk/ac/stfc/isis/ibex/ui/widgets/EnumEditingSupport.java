
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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class EnumEditingSupport<T, E extends Enum<E>> extends EditingSupport {
	private ComboBoxViewerCellEditor cellEditor;	
	
	private final Class<T> rowType;
	private final Class<E> enumType;
	
	public EnumEditingSupport(ColumnViewer viewer, Class<T> rowType, Class<E> enumType) {
		super(viewer);
		this.rowType = rowType;
		this.enumType = enumType;
		
		cellEditor = new ComboBoxViewerCellEditor((Composite) viewer.getControl(), SWT.READ_ONLY);
        cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContentProvider(new ArrayContentProvider());

        E[] options = enumType.getEnumConstants();
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
		if (enumType.isInstance(element)) {	
			E enumValue =  enumType.cast(element);
			return enumValue;
		}
		
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (rowType.isInstance(element) && enumType.isInstance(value)) {
			T row = rowType.cast(element);
			E newEnumValue = enumType.cast(value);
			
			if (getEnumValueForRow(row) != newEnumValue) {
				setEnumForRow(row, newEnumValue);
				this.getViewer().update(element, null);
			}
		}
	}

	protected abstract E getEnumValueForRow(T row);

	protected abstract void setEnumForRow(T row, E value);
}

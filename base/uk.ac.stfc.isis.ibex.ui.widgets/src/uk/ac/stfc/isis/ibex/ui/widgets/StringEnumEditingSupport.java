
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
 * @param <T> the type of row which is edited
 * @param <E> the enum type which is edited (within a cell)
 */
public abstract class EnumEditingSupport<T, E extends Enum<E>> extends EditingSupport {
	private ComboBoxViewerCellEditor cellEditor;	
	
	private final Class<T> rowType;
	private final List<String> enumValues;
	
	/**
	 * Create new editing support for an enum type.
	 * @param viewer the column viewer
	 * @param rowType the type of row
	 * @param enumValues the enum type which is edited within a cell
	 */
	public EnumEditingSupport(ColumnViewer viewer, Class<T> rowType, List<String> enumValues) {
		super(viewer);
		this.rowType = rowType;
		this.enumValues = enumValues;
		
		cellEditor = new ComboBoxViewerCellEditor((Composite) viewer.getControl(), SWT.READ_ONLY);
        cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContentProvider(new ArrayContentProvider());

        E[] options = enumValues.getEnumConstants();
        cellEditor.setInput(options);
	}

    // write all the code for the abstract methods that failcitate the enum working but the enum is actualy jus  a list of strings
    @Override
    protected CellEditor getCellEditor(Object element) {
        return cellEditor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

}

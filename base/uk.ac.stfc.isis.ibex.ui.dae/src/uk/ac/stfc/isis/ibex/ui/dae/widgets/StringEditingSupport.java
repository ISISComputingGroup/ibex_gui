
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

package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.widgets.GenericEditingSupport;

/**
 * Editing support for string-type values.
 *
 * @param <TRow> the type of row being edited
 */
public abstract class StringEditingSupport<TRow> extends GenericEditingSupport<TRow, String> {

	private ColumnViewer viewer;

	/**
	 * Create this editing support.
	 * @param viewer the column viewer
	 * @param rowType the row type
	 */
	public StringEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, String.class);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor((Composite) viewer.getControl());
	}
	
	@Override
	protected String valueFromString(String text) {
		return text;
	}
}

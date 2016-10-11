
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.TextCellEditor;

/***
 * A double editing support that displays an empty string if 
 * the value is null.
 *
 * @param <TRow> the type of row from a table
 */
public abstract class DoubleEditingSupportBlankIfNull<TRow> extends DoubleEditingSupport<TRow> {
	
	public DoubleEditingSupportBlankIfNull(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType);
	}
	
	protected void createEditor(ColumnViewer viewer) { 		
		// Override TextCellEditor to handle doubles better
		editor = new TextCellEditor((Composite) viewer.getControl()) {
			@Override
			protected void doSetValue(final Object value) {
				if (value == null) {
					// If value is null set to an empty string
					super.doSetValue("");
				} else {
					// Otherwise set the value
					super.doSetValue(String.valueOf(value.toString()));
				}
			}
		};
	}
}

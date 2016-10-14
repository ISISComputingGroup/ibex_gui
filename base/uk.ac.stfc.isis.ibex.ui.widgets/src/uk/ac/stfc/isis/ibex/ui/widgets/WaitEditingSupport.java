
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
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.WaitUnit;

/**
 * Editing support for the script generator's wait unit enums. 
 */
public abstract class WaitEditingSupport extends EnumEditingSupport<Row, WaitUnit> {
	/** 
	 * The default constructor.
	 * @param viewer the viewer
	 */
	public WaitEditingSupport(ColumnViewer viewer) {
		super(viewer, Row.class, WaitUnit.class);
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {		
		CellEditor editor = super.getCellEditor(element);
		editor.setStyle(SWT.READ_ONLY);
		
		return editor;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}
}

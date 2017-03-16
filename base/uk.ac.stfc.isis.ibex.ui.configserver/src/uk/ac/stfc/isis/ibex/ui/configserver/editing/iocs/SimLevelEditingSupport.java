
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

/**
 * Class to provide editing support for modifying the sim level of an IOC whilst
 * it is in a table.
 */
public class SimLevelEditingSupport extends EnumEditingSupport<EditableIoc, SimLevel> {

    /**
     * The default constructor.
     * 
     * @param viewer
     *            The column that this editing support applies to.
     */
	public SimLevelEditingSupport(ColumnViewer viewer) {
		super(viewer, EditableIoc.class, SimLevel.class);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {		
		CellEditor editor = super.getCellEditor(element);
		editor.setStyle(SWT.READ_ONLY);
		
		return editor;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		EditableIoc row = EditableIoc.class.cast(element);
		return row.isEditable();
	}
	
	@Override
	protected SimLevel getEnumValueForRow(EditableIoc row) {
		return row.getSimLevel();
	}

	@Override
	protected void setEnumForRow(EditableIoc row, SimLevel value) {
		row.setSimLevel(value);
	}
}

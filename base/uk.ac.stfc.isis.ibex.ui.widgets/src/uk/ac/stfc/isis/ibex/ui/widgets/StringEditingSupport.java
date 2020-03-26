
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
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;

public abstract class StringEditingSupport<TRow> extends GenericEditingSupport<TRow, String> {

	private final TextCellEditor editor;
	private boolean canEdit = true;

	public StringEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, String.class);
		editor = createTextCellEditor(viewer);
	}
	
	public void setEnabled(boolean enabled) {
		canEdit = enabled;
	}
	
    private TextCellEditor createTextCellEditor(ColumnViewer viewer) {
        return new TextCellEditor((Composite) viewer.getControl()) {
            @Override
            protected void editOccured(ModifyEvent e) {
                super.editOccured(e);
                onModify(e, text.getText());
            }
        };
    }

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

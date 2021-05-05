
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

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * A TextCellEditor that remembers it's selection when focus is removed 
 * and provides the ability to go back to that selection. Required to 
 * fix https://github.com/ISISComputingGroup/IBEX/issues/5708.
 */
public abstract class ResetSelectionTextCellEditor extends TextCellEditor {

	private Point selectionWhenFocusLost;

	/**
	 * Creates the TextCellEditor.
	 * @param composite The composite to create the editor in.
	 */
	public ResetSelectionTextCellEditor(Composite composite) {
		super(composite);
	}
	
    @Override
    protected void editOccured(ModifyEvent e) {
        super.editOccured(e);
        onModifyEvent(e, text.getText());
    }
    
    @Override
    protected void focusLost() {
        if (text != null) {
            selectionWhenFocusLost = text.getSelection();
            super.focusLost();
        }
    }
    
    /**
     * Return to the selection the editor was at when it lost focus.
     */
    public void resetSelectionAfterFocus() {
        if (selectionWhenFocusLost != null) {
            text.setSelection(selectionWhenFocusLost);
        }
    }
    
    /**
     * Called when the editor is modified.
     * @param e The modification event
     * @param newValue The new text of the editor
     */
    abstract void onModifyEvent(ModifyEvent e, String newValue);
    
}

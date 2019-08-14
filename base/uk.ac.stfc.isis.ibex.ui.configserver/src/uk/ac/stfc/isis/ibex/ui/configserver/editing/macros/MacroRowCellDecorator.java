
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;

/**
 * The cell decorator used to display values in grey if they are default.
 */
public class MacroRowCellDecorator extends CellDecorator<Macro> {
	
	private static final Color READONLY_COLOR = ResourceManager.getColor(SWT.COLOR_DARK_GRAY);
	private static final Color DEFAULT_COLOR = ResourceManager.getColor(SWT.COLOR_BLACK);
	
	@Override
	public void applyDecoration(ViewerCell cell) {
    	if (getRow(cell).getUseDefault()) {
    		cell.setForeground(READONLY_COLOR);
    	} else {
    	    cell.setForeground(DEFAULT_COLOR);
    	}
   	}
	
}

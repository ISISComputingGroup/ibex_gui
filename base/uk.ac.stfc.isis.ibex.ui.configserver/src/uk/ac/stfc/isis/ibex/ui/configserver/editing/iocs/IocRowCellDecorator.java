
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

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;

public class IocRowCellDecorator extends CellDecorator<EditableIoc> {
	
	private static final Color READONLY_COLOR = ResourceManager.getColor(SWT.COLOR_DARK_GRAY);
	
	@Override
	public void applyDecoration(ViewerCell cell) {
    	if (!isEditable(cell)) {
    		cell.setForeground(READONLY_COLOR);
    		italicise(cell);
    	}
   	}

	public boolean isEditable(ViewerCell cell) {
		return getRow(cell).isEditable();
	}
	
	private static void italicise(ViewerCell cell) {
		modifyFont(cell, SWT.ITALIC);
	}
	
	private static void modifyFont(ViewerCell cell, int modifier) {
		FontData fontData = cell.getFont().getFontData()[0];
        Font font =
                SWTResourceManager.getFont(fontData.getName(), fontData.getHeight(), fontData.getStyle() | modifier);
		cell.setFont(font);
	}
	
}

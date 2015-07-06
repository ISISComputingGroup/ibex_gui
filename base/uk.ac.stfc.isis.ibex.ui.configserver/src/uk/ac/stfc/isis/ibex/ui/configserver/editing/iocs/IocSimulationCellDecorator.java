
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;

public class IocSimulationCellDecorator extends CellDecorator<EditableIoc> {

	private static final Display display = Display.getCurrent();

	@Override
	public void applyDecoration(ViewerCell cell) {
    	if (!isSimulating(cell)) {
    		unbold(cell);
    		return;
    	}

    	bold(cell);
		addSimulationLabel(cell);
	}

	private boolean isSimulating(ViewerCell cell) {
		return getRow(cell).getSimLevel() != SimLevel.NONE;
	}
	
	private static void bold(ViewerCell cell) {
		modifyFont(cell, SWT.BOLD, true);
	}
	
	private void unbold(ViewerCell cell) {
		modifyFont(cell, SWT.BOLD, false);
	}
	
	private static void addSimulationLabel(ViewerCell cell) {
		String text = cell.getText();
		cell.setText(text + " (SIM)");
	}
	
	private static void modifyFont(ViewerCell cell, int modifier, boolean enable) {
		FontData fontData = cell.getFont().getFontData()[0];
		int newStyle = enable ? fontData.getStyle() | modifier : fontData.getStyle() & ~modifier;
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), newStyle));
		cell.setFont(font);
	}
}

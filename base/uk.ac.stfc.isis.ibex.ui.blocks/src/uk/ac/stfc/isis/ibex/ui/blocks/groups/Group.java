
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

public class Group extends Composite {

	private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
	Composite parent;
			
	public Group(Composite parent, int style, DisplayGroup group, boolean showHiddenBlocks) {
		super(parent, style | SWT.BORDER);
		this.parent = parent;
		
        RowLayout layout = new RowLayout();
        layout.wrap = true;
        layout.type = SWT.VERTICAL;
        this.setLayout(layout);
        this.setBackground(WHITE);
        
		Label title = new Label(this, SWT.CENTER);
		title.setText(group.name());
		title.setBackground(WHITE);
		
		Font titleFont = getEditedLabelFont(parent, title, 10, SWT.BOLD);
		title.setFont(titleFont);

		for(DisplayBlock block : group.blocks()) {
			if (block.getIsVisible() || showHiddenBlocks) {
				Label blockName = new Label(this, SWT.LEFT);
				blockName.setText(block.getName() + ": " + block.getValue());
				blockName.setBackground(WHITE);
				blockName.setToolTipText(block.getDescription());
				blockName.setMenu(new BlocksMenu(block).createContextMenu(blockName));
			}
		}
	}
	
	private Font getEditedLabelFont(Composite parent, Label label, int size, int style) {
		FontData[] fD = label.getFont().getFontData();
		fD[0].setHeight(size);
		fD[0].setStyle(style);
		return new Font(parent.getDisplay(), fD[0]);
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
//		TODO: Implement this if needed
	}
}

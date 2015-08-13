
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

public class Group extends Composite {

	private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
	private static final int NUMBER_OF_ROWS = 9;
	
	Composite parent;
			
	public Group(Composite parent, int style, DisplayGroup group, boolean showHiddenBlocks) {
		super(parent, style | SWT.BORDER);
		this.parent = parent;
		
		List<DisplayBlock> blocksList = new ArrayList<>(group.blocks());
		int numberOfColumns = (blocksList.size() - 1) / NUMBER_OF_ROWS + 1 ;

		GridLayout layout = new GridLayout(2 * numberOfColumns, false);
		this.setLayout(layout);
        this.setBackground(WHITE);
        		
		Label title = new Label(this, SWT.CENTER);
		title.setText(group.name());
		title.setBackground(WHITE);
		Font titleFont = getEditedLabelFont(parent, title, 10, SWT.BOLD);
		title.setFont(titleFont);
		
		for (int i = 0; i < 1 + (numberOfColumns - 1) * 2; i++) {
			Label blankTitle = new Label(this, SWT.CENTER);
			blankTitle.setText("");
			blankTitle.setBackground(WHITE);
			blankTitle.setFont(titleFont);
		}
		
		DataBindingContext bindingContext = new DataBindingContext();
		
		for (int i = 0; i < numberOfColumns; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				int position = i * NUMBER_OF_ROWS + j;
				
				if (position >= blocksList.size()) {
					break;
				}
				
				DisplayBlock currentBlock = blocksList.get(position);
				
				Label blockName = new Label(this, SWT.RIGHT);
				blockName.setText(currentBlock.getName() + ": ");
				blockName.setBackground(WHITE);
				blockName.setToolTipText(currentBlock.getDescription());
				blockName.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
				blockName.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
				
				Label blockValue = new Label(this, SWT.RIGHT);
				blockValue.setText(currentBlock.getValue());
				blockValue.setBackground(WHITE);
				blockValue.setToolTipText(currentBlock.getDescription());
				blockValue.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
				GridData gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1);
				gridData.widthHint = 100;
				blockValue.setLayoutData(gridData);
				
				bindingContext.bindValue(WidgetProperties.text().observe(blockValue), BeanProperties.value("value").observe(currentBlock));
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

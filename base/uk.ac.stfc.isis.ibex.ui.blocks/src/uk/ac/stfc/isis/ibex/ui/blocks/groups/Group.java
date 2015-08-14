
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


/**
 * Provides the display of the groups, which contain the selected blocks. Allows showing and hiding of selected blocks. 
 * 
 */
public class Group extends Composite {

	private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
	private static final int NUMBER_OF_ROWS = 8;
	
	Composite parent;
			
	public Group(Composite parent, int style, DisplayGroup group, boolean showHiddenBlocks) {
		super(parent, style | SWT.BORDER);
		this.parent = parent;
		
		// Add the blocks to the list if they are visible, or if showHiddenBlocks is true
		List<DisplayBlock> blocksList = new ArrayList<>();
		for (DisplayBlock block : group.blocks()) {
			if (block.getIsVisible() || showHiddenBlocks) {
				blocksList.add(block);
			}
		}
		
		// Calculate number of columns we need, each column holding one block with a name and value
		int numberOfColumns = (blocksList.size() - 1) / NUMBER_OF_ROWS + 1;

		// For each block we need two columns in the grid layout, one for name, one for value, and for
		// every column but the last we need a divider label column
		GridLayout layout = new GridLayout(2 * numberOfColumns + (numberOfColumns - 1), false);
		layout.verticalSpacing = 7;
		this.setLayout(layout);
        this.setBackground(WHITE);
        
        // In the first column put the title in
		Label title = labelMaker(this, SWT.NONE, group.name(), "", null);
		Font titleFont = getEditedLabelFont(parent, title, 10, SWT.BOLD);
		title.setFont(titleFont);
		
		// For the title row, fill with blanks
		for (int i = 0; i < 1 + (numberOfColumns - 1) * 2 + (numberOfColumns - 1); i++) {
			labelMaker(this, SWT.NONE, "", "", titleFont);
		}
		
		DataBindingContext bindingContext = new DataBindingContext();
		
		// Loop over the rows and columns. The GridLayout is filled with labels across rows first, then moving on to
		// the next column. So blank labels need to be inserted so that columns are always filled.
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				int position = i + j * NUMBER_OF_ROWS;
				
				if (position >= blocksList.size()) {
					// put blank labels in these name and value columns
					labelMaker(this, SWT.NONE, "", "", null);
					labelMaker(this, SWT.NONE, "", "", null);					
					break;
				}
								
				DisplayBlock currentBlock = blocksList.get(position);

				Label blockName = labelMaker(this, SWT.NONE, currentBlock.getName() + ": ", currentBlock.getDescription(), null);
				blockName.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
				blockName.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
				
				Label blockValue = labelMaker(this, SWT.RIGHT, currentBlock.getValue(), currentBlock.getDescription(), null);
				blockValue.setMenu(new BlocksMenu(currentBlock).createContextMenu(blockName));
				GridData gridData = new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1);
				gridData.widthHint = 70;
				blockValue.setLayoutData(gridData);

				if (j <  numberOfColumns - 1) {
					// insert divider label
					labelMaker(this, SWT.NONE, "   |   ", "", null);
				}
				
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
	
	private Label labelMaker(Composite composite, int style, String text, String toolTip, Font font) {
		Label label = new Label(composite, style);
		label.setText(text);
		label.setBackground(WHITE);
		if (!toolTip.equals("")) {
			label.setToolTipText(toolTip);
		}
		
		if (font != null) {
			label.setFont(font);
		}
		
		return label;	
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
//		TODO: Implement this if needed
	}
}

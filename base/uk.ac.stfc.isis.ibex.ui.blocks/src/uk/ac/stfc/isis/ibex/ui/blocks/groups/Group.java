
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

/**
 * Provides the display of the groups, which contain the selected blocks. Allows
 * showing and hiding of selected blocks.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Group extends Composite {
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final int MIN_ROWS = 5;
    private static final int ROW_HEIGHT = 24;
    private Label title;
    
    private int numColumns;
    
    private List<DisplayBlock> blocksList;
    private GridLayout glGroup;
    

    /**
     * Provides the display of groups.
     * 
     * @param parent a widget which will be the parent of the new instance
     * @param style the style of widget to construct
     * @param group the group to be displayed
     * @param panel The panel that shows the groups and blocks. 
     */
    public Group(Composite parent, int style, DisplayGroup group, GroupsPanel panel) {
        super(parent, style | SWT.BORDER);

        // Add the blocks to the list if they are visible, or if
        // showHiddenBlocks is true
        blocksList = new ArrayList<>();
        for (DisplayBlock block : group.blocks()) {
            if (block.getIsVisible() || panel.showHiddenBlocks()) {
                blocksList.add(block);
            }
        }

        // For each block we need three columns in the grid layout, one for
        // name, one for value, one for
        // run control status, and for every column but the last we need a
        // divider label column
        GridLayout layout = new GridLayout(1, false);
        layout.verticalSpacing = 5;
        
        this.setLayout(layout);
        this.setBackground(WHITE);

        // In the first column put the title in
        title = labelMaker(this, SWT.NONE, group.name(), "", null);
        Font titleFont = getEditedLabelFont(title, 10, SWT.BOLD);
        title.setFont(titleFont);
        
        Composite groupBlocks = new Composite(this, SWT.NONE);

        // Loop over the rows and columns. The GridLayout is filled with labels
        // across rows first, then moving on to
        // the next column. So blank labels need to be inserted so that columns
        // are always filled.
        for (int i = 0; i < blocksList.size(); i++) {
            	
            	DisplayBlock currentBlock = blocksList.get(i);
                
                GroupRow row = new GroupRow(groupBlocks, SWT.NONE, currentBlock);
                
                GroupsMenu fullMenu = new GroupsMenu(panel, new BlocksMenu(currentBlock));
                row.setMenu(fullMenu.get());
        }
        glGroup = new GridLayout(computeNumColumns(this.getClientArea().height), false);
        groupBlocks.setLayout(glGroup);
        groupBlocks.setBackground(WHITE);

		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				final Composite source = (Composite) e.getSource();
				Rectangle r = source.getClientArea();
				glGroup.numColumns = computeNumColumns(r.height);
				Display.getDefault().asyncExec(new Runnable() {  
					@Override
		            public void run() {
						try {
							source.layout();
							source.pack();
							source.getParent().layout();
							source.getParent().pack();
						} catch (SWTException e) {
							// TODO handle this
						}
					}
				});
			}
		});
	}

    private int computeNumColumns(int height) {
        int numRows = Math.max(height / ROW_HEIGHT, MIN_ROWS);
        int numColumns = (blocksList.size() / numRows) + 1;
        return numColumns;
    }
    
    private Font getEditedLabelFont(Label label, int size, int style) {
        final String currentFontName = label.getFont().getFontData()[0].getName();
        return SWTResourceManager.getFont(currentFontName, size, style);
    }

    private Label labelMaker(Composite composite, int style, String text, String toolTip, Font font) {
        Label label = new Label(composite, style);
        if (text != null) {
            label.setText(text);
        }

        label.setBackground(WHITE);

        if (toolTip != null) {
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
        title.setMenu(menu);
    }
}

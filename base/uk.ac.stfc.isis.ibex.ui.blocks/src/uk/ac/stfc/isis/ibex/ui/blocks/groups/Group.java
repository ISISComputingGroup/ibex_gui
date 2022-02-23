
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
	private static final int TITLE_HEIGHT = 37;
	private static final int ROW_HEIGHT = 24;
	private static final int ROW_VERTICAL_SPACING = 5;
	private Button title;
	private Composite groupBlocks;

	private int numColumns;
	private int numRows;
	private boolean collapsed = false;

	private List<Control> rows;
	private List<DisplayBlock> blocksList;
	private GridLayout glGroup;

	/**
	 * Provides the display of groups.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance
	 * @param style
	 *            the style of widget to construct
	 * @param group
	 *            the group to be displayed
	 * @param panel
	 *            The panel that shows the groups and blocks.
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

		rows = new ArrayList<Control>();

		// For each block we need three columns in the grid layout, one for
		// name, one for value, one for
		// run control status, and for every column but the last we need a
		// divider label column
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = ROW_VERTICAL_SPACING;

		this.setLayout(layout);
		this.setBackground(WHITE);

		// In the first column put the title in
		/*
		title = labelMaker(this, SWT.NONE, group.name(), "", null);
		Font titleFont = getEditedLabelFont(title, 10, SWT.BOLD);
		title.setFont(titleFont);
		*/
		
		title = new Button(this, SWT.TOGGLE);
		title.setText(group.name());
		title.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source = (Button) e.getSource();
				if (source.getSelection()) {
					groupBlocks.setVisible(false);
					((GridData) groupBlocks.getLayoutData()).exclude = true;
					collapsed = true;
				} else {
					groupBlocks.setVisible(true);
					((GridData) groupBlocks.getLayoutData()).exclude = false;
					collapsed = false;
				}
				panel.notifyListeners(SWT.Resize, new Event());
			}
		});

		groupBlocks = new Composite(this, SWT.NONE);

		// Loop over the rows and columns. The GridLayout is filled with labels
		// across rows first, then moving on to
		// the next column. So blank labels need to be inserted so that columns
		// are always filled.
		for (int i = 0; i < blocksList.size(); i++) {

			DisplayBlock currentBlock = blocksList.get(i);

			GroupRow row = new GroupRow(groupBlocks, SWT.NONE, currentBlock);
			rows.add(row);

			GroupsMenu fullMenu = new GroupsMenu(panel, new BlocksMenu(currentBlock));
			row.setMenu(fullMenu.get());
		}
		// Add enough spacers to fill all empty spaces in the last column. These
		// are then simply hidden / unhidden as needed to prevent having to
		// add/remove elements to the layout post initialisation
		for (int i = 0; i < blocksList.size(); i++) {
			Label spacer = new Label(groupBlocks, SWT.NONE);
			GridData data = new GridData();
			data.exclude = true;
			spacer.setLayoutData(data);
			rows.add(spacer);
		}
		glGroup = new GridLayout(computeNumColumns(this.getClientArea().height), false);
		glGroup.horizontalSpacing = 20;
		groupBlocks.setLayout(glGroup);
		groupBlocks.setBackground(WHITE);
		groupBlocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
	}

	/**
	 * Rearranges the layout of elements in the group according to the new size of the composite.
	 */
	public void relayout() {
		Rectangle r = getClientArea();
		glGroup.numColumns = computeNumColumns(r.height);
		hideSpacers();
		reorderRows();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!isDisposed() && !getParent().isDisposed()) {
					getParent().getParent().layout();
					getParent().getParent().pack();
				}
			}
		});
	}

	/**
	 * Calculates how many columns are required to display all blocks based on the new height of the group composite.
	 * 
	 * @param height The height of the composite containing the blocks
	 * @return The required number of columns
	 */
	private int computeNumColumns(int height) {
		numRows = Math.max((height - TITLE_HEIGHT) / ROW_HEIGHT, 1);
		numColumns = (blocksList.size() / numRows) + 1;
		return numColumns;
	}
	
	/**
	 * Excludes / includes as many spacers as needed for layout considerations.
	 */
	private void hideSpacers() {
		int emptyCells = numRows * numColumns - blocksList.size();
		for (int i = blocksList.size(); i < rows.size(); i++) {
			GridData data = (GridData) rows.get(i).getLayoutData();
			if (i < blocksList.size() + emptyCells - 1) {
				data.exclude = false;
			} else {
				data.exclude = true;
			}
		}
	}

	/**
	 * Goes through group cells in order and finds the right block (or a spacer)
	 * to be moved there.
	 */
	private void reorderRows() {
		int maxRow = Math.min(numRows, rows.size());
		for (int row = 0; row < maxRow; row++) {
			for (int col = 0; col < numColumns; col++) {
				int pos = row + (numRows * col);
				Control prevElement = null;
				// First element never moves
				if (pos == 0) {
					continue;
				}
				if (col == 0) {
					// last element of previous row
					prevElement = rows.get((numColumns - 1) * numRows + (row - 1));
				} else {
					prevElement = rows.get(pos - numRows);
				}
				rows.get(pos).moveBelow(prevElement);
			}
		}
	}
	
	/**
	 * Calculates desired height of the group widget if all blocks were to be in one column.
	 * @return total height of the group
	 */
	public int getHeight() {
		if (collapsed) {
			return Group.TITLE_HEIGHT;
		}
		int heightPerRow = Group.ROW_HEIGHT + Group.ROW_VERTICAL_SPACING;
		return heightPerRow * blocksList.size() + Group.TITLE_HEIGHT + Group.ROW_VERTICAL_SPACING;
	}

	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		title.setMenu(menu);
	}
}

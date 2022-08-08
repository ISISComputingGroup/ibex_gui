
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
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;
import uk.ac.stfc.isis.ibex.epics.pv.PvState;

/**
 * Provides the display of the groups, which contain the selected blocks. Allows
 * showing and hiding of selected blocks.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Group extends Composite {
	private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private static final int TITLE_HEIGHT = 65;
	private static final int ROW_HEIGHT = 20;
	private static final int ROW_VERTICAL_SPACING = 5;
	
	private Composite titleContainer;
	private Button title;
	private Composite groupBlocks;

	private int numColumns = 1;
	private int numRows = 1;
	private boolean collapsed = false;

	private List<PvState> rowStatuses;
	private BlockStatusBorderColourConverter stateConverter = new BlockStatusBorderColourConverter();
	
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
	 * @param handlerService
	 * 			  The handler service to be passed down to BlocksMenu
	 */
	public Group(Composite parent, int style, DisplayGroup group, GroupsPanel panel, IHandlerService handlerService) {
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
		rowStatuses = new ArrayList<PvState>();
		for (int i = 0; i < blocksList.size(); i++) {
			rowStatuses.add(PvState.DEFAULT);
		}

		// For each block we need three columns in the grid layout, one for
		// name, one for value, one for
		// run control status, and for every column but the last we need a
		// divider label column
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = ROW_VERTICAL_SPACING;

		this.setLayout(layout);
		this.setBackground(WHITE);
		
		titleContainer = new Composite(this, SWT.NONE);
		GridLayout titleContainerLayout = new GridLayout(1, false);
		titleContainerLayout.marginWidth = 2;
		titleContainerLayout.marginHeight = 2;
		titleContainerLayout.verticalSpacing = 0;
		titleContainerLayout.horizontalSpacing = 0;
		titleContainer.setLayout(titleContainerLayout);
		
		title = new Button(titleContainer, SWT.TOGGLE);
		title.setText("   " + group.name());
		title.setImage(ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.blocks", "icons/minus.png"));
		title.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button source = (Button) e.getSource();
				if (source.getSelection()) {
					groupBlocks.setVisible(false);
					((GridData) groupBlocks.getLayoutData()).exclude = true;
					source.setImage(ResourceManager.getPluginImage(
							"uk.ac.stfc.isis.ibex.ui.blocks", "icons/plus.png"));
					collapsed = true;
				} else {
					groupBlocks.setVisible(true);
					((GridData) groupBlocks.getLayoutData()).exclude = false;
					source.setImage(ResourceManager.getPluginImage(
							"uk.ac.stfc.isis.ibex.ui.blocks", "icons/minus.png"));
					collapsed = false;
				}
				panel.notifyListeners(SWT.Resize, new Event());
			}
		});

		groupBlocks = new Composite(this, SWT.NONE);

		for (int i = 0; i < blocksList.size(); i++) {

			DisplayBlock currentBlock = blocksList.get(i);
			GroupRow row = new GroupRow(groupBlocks, SWT.NONE, currentBlock, i);

			GroupsMenu fullMenu = new GroupsMenu(panel, new BlocksMenu(currentBlock, handlerService));
			row.setMenu(fullMenu.get());
			row.getValueContainer().addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					Composite container = (Composite) e.widget;
					GroupRow row = (GroupRow) container.getParent();
					Group group = (Group) (row.getParent().getParent());
					rowStatuses.set(row.getRowIndex(), row.getBlockStatus());
					group.reloadTitleStatus();
				}
			});
			
			rows.add(row);
			
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
		
		glGroup = new GridLayout(1, false);
		glGroup.horizontalSpacing = 20;
		groupBlocks.setLayout(glGroup);
		groupBlocks.setBackground(WHITE);
		groupBlocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		relayout(this.getClientArea().height);
	}

	/**
	 * Changes title's border colour to match block with the most severe status in the group.
	 */
	protected void reloadTitleStatus() {
		PvState mostSevereState = PvState.DEFAULT;
		for (PvState state : rowStatuses) {
			if (state.compareTo(mostSevereState) > 0) {
				mostSevereState = state;
			}
		}
		titleContainer.setBackground(stateConverter.convert(mostSevereState));
	}

	/**
	 * Rearranges the layout of elements in the group according to the new size of the composite.
	 * @param groupHeight maximum allowed height of the group composite
	 */
	public void relayout(int groupHeight) {
		glGroup.numColumns = computeNumColumns(groupHeight);
		reorderRows();
		hideSpacers();
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
	 * Reorganises the rows to stack vertically first, rather than SWT default which is horizontally first.
	 */
	private void reorderRows() {	
		// Create new ordering list for rows
		ArrayList<Integer> indexes = new  ArrayList<Integer>();
		int currCol = 0;
		int currRow = 0;
		for (int i = 0; i < numColumns * numRows; i++) {
			if (currCol >= numColumns) {
				currCol = 0;
				currRow += 1;
			}
			indexes.add(currRow + currCol * numRows);
			currCol += 1;
		}
		
		// Apply the list
		Control prevRow = rows.get(0);
		Control nextRow;
		for (int i : indexes) {
			Optional<Control> row = getRow(i);
			if (row.isPresent()) {
				nextRow = row.get();
				nextRow.moveBelow(prevRow);
				GridData data = (GridData) nextRow.getLayoutData();
				data.exclude = false;
				prevRow = nextRow;
			}
		}
	}
	
	/**
	 * Hides unused spacers.
	 */
	private void hideSpacers() {
		int spacersIndex = numRows * numColumns;
		if (numColumns == 1) {
			spacersIndex = blocksList.size();
		}
		for (int i = spacersIndex; i < rows.size(); i++) {
			Control spacer = rows.get(i);
			GridData data = (GridData) spacer.getLayoutData();
			data.exclude = true;
		}
	}

	/**
	 * Helper method for optionally getting element in row list.
	 * @param index Where in the list to look for element
	 * @return Optional of found element or empty optional
	 */
	private Optional<Control> getRow(int index) {
		if (index >= 0 && index < rows.size()) {
			return Optional.of(rows.get(index));
		}
		return Optional.empty();
	}

	/**
	 * Calculates how many columns are required to display all blocks based on the new height of the group composite.
	 * 
	 * @param height The height of the composite containing the blocks
	 * @return The required number of columns
	 */
	private int computeNumColumns(int height) {
		numRows = Math.max((height - TITLE_HEIGHT + ROW_VERTICAL_SPACING) / (ROW_HEIGHT + ROW_VERTICAL_SPACING), 3);
		numColumns = (int) Math.ceil((float) blocksList.size() / (float) numRows);
		return numColumns;
	}
	
	/**
	 * Calculates desired height of the group widget if all blocks were to be in one column.
	 * @return total height of the group
	 */
	public int getHeight() {
		int rowHeight = ROW_HEIGHT + ROW_VERTICAL_SPACING;
		int titleHeight = TITLE_HEIGHT - ROW_VERTICAL_SPACING * 3;
		if (collapsed) {
			return titleHeight;
		}
		return blocksList.size() * rowHeight + titleHeight;
	}

	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		title.setMenu(menu);
	}
}

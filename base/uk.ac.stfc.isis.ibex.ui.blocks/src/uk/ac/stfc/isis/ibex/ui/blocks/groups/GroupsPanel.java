
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

/**
 * The panel that shows blocks and groups in the main IBEX perspective.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class GroupsPanel extends Composite {
	
	private final Display display = Display.getCurrent();
	private final List<Group> groups = new ArrayList<>();
	
	private Composite mainComposite;
	private ScrolledComposite scrolledComposite;
	private List<Composite> columns  = new ArrayList<>();
	private CLabel banner;
	private ConnectionStatus status = ConnectionStatus.EMPTY;
	
    private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	
    private static final Font MESSAGE_FONT = SWTResourceManager.getFont("Arial", 14, SWT.NORMAL);
    private static final String DISCONNECTED_MESSAGE = "IBEX SERVER DISCONNECTED\nPlease talk to your point of contact";
    private static final String CONNECTED_NO_GROUPS_MESSAGE =
            "IBEX SERVER CONNECTED:\nNo Groups Present\nNew Blocks can be added by going to\n Configuration->Edit Current Configuration->Blocks->Add Blocks";
    
    private enum ConnectionStatus {
    	EMPTY(-1), DISCONNECTED(0), CONNECTED_NO_GROUPS(1);
    	
    	ConnectionStatus(int status) {
    	}
    }
	
    private Menu contextMenu;
	
	private boolean showHiddenBlocks = false;
	private boolean sortGroupsBySize = false;
	private final IHandlerService handlerService;
	
	private Optional<List<DisplayGroup>> displayGroups;
	
	private static final int COLUMN_HEIGHT_BASE = 10;
    /**
     * Constructor for the groups panel.
     * 
     * @param parent
     *            The parent composite that this panel belongs to.
     * @param style
     *            The SWT style of the panel.
     * @param handlerService
	 * 			  The handler service to be passed down to BlocksMenu
     */
	public GroupsPanel(Composite parent, int style, IHandlerService handlerService) {
		super(parent, SWT.NONE);
		this.handlerService = handlerService;
		FillLayout fLayout = new FillLayout(SWT.HORIZONTAL);
		fLayout.marginHeight = 0;
		fLayout.marginWidth = 0;
		this.setLayout(fLayout);
		
		scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		setMainCompositeLayout(1);
		
		scrolledComposite.setContent(mainComposite);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinHeight(125);
		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Composite source = (Composite) e.getSource();
				GroupsPanel panel = (GroupsPanel) source;
				if (panel.status != ConnectionStatus.DISCONNECTED && panel.status != ConnectionStatus.CONNECTED_NO_GROUPS) {
					relayoutGroups(true, source.getClientArea().height);
				}
			}
		});
		
		configureMenu();

		showBanner(ConnectionStatus.EMPTY);

	}

	/**
	 * Set main composite's layout to be a new GridLayout with correct settings.
	 * @param columnCount How many columns the main composite's grid should hold
	 */
	private void setMainCompositeLayout(int columnCount) {
		mainComposite = new Composite(scrolledComposite, SWT.NONE);
		GridLayout glMain = new GridLayout(columnCount, false);
		glMain.marginHeight = 0;
		glMain.marginWidth = 0;
		glMain.horizontalSpacing = 0;
		mainComposite.setLayout(glMain);
	}

	private void configureMenu() {
		GroupsMenu menu = new GroupsMenu(this);
        contextMenu = menu.get();
		mainComposite.setMenu(contextMenu);
		scrolledComposite.setMenu(contextMenu);
	}
	
	private void selectBanner(final Optional<List<DisplayGroup>> groups) {
		clear();
		if (!groups.isPresent()) {
			showBanner(ConnectionStatus.DISCONNECTED);
			this.status = ConnectionStatus.DISCONNECTED;
			return;
		} else if (groups.isPresent() && groups.get().isEmpty()) {
			showBanner(ConnectionStatus.CONNECTED_NO_GROUPS);
			this.status = ConnectionStatus.CONNECTED_NO_GROUPS;
			return;
		} else {
			this.status = ConnectionStatus.EMPTY;
			addColumns();
			addGroups();
			assert (this.groups.size() == columns.size()) : "Table creation failed";
			relayoutGroups(false, 0);	// This is needed to calculate height of the panel used below
			relayoutGroups(true, this.getClientArea().height);
		}
	}

	/**
	 * Creates maximum needed amount of columns for main composite
	 */
    private void addColumns() {
		Optional<List<DisplayGroup>> visibleGroups = HiddenGroupFilter.getVisibleGroups(displayGroups, showHiddenBlocks);

		int groupCount = visibleGroups.orElseThrow(IllegalStateException::new).size();
		for (int i = 0; i < groupCount; i++) {
			columns.add(createColumn());
		}
	}

    /**
     * Creates a column composite that will hold the groups
     * @return Column composite
     */
	private Composite createColumn() {
		Composite column = new Composite(mainComposite, SWT.NONE);
		GridLayout gLayout = new GridLayout(1, false);
		gLayout.marginHeight = 0;
		gLayout.marginWidth = 2;
		column.setLayout(gLayout);
		column.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		column.setMenu(contextMenu);
		column.pack();
	
		return column;
	}
	
	/**
	 * Creates group widgets and assigns them to main composite by default
	 */
	private void addGroups() {
		Optional<List<DisplayGroup>> visibleGroups = HiddenGroupFilter.getVisibleGroups(displayGroups, showHiddenBlocks);
		for (DisplayGroup group : visibleGroups.orElseThrow(IllegalStateException::new)) {
		    groups.add(groupWidget(group, mainComposite));
		}
	}
	
	/**
	 * Creates a group composite
	 * @param group Group data to create widget from
	 * @param parent Widget's composite parent
	 * @return Created group widget
	 */
	private Group groupWidget(DisplayGroup group, Composite parent) {
        Group groupWidget = new Group(parent, SWT.NONE, group, this, this.handlerService);
		groupWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		groupWidget.pack();
        groupWidget.setMenu(contextMenu);

		return groupWidget;
	}
	
	/**
	 * Restructures panel's layout using columns
	 * @param stackVertically Whether vertical group stacking should be applied.
	 * False to add one group per column and thus stack groups horizontally only.
	 * @param windowHeight Height of the panel, needed for calculating how many groups
	 * can fit in a column.
	 */
	private void relayoutGroups(boolean stackVertically, int windowHeight) {
		assignGroups(stackVertically, windowHeight);
		layoutColumns(windowHeight);
	}

	/**
	 * Assigns groups to columns based on parameters.
	 * @param stackVertically Whether vertical group stacking should be applied.
	 * @param windowHeight Height of the panel, needed for calculating how many groups
	 * can fit in a column.
	 */
	private void assignGroups(boolean stackVertically, int windowHeight) {
		if (!stackVertically) {
			int j = Math.min(groups.size(), columns.size());
			for (int i = 0; i < j; i++) {
				groups.get(i).setParent(columns.get(i));
			}
		} else {
			List<Group> groups;
			if (sortGroupsBySize) {
				groups = getGroupsSortedBySize(this.groups, windowHeight);
			} else {
				groups = new ArrayList<Group>(this.groups);
			}
			for (Composite column : columns) {
				((GridData) column.getLayoutData()).exclude = true;
				int columnHeight = COLUMN_HEIGHT_BASE;
				while (columnHeight < windowHeight) {
					if (groups.size() == 0) {
						break;
					}
					Group group = groups.get(0);
					assert (group.getHeight() > 0) : "Invalid group widget";	//secures from infinite loops
					if (group.getHeight() + columnHeight > windowHeight && columnHeight != COLUMN_HEIGHT_BASE) {
						break;
					}
					group.setParent(column);
					((GridData) column.getLayoutData()).exclude = false;
					columnHeight += group.getHeight();
					groups.remove(0);
				}
			}
		}
	}
	
	/**
	 * Get a new list of groups ordered using a size-based algorithm.
	 * Algorithm's logic is to start with biggest group first and then
	 * continuously fill remaining space in column with smaller groups.
	 * @param groups List of groups to order
	 * @return Ordered list of groups
	 */
	private List<Group> getGroupsSortedBySize(List<Group> groups, int windowHeight) {
		// Get a group list sorted in descending order of size
		ArrayList<Group> groupsToSort = new ArrayList<Group>(groups);
		Collections.sort(groupsToSort, new Comparator<Group>() {
			@Override
			public int compare(Group o1, Group o2) {
				return o2.getHeight() - o1.getHeight();
			}
		});
		
		List<Group> returnGroups = new ArrayList<Group>();
		
		// Apply algorithm to find optimal order of groups in columns
		int lastGroupsSize = groupsToSort.size();
		while (groupsToSort.size() > 0) {
			int columnSpaceRemaining = windowHeight - COLUMN_HEIGHT_BASE;
			boolean firstGroupInColumn = true;
			
			for (int i = 0; i < groupsToSort.size(); i++) {
				Group nextGroup = groupsToSort.get(i);
				if (columnSpaceRemaining - nextGroup.getHeight() >= 0 || firstGroupInColumn) {
					returnGroups.add(nextGroup);
					columnSpaceRemaining -= nextGroup.getHeight();
					groupsToSort.remove(i);
					i -= 1;
					firstGroupInColumn = false;
				}
			}
			// This assertion prevents infinite loop and helps in debugging
			assert (lastGroupsSize < groupsToSort.size()) : "Algorithm Failure";
			lastGroupsSize = groupsToSort.size();
		}
		
		return returnGroups;
	}

	/**
	 * Orders panel's composites to change layout accordingly and reload
	 */
	private void layoutColumns(int windowHeight) {
		GridLayout glMain = new GridLayout(columns.size(), false);
		glMain.horizontalSpacing = 0;
		mainComposite.setLayout(glMain);
		scrolledComposite.setContent(mainComposite);
		for (Group group : groups) {
			group.relayout(windowHeight);
			group.pack(true);
		}
		for (Composite column : columns) {
			column.pack(true);
		}
		mainComposite.pack(true);

		mainComposite.layout(true, true);
	}

	/**
     * Get whether the hidden blocks are showing or not.
     * 
     * @return True if blocks are showing
     */
	public boolean showHiddenBlocks() {
		return showHiddenBlocks;
	}
	
    /**
     * Set whether hidden blocks are visible or not.
     * 
     * @param showHidden
     *            True to show hidden blocks, false to hide.
     */
	public void setShowHiddenBlocks(boolean showHidden) {
		if (showHiddenBlocks == showHidden) {
			return;
		}
		
		showHiddenBlocks = showHidden;

		selectBanner(displayGroups);
	}
	
	/**
	 * Get whether groups are sorted by size or by configuration order.
	 * 
	 * @return True if groups are sorted by size, otherwise false
	 */
	public boolean sortGroups() {
		return sortGroupsBySize;
	}
	
	/**
	 * Set groups to be ordered by size or by configuration order.
	 * 
	 * @param orderBySize True to set groups to be ordered by size or
	 * false to order them by configuration order
	 */
	public void setOrderGroups(boolean orderBySize) {
		sortGroupsBySize = orderBySize;

		selectBanner(displayGroups);
	}
	
	
	/**
	 * Updates the groups to display a new set of groups/blocks.
	 * 
	 * @param groups The new set of groups.
	 */
	public synchronized void updateGroups(final Optional<List<DisplayGroup>> groups) {
		
		this.displayGroups = groups;
		
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				makeBanner(groups);
			}

			private void makeBanner(final Optional<List<DisplayGroup>> groups) {
				selectBanner(groups);
			}
		});
	}
	
	private void showBanner(ConnectionStatus status) {
		banner = new CLabel(scrolledComposite, SWT.NONE);
		banner.setLeftMargin(50);
		banner.setFont(MESSAGE_FONT);		
		scrolledComposite.setContent(banner);
		banner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		banner.setAlignment(SWT.CENTER);
		if (status == ConnectionStatus.EMPTY) {
			banner.setText("");
		} else if (status == ConnectionStatus.CONNECTED_NO_GROUPS) {
			banner.setText(CONNECTED_NO_GROUPS_MESSAGE);
		} else if (status == ConnectionStatus.DISCONNECTED) {
			banner.setText(DISCONNECTED_MESSAGE);
			banner.setForeground(RED);
		}
		banner.pack();
	}
	
	private void clear() {
		if (banner != null) {
			banner.dispose();
		}
		
		// Disposing of the children one by one seems to sometimes fail, so dispose
		// the parent instead
		mainComposite.dispose();
		setMainCompositeLayout(1);
		configureMenu();
		
		columns.clear();
		groups.clear();
	} 
	
}

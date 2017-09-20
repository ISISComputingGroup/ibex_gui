
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
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

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
	private CLabel banner;
	
    private Menu contextMenu;
	
	private boolean showHiddenBlocks = false;
	
	private final int minHeight = 100;
	
	private Collection<DisplayGroup> displayGroups;
	
    /**
     * Constructor for the groups panel.
     * 
     * @param parent
     *            The parent composite that this panel belongs to.
     * @param style
     *            The SWT style of the panel.
     */
	public GroupsPanel(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		mainComposite = new Composite(scrolledComposite, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		scrolledComposite.setContent(mainComposite);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinHeight(125);
		
		configureMenu();
		//Leave text blank
		showBanner("");

	}

	private void configureMenu() {
		GroupsMenu menu = new GroupsMenu(this);
        contextMenu = menu.get();
		mainComposite.setMenu(contextMenu);
		scrolledComposite.setMenu(contextMenu);
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

		clear();
		addGroups();
		setRows();
		layoutGroups();
	}
	
	/**
	 * Updates the groups to display a new set of groups/blocks.
	 * 
	 * @param groups The new set of groups.
	 */
	public synchronized void updateGroups(final Collection<DisplayGroup> groups) {
		this.displayGroups = groups;
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				clear();
				if (groups.isEmpty()) {
					// Leave text blank
					showBanner("");
					return;
				}
				addGroups();
				setRows();
				layoutGroups();
			}
		});
	}

	private void showBanner(String text) {
		banner = new CLabel(scrolledComposite, SWT.NONE);
		banner.setLeftMargin(5);
		scrolledComposite.setContent(banner);
		banner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		banner.setText(text);
		banner.pack();
	}

	private void layoutGroups() {
		scrolledComposite.setContent(mainComposite);
		mainComposite.pack(true);
		for (Group group : groups) {
			group.pack(true);
		}

		mainComposite.layout(true, true);
	}
	
	private void addGroups() {
		for (DisplayGroup group : displayGroups) {
			groups.add(groupWidget(group));
		}
	}
	
	private void setRows() {
		mainComposite.setLayout(new GridLayout(groups.size(), false));
	}
	
	private Group groupWidget(DisplayGroup group) {
        Group groupWidget = new Group(mainComposite, SWT.NONE, group, this);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.minimumHeight = minHeight; // TODO right number
		groupWidget.setLayoutData(gd);
		groupWidget.pack();
        groupWidget.setMenu(contextMenu);
		
		groups.add(groupWidget);
		
		return groupWidget;
	}
	
	private void clear() {
		if (banner != null) {
			banner.dispose();
		}
		
		// Disposing of the children one by one seems to sometimes fail, so dispose
		// the parent instead
		mainComposite.dispose();
		mainComposite = new Composite(scrolledComposite, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		configureMenu();
		
		groups.clear();
	}
}

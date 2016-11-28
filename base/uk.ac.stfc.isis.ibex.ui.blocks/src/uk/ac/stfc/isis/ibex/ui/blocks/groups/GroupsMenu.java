
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

/**
 * Holds all the actions for the context menu for the Groups.
 */
public class GroupsMenu {

    private final MenuManager manager;
	private final GroupsPanel groups;
	
    private final IAction showAllBlocks = new Action("Show hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(true);
            manager.remove(hideBlocks.getId());
            manager.add(showAllBlocks);
		}
	};
	
	private final IAction hideBlocks = new Action("Hide hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(false);
            manager.remove(showAllBlocks.getId());
            manager.add(hideBlocks);
		}
	};

    /**
     * Constructor for the class when there is already a menu that we want to
     * add on to the end of. Sets up the menu manager to include show or hide
     * blocks.
     * 
     * @param groups
     *            The panel on which to hide/show blocks.
     * @param manager
     *            The pre-existing menu
     */
    public GroupsMenu(final GroupsPanel groups, final MenuManager manager) {
        this.manager = manager;
        this.groups = groups;

        if (groups.showHiddenBlocks()) {
            manager.add(hideBlocks);
        } else {
            manager.add(showAllBlocks);
        }
    }

    /**
     * Constructor for the class when there is no menu already. Sets up the menu
     * manager to include show or hide blocks.
     * 
     * @param groups
     *            The panel on which to hide/show blocks.
     */
    public GroupsMenu(final GroupsPanel groups) {
        this(groups, new MenuManager());
	}
	
    /**
     * Creates and returns a context menu for the GroupsPanel.
     * 
     * @return A context menu
     */
	public Menu get() {
		return manager.createContextMenu(groups);
	}
}

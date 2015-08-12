
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
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

public class GroupsMenu {

	private final MenuManager manager = new MenuManager();
	private final GroupsPanel groups;
	
	private final IAction showAllBlocks = new Action("Show hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(true);
		}
	};
	
	private final IAction hideBlocks = new Action("Don't show hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(false);
		}
	};

	
	public GroupsMenu(final GroupsPanel groups) {
		this.groups = groups;
		
		manager.addMenuListener(new IMenuListener() {		
			@Override
			public void menuAboutToShow(IMenuManager manager) {						
				showAllBlocks.setEnabled(!groups.showHiddenBlocks());				
				hideBlocks.setEnabled(!showAllBlocks.isEnabled());
			}
		});
		
		manager.add(showAllBlocks);
		manager.add(hideBlocks);
	}
	
	public Menu get() {
		return manager.createContextMenu(groups);
	}
}

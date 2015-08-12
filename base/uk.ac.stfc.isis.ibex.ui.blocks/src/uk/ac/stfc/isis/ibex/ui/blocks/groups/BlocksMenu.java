
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;

public class BlocksMenu extends MenuManager {
	
	private final DisplayBlock block;
	
	private final IAction displayHistory;
	private final PVHistoryPresenter pvHistoryPresenter = Presenter.getInstance().pvHistoryPresenter();
	
	public BlocksMenu(DisplayBlock displayBlock) {
		
		this.block = displayBlock;
		
		displayHistory = new Action("Display block history") {
			@Override
			public void run() {
				pvHistoryPresenter.displayHistory(block.blockServerAlias());
			}
		};
		
		add(displayHistory);
	}
	
	public Menu createContextMenu(Composite composite) {
		return super.createContextMenu(composite);
	}
}


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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.EditCurrentConfigHandler;

public class BlocksMenu extends MenuManager {
	
	private final DisplayBlock block;
	
	private final PVHistoryPresenter pvHistoryPresenter = Presenter.getInstance().pvHistoryPresenter();
	
	public BlocksMenu(DisplayBlock displayBlock) {
		
		this.block = displayBlock;
		
        IAction displayHistory = new Action("Display block history") {
			@Override
			public void run() {
				pvHistoryPresenter.displayHistory(block.blockServerAlias());
			}
		};
		
		add(displayHistory);

        IAction editBlock = new Action("Edit block") {
            @Override
            public void run() {
                EditCurrentConfigHandler editBlockHandler = new EditCurrentConfigHandler(block.getName());
                try {
                    editBlockHandler.execute(new ExecutionEvent());
                } catch (ExecutionException e) {
                    // do nothing
                }
            }
        };

        add(editBlock);
	}
	
	public Menu createContextMenu(Label label, GroupsMenu menu) {
		return super.createContextMenu(label);
	}
}

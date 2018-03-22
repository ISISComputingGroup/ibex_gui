
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
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveSwitcher;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;
import uk.ac.stfc.isis.ibex.ui.blocks.views.BlocksView;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.EditBlockHandler;

/**
 * The right-click menu for blocks in the dashboard.
 */
public class BlocksMenu extends MenuManager {
	
	private final DisplayBlock block;
	
    private static final String BLOCK_MENU_GROUP = "Block";

    private static final String EDIT_BLOCK_PREFIX = "Edit host ";
    private static final String COMPONENT_SUFFIX = "component";
    private static final String CONFIGURATION_SUFFIX = "configuration";
	private static final String LOGPLOTTER_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";

	private final IAction editBlockAction;
	
	private final PerspectiveSwitcher switcher = BlocksView.switcher;
	private final PVHistoryPresenter pvHistoryPresenter = Presenter.getInstance().pvHistoryPresenter();
	
	/**
	 * This is an inner anonymous class inherited from SameTypeWriter with added functionality
	 * for modifying the command if the underlying configuration PV cannot be written to.
	 */
	protected final SameTypeWriter<Configuration> readOnlyListener = new SameTypeWriter<Configuration>() {	
		@Override
		public void onCanWriteChanged(final boolean canWrite) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (canWrite) {
						if (find(editBlockAction.getId()) == null) {
                            appendToGroup(BLOCK_MENU_GROUP, editBlockAction);
						}
					} else {
						remove(editBlockAction.getId());
					}
				}
			});
		}	
	};
	
    /**
     * The constructor, creates the menu for when the specific block is right-clicked on.
     * 
     * @param displayBlock the selected block
     */
    public BlocksMenu(DisplayBlock displayBlock) {
		this.block = displayBlock;
		
		Configurations.getInstance().server().setCurrentConfig().subscribe(readOnlyListener);
		
        add(new GroupMarker(BLOCK_MENU_GROUP));

        final MenuManager logSubMenu = new MenuManager("Display block history...");
        logSubMenu.add(new Action("never shown entry") {
        	//needed if it's a submenu
        });
        // Allows the menu to be dynamic
        logSubMenu.setRemoveAllWhenShown(true);
        
        final IAction newPresenter = new Action("New Plot") {
			@Override
			public void run() {
				switcher.switchPerspective(LOGPLOTTER_ID);
				pvHistoryPresenter.newPresenter(block.blockServerAlias(), block.getName());			}
		};
		
        logSubMenu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				logSubMenu.add(newPresenter);
				for (final String plot : pvHistoryPresenter.getCurrentPresenters()) {
					logSubMenu.add(new Action("Add to " + plot + " plot"){
						@Override
						public void run() {
							pvHistoryPresenter.addToPresenter(block.blockServerAlias(), block.getName(), plot);
						}
					});
				}
			}
        });
		
        appendToGroup(BLOCK_MENU_GROUP, logSubMenu);

        String editBlockLabel = EDIT_BLOCK_PREFIX;
        if (this.block.hasComponent()) {
            editBlockLabel += COMPONENT_SUFFIX;
        } else {
            editBlockLabel += CONFIGURATION_SUFFIX;
        }
        editBlockAction = new Action(editBlockLabel) {
            @Override
            public void run() {
                new EditBlockHandler(block.getName()).execute(null); //TODO e4 migrate: This will be added as a command which includes a shell at that time make this correct                
            }
        };
	}
}

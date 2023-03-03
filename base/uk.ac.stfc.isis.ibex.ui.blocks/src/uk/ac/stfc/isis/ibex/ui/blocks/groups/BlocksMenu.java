
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
import java.util.HashMap;
import java.util.Optional;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.handlers.IHandlerService;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views.PerspectiveSwitcherView;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
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
    private static final String DISPLAY_BLOCK_HISTORY = "Display block history...";
    private static final String VIEW_RUN_CONTROL_SETTINGS = "View run control settings";
	private static final String LOGPLOTTER_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
	
	private static boolean canWrite = false;

	private MenuManager logSubMenu;
	private MenuManager noLogPlotterSubMenu;
	
	static {
		// Set a listener for the edit host configuration/component menu item based on server write access status.
		Configurations.getInstance().server().setCurrentConfig().addOnCanWriteChangeListener(canWrite -> BlocksMenu.canWrite = canWrite);
	}

	private IAction createAddToPlotAction(String plotName) {
		return new Action("Add to new axis") {
			@Override
			public void run() {
				if (canAddPlot()) {
					BlocksView.partService.switchPerspective(LOGPLOTTER_ID);
					Presenter.pvHistoryPresenter().addToDisplay(block.blockServerAlias(), block.getName(), plotName, Optional.empty());
				}
			}
		};
	}

	private IAction createAddToAxisAction(String plotName, String axisName) {
	    return new Action("Add to " + axisName + " axis") {
            @Override
            public void run() {
            	if (canAddPlot()) {
                    BlocksView.partService.switchPerspective(LOGPLOTTER_ID);
                    Presenter.pvHistoryPresenter().addToDisplay(block.blockServerAlias(), block.getName(), plotName, Optional.of(axisName));
            	}
            }
        };
	}

    /**
     * The constructor, creates the menu for when the specific block is right-clicked on.
     *
     * @param displayBlock the selected block
     * @param handlerService to get safe access to runcontrol command
     */
    public BlocksMenu(DisplayBlock displayBlock, IHandlerService handlerService) {
		this.block = displayBlock;
		
        add(new GroupMarker(BLOCK_MENU_GROUP));
        
        final IAction viewRunControlSettingsAction = new Action(VIEW_RUN_CONTROL_SETTINGS) {
        	// get run control command from handler service and execute 
        	// i.e. call new runcontrol window 
			@Override
			public void run() {
				try {
					handlerService.executeCommand("uk.ac.stfc.isis.ibex.e4.client.command.runcontrol", null);
				} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
					LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
				}
			}
		};
		appendToGroup(BLOCK_MENU_GROUP, viewRunControlSettingsAction);
        
        noLogPlotterSubMenu = new MenuManager(DISPLAY_BLOCK_HISTORY);
        noLogPlotterSubMenu.add(new Action("Enable log plotter perspective to add block to log plotter") { });
        appendToGroup(BLOCK_MENU_GROUP, noLogPlotterSubMenu);
        
        logSubMenu = new MenuManager(DISPLAY_BLOCK_HISTORY);
        logSubMenu.add(new Action("never shown entry") {
        	//needed if it's a submenu
        });
        // Allows the menu to be dynamic
        logSubMenu.setRemoveAllWhenShown(true);

        final IAction newPlotAction = new Action("New Plot") {
			@Override
			public void run() {
				if (canAddPlot()) {
					BlocksView.partService.switchPerspective(LOGPLOTTER_ID);
					Presenter.pvHistoryPresenter().newDisplay(block.blockServerAlias(), block.getName());
				}
			}
		};

        logSubMenu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				logSubMenu.add(newPlotAction);

				HashMap<String, ArrayList<String>> dataBrowserData = Presenter.pvHistoryPresenter().getPlotsAndAxes();
				for (String plotName : dataBrowserData.keySet()) {
				    MenuManager plotSubMenu = new MenuManager("Add to " + plotName + " plot...");

				    plotSubMenu.add(createAddToPlotAction(plotName));
				    dataBrowserData.get(plotName).stream().forEach(a -> plotSubMenu.add(createAddToAxisAction(plotName, a)));

				    logSubMenu.add(plotSubMenu);
				}
			}
        });

        appendToGroup(BLOCK_MENU_GROUP, logSubMenu);
        
        final var editBlockAction = createEditBlockLabelAndAction();
        
        appendToGroup(BLOCK_MENU_GROUP, editBlockAction);
        
        this.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (canAddPlot()) {
					logSubMenu.setVisible(true);
					noLogPlotterSubMenu.setVisible(false);
				} else {
					logSubMenu.setVisible(false);
					noLogPlotterSubMenu.setVisible(true);
				}
				editBlockAction.setEnabled(canWrite);
				updateAll(true);
			}
        });
	}

	private IAction createEditBlockLabelAndAction() {
		String editBlockLabel = EDIT_BLOCK_PREFIX;
        if (this.block.inComponent()) {
            editBlockLabel += COMPONENT_SUFFIX;
        } else {
            editBlockLabel += CONFIGURATION_SUFFIX;
        }
        
        return new Action(editBlockLabel) {
            @Override
            public void run() {
                new EditBlockHandler(block.getName()).execute(null); //TODO e4 migrate: This will be added as a command which includes a shell at that time make this correct
            }
        };
	}
    
    /**
     * Helper method for determining if a plot can be added safely. Plot cannot be added if
     * the log plotter perspective is hidden.
     * @return true if plot can be added, otherwise false
     */
    public static boolean canAddPlot() {
		PerspectivesProvider perspectivesProvider = new PerspectivesProvider(
				PerspectiveSwitcherView.app, PerspectiveSwitcherView.partService, PerspectiveSwitcherView.modelService);
		MPerspectiveStack perspectiveStack = perspectivesProvider.getTopLevelStack();
		for (MPerspective perspective : perspectiveStack.getChildren()) {
			String id = perspective.getElementId();
			if (id.equals(LOGPLOTTER_ID) && perspective.isVisible()) {
				return true;
			}
		}
		return false;
    }
}

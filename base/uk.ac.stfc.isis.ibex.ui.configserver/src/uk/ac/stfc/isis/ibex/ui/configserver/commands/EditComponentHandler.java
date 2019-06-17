
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.List;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.ui.configserver.BundleConstants;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ConfigHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditComponentHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.ViewComponentHelper;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;

/**
 * The handler class for editing components.
 * 
 * It sets the menu labels, and opens the dialogue for editing or viewing the
 * components.
 */
public class EditComponentHandler extends ConfigHandler<Configuration> {

    private static final String EDIT_MENU_TEXT = "Edit";
    private static final String READ_ONLY_TEXT = "View";
    private static final String MENU_MNEMONIC = "E";
    private static final String EDIT_TITLE = "Edit Component";
    private static final String VIEW_TITLE = "View Component";
    private static final String EDIT_TOOLTIP = "Select and edit an existing component";
    private static final String VIEW_TOOLTIP = "Select and view an existing component";

    private boolean canWrite;

    /**
     * This is an inner anonymous class will disable the menu item if the
     * components are not available.
     */
    private Observer<Configuration> configObserver = new BaseObserver<Configuration>() {

        @Override
        public void onConnectionStatus(boolean isConnected) {
            canViewOrEditConfig(isConnected);
        }

        @Override
        public void onError(Exception e) {
            canViewOrEditConfig(false);
        }

        private void canViewOrEditConfig(boolean isConnected) {
            setCanExecute(isConnected);
        }
    };

    /**
     * Create the handler for opening the editor via the menu.
     */
    public EditComponentHandler() {
        super(SERVER.saveAsComponent());
        SERVER.currentConfig().addObserver(configObserver);
    }

    /**
     * Execute the handler to open the given edit/view component dialogue.
     *
     * @param shell
     *            the shell to user
     */
    @Override
    public void safeExecute(Shell shell) {
        ConfigHelper helper;
        String titleText;
        if (canWrite) {
            helper = new EditComponentHelper(shell, SERVER);
            titleText = EDIT_TITLE;
        } else {
            helper = new ViewComponentHelper(shell);
            titleText = VIEW_TITLE;
        }

        ConfigSelectionDialog selectionDialog =
                new ConfigSelectionDialog(shell, titleText, SERVER.componentsInfo().getValue(), false, true);
        if (selectionDialog.open() == Window.OK) {
            String configName = selectionDialog.selectedConfig();
            helper.createDialog(configName, false);
        }
    }

    /**
     * Generate the menu item as the menu is about to be shown.
     * 
     * It must be dynamic because the menu has a different label depending on
     * the state of the config pv.
     * 
     * @param items
     *            menu items to add to
     */
    @AboutToShow
    public void aboutToShow(List<MMenuElement> items) {
        String menuText;
        String tooltipText;
        if (canWrite) {
            menuText = EDIT_MENU_TEXT;
            tooltipText = EDIT_TOOLTIP;
        } else {
            menuText = READ_ONLY_TEXT;
            tooltipText = VIEW_TOOLTIP;
        }

        MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();

        dynamicItem.setLabel(menuText);
        dynamicItem.setTooltip(tooltipText);
        dynamicItem.setMnemonics(MENU_MNEMONIC);
        dynamicItem.setContributorURI(BundleConstants.getPlatformPlugin()); // Plugin in which this menu item exists
        dynamicItem.setContributionURI(BundleConstants.getClassURI(EditComponentHandler.class));
        items.add(dynamicItem);
    }

    @Override
    public void canWriteChanged(final boolean canWrite) {
        this.canWrite = canWrite;
    }
}

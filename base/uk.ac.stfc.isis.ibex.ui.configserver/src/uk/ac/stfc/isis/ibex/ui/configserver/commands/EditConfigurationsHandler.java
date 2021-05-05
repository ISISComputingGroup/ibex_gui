
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

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.ui.configserver.BundleConstants;

/**
 * Class providing shared functionality for editing configurations and components.
 */
public abstract class EditConfigurationsHandler extends ConfigHandler<Configuration>  {
    /**
     * Whether the configuration can be written to.
     */
    protected boolean canWrite;
    
    private static final String EDIT_MENU_TEXT = "Edit";
    private static final String READ_ONLY_TEXT = "View";
    private static final String MENU_MNEMONIC = "E";
    
    /**
     * The dialog title when editing the config/component.
     */
    protected final String editTitle = "Edit " + getTypeString();
    
    /**
     * The dialog title when viewing the config/component.
     */
    protected final String viewTitle = "View " + getTypeString();
    
    private final String editTooltip = "Select and edit an existing " + getTypeString().toLowerCase();
    private final String viewTooltip = "Select and view an existing " + getTypeString().toLowerCase();

    /**
     * Constructor.
     * 
     * @param destination where to write the data to
     */
    public EditConfigurationsHandler(Writable<Configuration> destination) {
        super(destination);
    }
    
    /**
     * Abstract method for getting the name of the type.
     * @return the name of the type
     */
    public abstract String getTypeString();
    
    /**
     * This is an inner anonymous class will disable the menu item if the
     * configurations are not available.
     */
    protected Observer<Configuration> configObserver = new BaseObserver<Configuration>() {

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
     * {@inheritDoc}
     */
    @Override
    public void canWriteChanged(final boolean canWrite) {
        this.canWrite = canWrite;
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
            tooltipText = editTooltip;
        } else {
            menuText = READ_ONLY_TEXT;
            tooltipText = viewTooltip;
        }

        MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();

        dynamicItem.setLabel(menuText);
        dynamicItem.setTooltip(tooltipText);
        dynamicItem.setMnemonics(MENU_MNEMONIC);
        dynamicItem.setContributorURI(BundleConstants.getPlatformPlugin()); // Plugin in which this menu item exists
        dynamicItem.setContributionURI(BundleConstants.getClassURI(getClass()));
        items.add(dynamicItem);
    }
}

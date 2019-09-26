
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

/**
 *
 */
package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import java.util.concurrent.TimeoutException;

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigDetailsDialog;

/**
 * A helper class to open component viewing dialog boxes.
 */
public class ViewComponentHelper extends ConfigHelper {

    private static final String TITLE = "View Components";
    /**
     * Constructor for the helper class.
     *
     * @param shell
     *            The shell in which to display dialog boxes
     */
    public ViewComponentHelper(Shell shell) {
        this.shell = shell;
        this.configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
    }

    /**
     * Opens the dialog.
     *
     * @param component
     *            the component to edit
     * @param isCurrent
     *            whether it is the current configuration
     * @param editBlockFirst
     *            Open the dialog with blocks tab open
     */
    @Override
    protected void openDialog(EditableConfiguration component, boolean isCurrent, boolean editBlockFirst) {
        component.setIsComponent(true);
        final String componentName = getConfigDisplayName(component, false);
        final String subTitle = "Viewing the " + componentName + " component";

        ConfigDetailsDialog dialog =
                new ConfigDetailsDialog(shell, TITLE, subTitle, component, false, configurationViewModels);
        dialog.open();
    }

    /**
     * Create a dialog box for editing a component.
     *
     * @param componentName
     *            The name of the component we wish to edit
     * @param editBlockFirst
     *            Whether to present the blocks tab first
     * @throws TimeoutException
     */
    @Override
    public void createDialog(String componentName, boolean editBlockFirst) throws TimeoutException {
        openDialog(configurationViewModels.getComponent(componentName), false, editBlockFirst);
    }
}

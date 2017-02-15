 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

/**
 * A dialog for selecting components to delete, that add a warning icon if there
 * are dependencies associated to a component.
 */
public class DeleteComponentsDialog extends MultipleConfigsSelectionDialog {

    private Collection<String> compsInUse;

    /**
     * Constructor.
     * 
     * @param parentShell The parent shell
     * @param compsAvailable The list of available components
     * @param compsInUse The list of components that are used in configurations
     */
    public DeleteComponentsDialog(Shell parentShell, Collection<ConfigInfo> compsAvailable, Collection<String> compsInUse) {
        super(parentShell, "Delete Components", compsAvailable, true, false);
        this.compsInUse = compsInUse;
    }


    /**
     * Sets the items in the selection table. Override superclass method to add
     * warning icons where appropriate.
     * 
     * @param names The component names
     */
    @Override
    protected void setItems(String[] names) {
        super.setItems(names);
        for (TableItem item : items.getItems()) {
            if (compsInUse.contains(item.getText())) {
                Image image = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/warning_icon.png");
                item.setImage(image);
            }
        }

    }

}

 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.devicescreens;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Provides a pair of checkbox icon images.
 */
public final class CheckboxIcons {

    /*
     * This is a utility class and is never instantiated.
     */
    private CheckboxIcons() {
    }

    /**
     * Gets a checkbox icon image.
     * 
     * @param checked
     *            whether the checkbox should be checked
     * @return a checkbox with or without a tick in it
     */
    public static Image getCheckboxImage(boolean checked) {
        String folder = "uk.ac.stfc.isis.ibex.ui.devicescreens";
        String path = "icons/";

        if (checked) {
            path += "checkbox.png";
        } else {
            path += "navigate_cross.png";
        }

        return ResourceManager.getPluginImage(folder, path);
    }

}

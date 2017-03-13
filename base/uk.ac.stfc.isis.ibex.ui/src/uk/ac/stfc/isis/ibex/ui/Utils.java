
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
package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Set of utility methods and constants for working with SWT.
 */
public final class Utils {

    /** The mu unicode character. */
    public static final String MU = "\u03BC";

    private Utils() {

    }

    /**
     * Sets the enabled flag on the input control and recursively on all its
     * children.
     * 
     * @param control the top-level control
     * @param enabled whether the control and all its children should be enabled
     */
    public static void recursiveSetEnabled(Control control, boolean enabled) {
        if (control instanceof Composite) {
            Composite composite = (Composite) control;
            for (Control child : composite.getChildren()) {
                recursiveSetEnabled(child, enabled);
            }
        }

        control.setEnabled(enabled);
    }

    /**
     * Gets the active page in the GUI. Checks that the workbench is running.
     *
     * @return the active page; or null if there is not one
     */
    public static IWorkbenchPage getActivePage() {
        if (!PlatformUI.isWorkbenchRunning()) {
            // workbench is not running yet so no perspectives open
            return null;
        }

        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return null;
        }

        return activeWorkbenchWindow.getActivePage();
    }

}

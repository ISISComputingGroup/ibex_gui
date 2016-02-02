
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
package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A way to keep track of which Perspective is active.
 */
public class PerspectiveModel {

    private final IWorkbench workbench = PlatformUI.getWorkbench();
    private final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

    public String previousPerspective;

    /**
     * @return the previousPerspective
     */
    public String getPreviousPerspective() {
        return previousPerspective;
    }

    /**
     * @param previousPerspective
     *            the previousPerspective to set
     */
    public void setPreviousPerspective(String previousPerspective) {
        this.previousPerspective = previousPerspective;
    }

    /**
     * Get the current perspective ID.
     * 
     * @return the id of the current perspective
     */
    public String getCurrentPerspective() {
        String id = "";
        try {
            id = workbenchWindow.getActivePage().getPerspective().getId();
        } catch (Exception e) {
            // Do nothing
        }
        return id;
    }
}

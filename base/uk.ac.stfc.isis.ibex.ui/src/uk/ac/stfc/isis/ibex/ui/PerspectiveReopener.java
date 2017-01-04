
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

import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Closes and the reopens a perspective if that perspective was originally open.
 */
public class PerspectiveReopener {

    private static final Logger LOG = IsisLog.getLogger(PerspectiveReopener.class);
    private String perspectiveID;
    private boolean perspectiveWasOpen = false;

    /**
     * Instantiates a new perspective re-opener.
     *
     * @param perspectiveID the perspective id to close and reopen
     */
    public PerspectiveReopener(String perspectiveID) {
        this.perspectiveID = perspectiveID;
    }

    /**
     * Close the perspective.
     * 
     */
    public void closePerspective() {

        IWorkbenchPage activePage = Utils.getActivePage();

        if (activePage == null) {
            // no active page so nothing to close
            perspectiveWasOpen = false;
            return;
        }

        IPerspectiveDescriptor descriptor =
                PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspectiveID);
        IPerspectiveDescriptor alarmPerspective =
                PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspectiveID);
        perspectiveWasOpen = (activePage.getPerspective() == alarmPerspective);
        activePage.closePerspective(descriptor, true, true);
    }

    /**
     * If the last time close perspective was called it closed the perspective
     * then reopen it now.
     */
    public void reopenPerspective() {
        if (perspectiveWasOpen) {
            try {
                PlatformUI.getWorkbench().showPerspective(perspectiveID,
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow());
            } catch (WorkbenchException ex) {
                LOG.error("Error reopening alarm perspective.");
                LOG.catching(ex);
            }

        }

    }
}

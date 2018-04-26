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
package uk.ac.stfc.isis.ibex.ui.perspectives.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveSwitcher;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.ui.perspectives.Activator;
import uk.ac.stfc.isis.ibex.ui.perspectives.IsisPerspective;

/**
 * Class for switching perspectives based on a key press.
 */
public class SwitchPerspectiveHandler {

    /**
     * The method that is executed when a key is pressed. Switches to an isis
     * perspective.
     * 
     * @param event
     *            The execution event, should contain info on which perspective
     *            to switch to.
     * @return true if success, false otherwise
     * @throws ExecutionException
     */
    @Execute
    public boolean execute(ExecutionEvent event, MApplication app, EPartService partService, EModelService modelService) throws ExecutionException {
        String id = event.getParameter("uk.ac.stfc.isis.ibex.ui.perspectives.commands.perspectiveID");

        List<IsisPerspective> visiblePerspectives = Activator.getDefault().perspectives().getVisible();

        for (IsisPerspective perspective : visiblePerspectives) {
            if (perspective.id().equals(id)) {
                new PerspectiveSwitcher(new PerspectivesProvider(app, partService, modelService));
                return true;
            }
        }
        return false;
    }

}

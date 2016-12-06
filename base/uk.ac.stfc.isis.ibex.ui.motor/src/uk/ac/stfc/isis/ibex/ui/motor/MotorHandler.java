
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
package uk.ac.stfc.isis.ibex.ui.motor;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.motor.views.TableOfMotorsView;

/**
 * 
 */
public class MotorHandler extends AbstractHandler {

    /**
     * @param event
     * @return
     * @throws ExecutionException
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        Map test = event.getParameters();
        String output = event.getParameter("uk.ac.stfc.isis.ibex.ui.motor.colourOptionID");

        System.out.println(output);

        DisplayPreferences.setMotorBackgroundPalette(ColourOption.DEUTERANOPIA);

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference[] viewReferences = page.getViewReferences();

        for (IViewReference viewReference : viewReferences) {
            IViewPart viewPart = viewReference.getView(false);
            if (viewPart instanceof TableOfMotorsView) {
                TableOfMotorsView motorsView = (TableOfMotorsView) viewPart;
                motorsView.updatePalette();
            }
        }

        return null;
    }

}

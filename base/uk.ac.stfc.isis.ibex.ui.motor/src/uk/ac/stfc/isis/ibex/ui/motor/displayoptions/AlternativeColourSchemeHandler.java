
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

package uk.ac.stfc.isis.ibex.ui.motor.displayoptions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.motor.views.TableOfMotorsView;

/**
 * Sets the current motor background palette to the alternative colourblind-friendly scheme.
 */
public class AlternativeColourSchemeHandler {

    /**
     * Method to execute when command corresponding to this handler is triggered.
     * 
     * @param menuItem The menuItem triggering the command.
     * @param shell The Shell
     * @throws ExecutionException Thrown when execution fails
     */
    @Execute
    public void execute(MHandledMenuItem menuItem, Shell shell) throws ExecutionException {

    	if (menuItem.isSelected()) {
            DisplayPreferences.setMotorBackgroundPalette(ColourOption.COLOURBLIND);

            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IViewReference[] viewReferences = page.getViewReferences();

            for (IViewReference viewReference : viewReferences) {
                IViewPart viewPart = viewReference.getView(false);
                if (viewPart instanceof TableOfMotorsView) {
                    TableOfMotorsView motorsView = (TableOfMotorsView) viewPart;
                    motorsView.updatePalette();
                }
            }

    	}
    }
}

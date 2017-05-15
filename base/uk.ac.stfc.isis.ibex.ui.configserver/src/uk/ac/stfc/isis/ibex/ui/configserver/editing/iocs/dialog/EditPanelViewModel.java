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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.MacroPanelViewModel;
import uk.ac.stfc.isis.ibex.validators.ErrorAggregator;

/**
 * The view model for the edit panel.
 */
public class EditPanelViewModel extends ErrorAggregator {
    private MacroPanelViewModel macrosViewModel;

    /**
     * Creates the edit panel view model.
     * 
     * @param ioc
     *            The ioc the panel is based on.
     */
    public EditPanelViewModel(EditableIoc ioc) {
        macrosViewModel = new MacroPanelViewModel();
        macrosViewModel.addPropertyChangeListener("error", errorListener);
        setIOC(ioc);
    }

    /**
     * Sets the IOC used by the panel.
     * 
     * @param editableIoc
     *            The IOC.
     */
    public void setIOC(final EditableIoc editableIoc) {
        macrosViewModel.setIOC(editableIoc);
    }

    /**
     * @return The view model for the macros panel.
     */
    public MacroPanelViewModel getMacrosViewModel() {
        return macrosViewModel;
    }

    @Override
    public String constructMessage() {
        return getErrorMessages().get(0);
    }

}

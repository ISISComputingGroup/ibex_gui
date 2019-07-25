
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.RGB;

import uk.ac.stfc.isis.ibex.banner.ObservableCustomControl;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerButton;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;

/**
 * The model for custom buttons in the banner.
 */
public class CustomControlModel extends ModelObject implements ControlModel {

    private final Observables observables;
    private final ObservableCustomControl control;
    private final String text;
    private final RGB textColour;
    private final RGB buttonColour;
    private final int width;
    private final int height;
    private final BannerButton button;
    
    /**
     * TThe model for custom buttons in the banner.
     * 
     * @param observables
     *                     The observables class associated with the custom control.
     * @param button
     *                     A BannerButton class containing information about the custom button
     */
    public CustomControlModel(Observables observables, BannerButton button) {
        this.observables = observables;
        this.button = button;
        control = new ObservableCustomControl(button.writable(), button.valueToWrite());
        text = button.name();
        textColour = button.textColour();
        buttonColour = button.buttonColour();
        width = button.width();
        height = button.height();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void click() {
        control.act();
    }

    /**
     * {@inheritDoc}
     */
    public String text() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValue<Boolean> enabled() {
        return control.canWrite();
    }

}

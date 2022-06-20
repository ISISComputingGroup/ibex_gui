
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.pv.PvState;
import uk.ac.stfc.isis.ibex.ui.PvStateColour;


/**
 * This is a converter for converting the colour of a border around a label
 * based on the state of the associated PV.
 * 
 * Used for data-binding.
 * 
 */
public class PvStatusBorderColourConverter extends Converter<PvState, Color> {

    /**
     * Instantiates the converter.
     */
    public PvStatusBorderColourConverter() {
        super(PvState.class, Color.class);
    }

    /**
     * Specifies how the converter should react to possible PV states.
     * 
     * @param state the PV state to convert
     * @return the converted border colour
     */
    @Override
    public Color convert(PvState state) {
        switch (state) {
            case DISCONNECTED:
                return PvStateColour.MAGENTA;
            case INVALID:
            	return PvStateColour.MAGENTA;
            case MAJOR_ALARM:
                return PvStateColour.RED;
            case MINOR_ALARM:
                return PvStateColour.ORANGE;
            default:
                return PvStateColour.GREY;
        }
    }

}

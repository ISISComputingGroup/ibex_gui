
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.configserver.displaying.BlockState;
import uk.ac.stfc.isis.ibex.ui.PvStateColour;

/**
 * This is a converter for converting the colour of a border around a label
 * based on the state of the associated block.
 * 
 * Used for data-binding.
 * 
 */
public class BlockStatusBorderColourConverter extends Converter {

    /**
     * Instantiates the converter.
     */
    public BlockStatusBorderColourConverter() {
        super(BlockState.class, Color.class);
    }

    /**
     * Specifies how the converter should react to possible block states.
     * 
     * @param fromObject the block state to convert
     * @return the converted border colour
     */
    @Override
    public Object convert(Object fromObject) {
        BlockState state = (BlockState) fromObject;
        switch (state) {
            case DISCONNECTED:
                return PvStateColour.MAGENTA;
            case MAJOR_ALARM:
                return PvStateColour.RED;
            case MINOR_ALARM:
                return PvStateColour.ORANGE;
            default:
                return PvStateColour.WHITE;
        }
    }

}

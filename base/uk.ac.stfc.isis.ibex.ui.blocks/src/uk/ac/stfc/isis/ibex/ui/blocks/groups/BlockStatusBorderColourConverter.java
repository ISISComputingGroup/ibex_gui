
/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.BlockState;

/**
 * This is a converter for converting the colour of a border around a label
 * based on the state of the associated block.
 * 
 * Used for data-binding.
 * 
 */
public class BlockStatusBorderColourConverter extends Converter {
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final Color MAGENTA = SWTResourceManager.getColor(SWT.COLOR_MAGENTA);
    private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
    private static final Color ORANGE = SWTResourceManager.getColor(255, 128, 0);

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
                return MAGENTA;
            case MAJOR_ALARM:
                return RED;
            case MINOR_ALARM:
                return ORANGE;
            default:
                return WHITE;
        }
    }

}

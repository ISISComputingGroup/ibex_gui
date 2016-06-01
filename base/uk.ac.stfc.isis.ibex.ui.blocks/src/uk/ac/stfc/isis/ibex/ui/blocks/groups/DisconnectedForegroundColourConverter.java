
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
 * This is a converter for converting the text colour value of a block in the
 * GUI into the appropriate colour to reflect its disconnected state
 * 
 * Used for data-binding.
 * 
 */
public class DisconnectedForegroundColourConverter extends Converter {
    private static final Color GRAY = SWTResourceManager.getColor(SWT.COLOR_GRAY);
    private static final Color BLACK = SWTResourceManager.getColor(SWT.COLOR_BLACK);

    public DisconnectedForegroundColourConverter() {
        super(BlockState.class, Color.class);
    }

    @Override
    public Object convert(Object fromObject) {
        BlockState state = (BlockState) fromObject;

        if (state == BlockState.DISCONNECTED) {
            return GRAY;
        }

        return BLACK;
    }

}


/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.BlockState;

/**
 * This is a converter for converting the value for the current run-control
 * state into the appropriate colour for the foreground of the run-control
 * status indicator.
 * 
 * Used for data-binding.
 * 
 */
public class RunControlForegroundColourConverter extends Converter {
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final Color BLACK = SWTResourceManager.getColor(SWT.COLOR_BLACK);
    private static final List<BlockState> ENABLED_INRANGE_STATES = Arrays.asList(BlockState.RUNCONTROL_ENABLED_IN_RANGE,
            BlockState.RUNCONTROL_ENABLED_IN_RANGE_HIALARM, BlockState.RUNCONTROL_ENABLED_IN_RANGE_LOALARM);

    public RunControlForegroundColourConverter() {
        super(BlockState.class, Color.class);
    }

    @Override
    public Object convert(Object fromObject) {
        BlockState state = (BlockState) fromObject;

        if (ENABLED_INRANGE_STATES.contains(state)) {
            return BLACK;
        }

        return WHITE;
    }

}

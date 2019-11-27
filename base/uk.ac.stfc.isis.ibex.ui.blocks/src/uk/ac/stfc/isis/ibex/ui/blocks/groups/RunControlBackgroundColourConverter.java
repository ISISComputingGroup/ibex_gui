
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

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.RuncontrolState;

/**
 * This is a converter for converting the value for the current run-control
 * state into the appropriate colour for the background of the run-control
 * status indicator.
 * 
 * Used for data-binding.
 * 
 */
public class RunControlBackgroundColourConverter extends Converter<RuncontrolState, Color> {
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final Color DARK_RED = SWTResourceManager.getColor(192, 0, 0);
    private static final Color GREEN = SWTResourceManager.getColor(51, 255, 153);
    private static final Color ORANGE = SWTResourceManager.getColor(255, 154, 46);

    /**
     * Instantiates a new RunControlBackgroundColourConverter.
     */
    public RunControlBackgroundColourConverter() {
        super(RuncontrolState.class, Color.class);
    }

    @Override
    public Color convert(RuncontrolState state) {
        switch (state) {
        	case ENABLED_IN_RANGE:
        		return GREEN;
        	case ENABLED_OUT_RANGE:
        		return DARK_RED;
        	case DISCONNECTED:
        		return ORANGE;
        	default:
        		return WHITE;
        }
    }
}


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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.motor.views.MotorBackgroundPalette;

/**
 * 
 */
public class DisplayPreferences {

    private static final Color NORMAL_VISION_MOVING_COLOR = SWTResourceManager.getColor(160, 250, 170);
    private static final Color NORMAL_VISION_STOPPED_COLOR = SWTResourceManager.getColor(255, 200, 200);
    private static final Color NORMAL_VISION_DISABLED_COLOR = SWTResourceManager.getColor(200, 200, 200);
    private static final Color NORMAL_VISION_UNNAMED_COLOR = SWTResourceManager.getColor(220, 220, 220);

    private static final Color DEUTERANOPIA_MOVING_COLOR = SWTResourceManager.getColor(0, 250, 0);
    private static final Color DEUTERANOPIA_STOPPED_COLOR = SWTResourceManager.getColor(250, 0, 0);
    private static final Color DEUTERANOPIA_DISABLED_COLOR = SWTResourceManager.getColor(0, 0, 250);
    private static final Color DEUTERANOPIA_UNNAMED_COLOR = SWTResourceManager.getColor(220, 220, 220);


    private static final Map<ColourOption, MotorBackgroundPalette> paletteOptions;
    static {
        paletteOptions = new HashMap<>();
        paletteOptions.put(ColourOption.NORMAL_VISION,
                new MotorBackgroundPalette(NORMAL_VISION_MOVING_COLOR, NORMAL_VISION_STOPPED_COLOR,
                NORMAL_VISION_DISABLED_COLOR, NORMAL_VISION_UNNAMED_COLOR));
        paletteOptions.put(ColourOption.DEUTERANOPIA, new MotorBackgroundPalette(DEUTERANOPIA_MOVING_COLOR,
                DEUTERANOPIA_STOPPED_COLOR, DEUTERANOPIA_DISABLED_COLOR, DEUTERANOPIA_UNNAMED_COLOR));
    }

    private static MotorBackgroundPalette currentMotorBackgroundPalette =
            paletteOptions.get(ColourOption.NORMAL_VISION);

    public static MotorBackgroundPalette getMotorBackgroundPalette() {
        return currentMotorBackgroundPalette;
    }

    public static void setMotorBackgroundPalette(ColourOption paletteKey) {
        currentMotorBackgroundPalette = paletteOptions.get(paletteKey);
    }
}

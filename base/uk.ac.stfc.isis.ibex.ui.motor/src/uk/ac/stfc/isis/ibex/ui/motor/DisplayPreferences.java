
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

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.motor.views.MotorBackgroundPalette;

/**
 * 
 */
public class DisplayPreferences {

    private static final Color MOVINGCOLOR = SWTResourceManager.getColor(160, 250, 170);
    private static final Color STOPPEDCOLOR = SWTResourceManager.getColor(255, 200, 200);
    private static final Color DISABLEDCOLOR = SWTResourceManager.getColor(200, 200, 200);
    private static final Color UNNAMEDCOLOR = SWTResourceManager.getColor(220, 220, 220);

    private static MotorBackgroundPalette motorBackgroundPalette =
            new MotorBackgroundPalette(MOVINGCOLOR, STOPPEDCOLOR, DISABLEDCOLOR, UNNAMEDCOLOR);

    public static MotorBackgroundPalette getMotorBackgroundPalette() {
        return motorBackgroundPalette;
    }

    public static void setMotorBackgroundPalette(MotorBackgroundPalette newPalette) {
        motorBackgroundPalette = newPalette;
    }
}

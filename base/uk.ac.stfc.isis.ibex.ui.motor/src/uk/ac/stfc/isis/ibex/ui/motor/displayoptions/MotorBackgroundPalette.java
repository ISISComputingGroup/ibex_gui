
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

package uk.ac.stfc.isis.ibex.ui.motor.displayoptions;

import org.eclipse.swt.graphics.Color;

/**
 * Class to hold the motor background palette.
 */
public class MotorBackgroundPalette {

    private Color unnamedColor;
    private Color disabledColor;
    private Color stoppedColor;
    private Color movingColor;

    /**
     * Instantiates the motor background palette.
     * 
     * @param movingColor colour to be used when the motor is moving.
     * @param stoppedColor colour to be used when the motor is stopped.
     * @param disabledColor colour to be used when the motor is disabled.
     * @param unnamedColor colour to be used when the motor is unnamed.
     */
    public MotorBackgroundPalette(Color movingColor, Color stoppedColor, Color disabledColor, Color unnamedColor) {
        this.movingColor = movingColor;
        this.stoppedColor = stoppedColor;
        this.disabledColor = disabledColor;
        this.unnamedColor = unnamedColor;
    }

    /**
     * Returns the colour used when the motor is moving.
     * 
     * @return the colour used when the motor is moving.
     */
    public Color getMovingColor() {
        return movingColor;
    }

    /**
     * Returns the colour used when the motor is disabled.
     * 
     * @return the colour used when the motor is disabled.
     */
    public Color getDisabledColor() {
        return disabledColor;
    }

    /**
     * Returns the colour used when the motor is stopped.
     * 
     * @return the colour used when the motor is stopped.
     */
    public Color getStoppedColor() {
        return stoppedColor;
    }

    /**
     * Returns the colour used when the motor is unnamed.
     * 
     * @return the colour used when the motor is unnamed.
     */
    public Color getUnnamedColor() {
        return unnamedColor;
    }

}
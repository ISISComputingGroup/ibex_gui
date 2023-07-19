
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
public class MotorPalette {

    private final Color unnamedColor;
    private final Color disabledColor;
    private final Color stoppedColor;
    private final Color movingColor;
    private final Color intermediateColor;
    
    private final Color inPositionBorderColor;
    private final Color outsideToleranceBorderColor;

    /**
     * Instantiates the motor background palette.
     * 
     * @param movingColor colour to be used when the motor is moving.
     * @param stoppedColor colour to be used when the motor is stopped.
     * @param disabledColor colour to be used when the motor is disabled.
     * @param unnamedColor colour to be used when the motor is unnamed.
     * @param intermediateColor colour to be used when the motor is not done moving and it is not moving.
     * @param inPositonBorderColor border colour to be used when the motor is in position (within tolerance)
     * @param outsideToleranceBorderColor border colour to be used when the motor is outside tolerance of SP
     */
    public MotorPalette(Color movingColor, Color stoppedColor, 
    		Color disabledColor, Color unnamedColor, Color intermediateColor,
    		Color inPositonBorderColor, Color outsideToleranceBorderColor) {
        this.movingColor = movingColor;
        this.stoppedColor = stoppedColor;
        this.disabledColor = disabledColor;
        this.unnamedColor = unnamedColor;
        this.intermediateColor = intermediateColor;
        
        this.inPositionBorderColor = inPositonBorderColor;
        this.outsideToleranceBorderColor = outsideToleranceBorderColor;
    }

    /**
     * Returns the colour used when the motor is moving.
     * 
     * @return the colour used when the motor is moving.
     */
    public Color getMovingColor() {
        return movingColor;
    }
    
    public Color getIntermediateColor() {
    	return intermediateColor;
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
    
    /**
     * Returns the border colour used when the motor in in position.
     * @return the border colour used when the motor in in position.
     */
    public Color getInPositionBorderColor() {
    	return inPositionBorderColor;
    }
    
    /**
     * Returns the border colour used when the motor in out of position.
     * @return the border colour used when the motor in out of position.
     */
    public Color getOutsideToleranceBorderColor() {
    	return outsideToleranceBorderColor;
    }

}

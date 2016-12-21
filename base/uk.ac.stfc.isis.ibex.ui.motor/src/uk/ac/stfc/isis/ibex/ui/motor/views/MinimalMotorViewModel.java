
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */

package uk.ac.stfc.isis.ibex.ui.motor.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.MotorBackgroundPalette;

/**
 * The view model for an individual motor.
 */
public class MinimalMotorViewModel extends ModelObject {

    private static final Font ENABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.BOLD);
    private static final Font DISABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.ITALIC);
    private Motor motor;
    private String value;
    private String setpoint;
    private String motorName;
    private MotorBackgroundPalette palette;

    private Font font;
    private Color color;
	
    private Font chooseFont() {
        if (this.motor == null) {
            return DISABLEDFONT;
        } else if (this.motor.getEnabled() == MotorEnable.ENABLE) {
            return ENABLEDFONT;
        } else {
            return DISABLEDFONT;
        }
    }

    private Color chooseMotorColor() {
        Boolean movingValue = motor.getMoving();
        boolean isMoving = movingValue != null && movingValue;
        boolean isEnabled = (motor.getEnabled() == MotorEnable.ENABLE);
        boolean isNamed = (motor.getDescription() != "");

        Color backgroundColour;

        if (!isEnabled) {
            backgroundColour = palette.getDisabledColor();
        } else if (!isNamed) {
            backgroundColour = palette.getUnnamedColor();
        } else if (!isMoving) {
            backgroundColour = palette.getStoppedColor();
        } else {
            backgroundColour = palette.getMovingColor();
        }

        return backgroundColour;
    }

    private String formatForMotorDisplay(String prefix, Double value) {
        if (value != null) {
            return String.format("%s: %.2f", prefix, value);
        } else {
            return "";
        }
    }

    private void setColor(Color newColor) {
        firePropertyChange("color", this.color, this.color = newColor);
    }

    private void setFont(Font font) {
        firePropertyChange("font", this.font, this.font = font);
    }

    private void setMotorName(String newMotorName) {
        firePropertyChange("motorName", this.motorName, this.motorName = newMotorName);
    }

    private void setSetpoint(String newSetpoint) {
        firePropertyChange("setpoint", this.setpoint, this.setpoint = newSetpoint);
    }

    private void setValue(String newValue) {
        firePropertyChange("value", this.value, this.value = newValue);
    }

    /**
     * Gets the colour of the motor.
     *
     * @return the color of the motor
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the font used by the motor.
     *
     * @return the font used by the motor
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Gets the name of the motor.
     *
     * @return the name of the motor
     */
    public String getMotorName() {
        return motorName;
    }

    /**
     * Gets the setpoint of the motor.
     *
     * @return the setpoint of the motor
     */
    public String getSetpoint() {
        return setpoint;
    }

    /**
     * Gets the current position of the motor.
     *
     * @return the current position of the motor
     */
    public String getValue() {
        return value;
    }
	
    /**
     * Sets the motor that the grid cell refers to.
     *
     * @param motor
     *            the motor that this view model should control
     */
    public void setMotor(final Motor motor) {
        this.motor = motor;
        setColor(chooseMotorColor());
        this.setpoint = formatForMotorDisplay("SP", motor.getSetpoint().getSetpoint());
        this.value = formatForMotorDisplay("SP", motor.getSetpoint().getValue());
        this.motorName = motor.name();
        this.font = chooseFont();

        motor.addPropertyChangeListener("moving", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setColor(chooseMotorColor());
                setFont(chooseFont());
            }
        });

        motor.getSetpoint().addPropertyChangeListener("setpoint", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    setSetpoint(formatForMotorDisplay("SP", (Double) evt.getNewValue()));
                } catch (java.lang.ClassCastException e) {
                    throw e;
                }
            }
        });

        motor.getSetpoint().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // TODO: Check cast to double.
                setValue(formatForMotorDisplay("Val", (Double) evt.getNewValue()));
            }
        });

        motor.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                setColor(chooseMotorColor());
                setFont(chooseFont());
            }
        });
    }

    /**
     * Sets the colour palette used by this motor.
     *
     * @param palette
     *            the new palette that this motor should use
     */
    public void setPalette(MotorBackgroundPalette newPalette) {
        this.palette = newPalette;
        if (this.motor != null) {
            setColor(chooseMotorColor());
        }
    }
}


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
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.DisplayPreferences;
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.MotorBackgroundPalette;

/**
 * The view model for an individual motor.
 */
public class MinimalMotorViewModel extends ModelObject {
	
    private static final Font ENABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.BOLD);
    private static final Font DISABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.ITALIC);
    private static final Color NOPALETTECOLOR = SWTResourceManager.getColor(200, 200, 200);
    private Motor motor;
    private String value;
    private String setpoint;
    private String motorName;
    private Boolean enabled;
    private Boolean moving;
    private MotorBackgroundPalette palette;
    private Font font;
    private Color color;
	
    public MinimalMotorViewModel() {
    	DisplayPreferences displayPrefsModel = DisplayPreferences.getInstance();
    	displayPrefsModel.addPropertyChangeListener("motorBackgroundPalette", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setPalette((MotorBackgroundPalette)evt.getNewValue());
			}
		});
        setPalette(displayPrefsModel.getMotorBackgroundPalette());
    }
    
    private Font chooseFont() {
        if (enabled == null) {
            return DISABLEDFONT;
        } else if (enabled) {
            return ENABLEDFONT;
        } else {
            return DISABLEDFONT;
        }
    }

    private Color chooseColor() {

        if (palette == null) {
            // If no palette has been set yet, return a default colour
            return NOPALETTECOLOR;
        }

        boolean isMoving = (moving != null) && (moving);
        boolean isEnabled = (enabled != null) && (enabled);
        boolean isNamed = (motorName != null) && !(motorName.isEmpty());

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

    /**
     * Sets the name of the motor in the display.
     *
     * @param newName
     *            the new name of the motor
     */
    public void setMotorName(String newName) {
        firePropertyChange("motorName", this.motorName, this.motorName = newName);
    }

    /**
     * Sets the color used by the motor.
     *
     * @param newColor
     *            the new color of the motor
     */
    public void setColor(Color newColor) {
        firePropertyChange("color", this.color, this.color = newColor);
    }

    /**
     * Sets the font used by the motor.
     *
     * @param newFont
     *            the new font used by the motor
     */
    public void setFont(Font newFont) {
        firePropertyChange("font", this.font, this.font = newFont);
    }

    /**
     * Sets the current setpoint of the motor in the display.
     *
     * @param newSetpoint
     *            the new setpoint of the motor
     */
    public void setSetpoint(String newSetpoint) {
        firePropertyChange("setpoint", this.setpoint, this.setpoint = newSetpoint);
    }

    /**
     * Sets the current position of the motor in the display.
     *
     * @param newValue
     *            the new position of the motor
     */
    public void setValue(String newValue) {
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
     * Sets whether the motor is enabled.
     *
     * @param enabled
     *            whether the motor is enabled or not
     */
    public void setEnabled(MotorEnable enabled) {
        if (enabled == MotorEnable.ENABLE) {
            this.enabled = true;
        } else {
            this.enabled = false;
        }
    }
	
    /**
     * Sets the motor that the grid cell refers to.
     *
     * @param motor
     *            the motor that this view model should control
     */
    public void setMotor(final Motor motor) {

        this.motor = motor;
        setMotorName(motor.name());
        this.setpoint = formatForMotorDisplay("SP", motor.getSetpoint().getSetpoint());
        this.value = formatForMotorDisplay("Val", motor.getSetpoint().getValue());
        this.moving = motor.getMoving();
        setEnabled(motor.getEnabled());
        setColor(chooseColor());
        this.font = chooseFont();

        motor.addPropertyChangeListener("moving", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                moving = (Boolean) evt.getNewValue();
                setColor(chooseColor());
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
                try {
                    setValue(formatForMotorDisplay("Val", (Double) evt.getNewValue()));
                } catch (java.lang.ClassCastException e) {
                    throw e;
                }
            }
        });

        motor.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                setEnabled((MotorEnable) arg0.getNewValue());
                setColor(chooseColor());
                setFont(chooseFont());
            }
        });
    }

    /**
     * Sets the colour palette used by this motor.
     *
     * @param newPalette
     *            the new palette that this motor should use
     */
    public void setPalette(MotorBackgroundPalette newPalette) {
        this.palette = newPalette;
        setColor(chooseColor());
    }

    /**
     * Gets whether the motor is moving.
     *
     * @return whether the motor is moving or not
     */
    public boolean getMoving() {
        return moving;
    }

    /**
     * Sets whether or not the motor is moving.
     *
     * @param moving
     *            whether the motor is moving or not
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * @return the motor used by this view model
     */
    public Motor getMotor() {
        return this.motor;
    }

}

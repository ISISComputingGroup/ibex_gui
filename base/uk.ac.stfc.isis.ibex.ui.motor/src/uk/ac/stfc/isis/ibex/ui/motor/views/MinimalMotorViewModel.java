
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

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;
import uk.ac.stfc.isis.ibex.motor.Motors;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTableSettings;
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.DisplayPreferences;
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.MotorPalette;

/**
 * The view model for an individual motor.
 */
public class MinimalMotorViewModel extends ModelObject {
    private static final Integer MAX_NAME_LENGTH = 16;
    private static final Font ENABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.BOLD);
    private static final Font DISABLEDFONT = SWTResourceManager.getFont("Arial", 9, SWT.ITALIC);
    private static final Color NOPALETTECOLOR = SWTResourceManager.getColor(200, 200, 200);
    private Motor motor;

    private String value;
    private String setpoint;
    private String lowLimit;
    private String highLimit;
    private String offset;
    private String error;
    private String motorName;
    private String tooltip;
    private Boolean enabled;
    private Boolean moving;
    private Boolean doneMoving;
    private Boolean usingEncoder;
    private Boolean energised;
    private boolean advancedMinimalMotorView;
    private MotorPalette palette;
    private Font font;
    private Color color;
    private Color borderColor;

    /**
     * Constructor.
     */
    public MinimalMotorViewModel() {
    	this(DisplayPreferences.getInstance(), Motors.getInstance().getMotorSettingsModel());
    }

    /**
     * Constructor.
     * @param displayPrefsModel Model for display preferences, e.g. colours 
     * @param motorsTableSettingsModel Model for table settings, e.g. advance or simple
     */
    public MinimalMotorViewModel(DisplayPreferences displayPrefsModel, MotorsTableSettings motorsTableSettingsModel) {
		displayPrefsModel.addPropertyChangeListener("motorBackgroundPalette", 
				evt -> setPalette((MotorPalette) evt.getNewValue()));
		
        setPalette(displayPrefsModel.getMotorBackgroundPalette());
        
        /**
         *  This is needed because this class gets instantiated when the user opens the tab associated with it.
         *  If the user doesn't open the non default (first) tab the change event listeners wont be
         *  registered yet when the advanced setting is changed.
         *  And the default value of this field will be false leading to inconsistencies within the UI.
         */
        this.advancedMinimalMotorView = motorsTableSettingsModel.isAdvancedMinimalMotorView();
        
        /**
         *  Property change listener for the motor settings model to this minimal motor view model 
         *  to determine if the advanced minimal view is enabled for the table of motors.
         */
        motorsTableSettingsModel.addPropertyChangeListener("advancedMinimalMotorView",
        		evt -> setAdvancedMinimalMotorView((boolean) evt.getNewValue()));
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

    private Color chooseBackgroundColor() {

        if (palette == null) {
            // If no palette has been set yet, return a default colour
            return NOPALETTECOLOR;
        }

        boolean isMoving = (moving != null) && (moving);
        boolean isDoneMoving = (doneMoving != null) && (doneMoving);
        boolean isEnabled = (enabled != null) && (enabled);
        boolean isNamed = !Strings.isNullOrEmpty(motorName);

        Color backgroundColour;

        if (!isEnabled) {
            backgroundColour = palette.getDisabledColor();
        } else if (!isNamed) {
            backgroundColour = palette.getUnnamedColor();
        } else if (!isMoving && !isDoneMoving) {
            backgroundColour = palette.getIntermediateColor();
        } else if (!isMoving) {
            backgroundColour = palette.getStoppedColor();
        } else {
            backgroundColour = palette.getMovingColor();
        }

        return backgroundColour;
    }

    private String formatForMotorDisplay(String prefix, Double value) {
        if (value == null) {
        	return "";
        }
        
    	if (advancedMinimalMotorView) {
            return String.format("%s %.3f", prefix, value);
    	} else {
    		return String.format("%s: %.3f", prefix, value);
    	}
    }

    /**
     * Sets the name of the motor in the display based on a motor object.
     *
     * @param motor
     *            the new motor
     */
    public void setMotorName(Motor motor) {
        String name = Strings.isNullOrEmpty(motor.getDescription()) ? motor.name() : motor.getDescription();
        if (name != null && name.length() > MAX_NAME_LENGTH) {
            name = name.substring(0, MAX_NAME_LENGTH);
        }
        firePropertyChange("motorName", this.motorName, this.motorName = name);
    }

    /**
     * Sets the tooltip of the motor view.
     *
     * @param tooltip
     *            the new tooltip for the view.
     */
    public void setTooltip(String tooltip) {
        firePropertyChange("tooltip", this.tooltip, this.tooltip = tooltip);
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
     * @return The colour for the border, i.e. for within tolerance.
     */
    public Color getBorderColor() {
    	return borderColor;
    }
    
    /**
     * Set the colour for the border, i.e. for within tolerance. 
     * @param borderColor border colour to use
     */
    public void setBorderColor(final Color borderColor) {
    	firePropertyChange("borderColor", this.borderColor, this.borderColor = borderColor);
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
     * Gets the tooltip for the view.
     *
     * @return the tooltip for the view.
     */
    public String getTooltip() {
        return tooltip;
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
    
    private void refreshLabels() {
    	setSetpoint(formatForMotorDisplay("SP", motor.getSetpoint()));
        setValue(formatForMotorDisplay("Val", motor.getValue()));
        
        setLowLimit(formatForMotorDisplay("Lo", motor.getLowerLimit()));
        setHighLimit(formatForMotorDisplay("Hi", motor.getUpperLimit()));

        setOffset(formatForMotorDisplay("Off", motor.getOffset()));
        setError(formatForMotorDisplay("Err", motor.getError()));
    }
	
    /**
     * Sets the motor that the grid cell refers to.
     *
     * @param motor
     *            the motor that this view model should control
     */
    public void setMotor(final Motor motor) {

        this.motor = motor;
        setMotorName(motor);
        setTooltip(motor.getDescription());
        
        refreshLabels();
        
        this.moving = motor.getMoving();
        this.doneMoving = motor.getDoneMoving();
        setEnabled(motor.getEnabled());
        setColor(chooseBackgroundColor());
                
        setBorderColor(chooseBorderColor());
        this.font = chooseFont();
        
        setUsingEncoder(motor.getUsingEncoder());
        setEnergised(motor.getEnergised());
        
        motor.addPropertyChangeListener("description", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setMotorName(motor);
                setTooltip(motor.getDescription());
            }
        });
        
        motor.addPropertyChangeListener("moving", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                moving = (Boolean) evt.getNewValue();
                setColor(chooseBackgroundColor());
                setFont(chooseFont());
            }
        });
        motor.addPropertyChangeListener("doneMoving", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doneMoving = (Boolean) evt.getNewValue();
                setColor(chooseBackgroundColor());
                setFont(chooseFont());
            }
        });

        motor.addPropertyChangeListener("setpoint", evt -> setSetpoint(formatForMotorDisplay("SP", motor.getSetpoint())));
        motor.addPropertyChangeListener("value", evt -> setValue(formatForMotorDisplay("Val", motor.getValue())));
        
        motor.addPropertyChangeListener("lowerLimit", evt -> setLowLimit(formatForMotorDisplay("Lo", motor.getLowerLimit())));
        motor.addPropertyChangeListener("upperLimit", evt -> setHighLimit(formatForMotorDisplay("Hi", motor.getUpperLimit())));
        
        motor.addPropertyChangeListener("offset", evt -> setOffset(formatForMotorDisplay("Off", motor.getOffset())));
        motor.addPropertyChangeListener("error", evt -> setError(formatForMotorDisplay("Err", motor.getError())));
        
        motor.addPropertyChangeListener("usingEncoder", evt -> setUsingEncoder(motor.getUsingEncoder()));
        motor.addPropertyChangeListener("energised", evt -> setEnergised(motor.getEnergised()));

        motor.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                setEnabled((MotorEnable) event.getNewValue());
                setColor(chooseBackgroundColor());
                setFont(chooseFont());
            }
        });
        
        motor.addPropertyChangeListener("withinTolerance", event -> setBorderColor(chooseBorderColor()));
    }
    
    private Color chooseBorderColor() {
    	if (motor == null || palette == null) {
    		return NOPALETTECOLOR;
    	}
    	
    	Color borderColor;
        final Boolean isWithinTolerance = motor.getWithinTolerance();
        if (isWithinTolerance == null || isWithinTolerance) {
        	borderColor = palette.getInPositionBorderColor();
        } else {
        	borderColor = palette.getOutsideToleranceBorderColor();
        }
        return borderColor;
    }
    
    /**
     * Sets the colour palette used by this motor.
     *
     * @param newPalette
     *            the new palette that this motor should use
     */
    public void setPalette(MotorPalette newPalette) {
        this.palette = newPalette;
        setColor(chooseBackgroundColor());
        setBorderColor(chooseBorderColor());
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
     * Gets whether the motor is done moving.
     *
     * @return whether the motor is done moving or not
     */
    public boolean getDoneMoving() {
        return doneMoving;
    }
    
    /**
     * Sets whether or not the motor is done moving.
     *
     * @param doneMoving
     *            whether the motor is moving or not
     */
    public void setDoneMoving(boolean doneMoving) {
        this.doneMoving = doneMoving;
    }
    
    /**
     * @return the motor used by this view model
     */
    public Motor getMotor() {
        return this.motor;
    }
    
    /**
     * @return whether the advanced minimal view for the table of motors is enabled
     */
    public boolean isAdvancedMinimalMotorView() {
    	return advancedMinimalMotorView;
    }
    
    /**
     * Sets if the advanced minimal motor view is enabled.
     * 
     * @param newSetting
     *              whether the advanced minimal view for the table of motors is enabled
     */
    public void setAdvancedMinimalMotorView(boolean newSetting) {
    	firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
    	refreshLabels();
    }
    
    /**
     * Get this motors' soft low limit.
     * @return the limit
     */
    public String getLowLimit() {
    	return lowLimit;
    }
    
    /**
     * Set this motors' soft low limit.
     * @param lowLimit the limit
     */
    public void setLowLimit(final String lowLimit) {
    	firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = lowLimit);
    }
    
    /**
     * Get this motors' soft high limit.
     * @return the limit
     */
    public String getHighLimit() {
    	return highLimit;
    }
    
    /**
     * Set this motors' soft high limit.
     * @param highLimit the limit
     */
    public void setHighLimit(final String highLimit) {
    	firePropertyChange("highLimit", this.highLimit, this.highLimit = highLimit);
    }
    
    /**
     * Get this motors' offset.
     * @return the offset
     */
    public String getOffset() {
    	return offset;
    }
    
    /**
     * Set this motors' offset.
     * @param offset the offset
     */
    public void setOffset(final String offset) {
    	firePropertyChange("offset", this.offset, this.offset = offset);
    }
    
    /**
     * Get this motors' positioning error.
     * @return the error
     */
    public String getError() {
    	return error;
    }
    
    /**
     * Set this motors' positioning error.
     * @param error the error
     */
    public void setError(final String error) {
    	firePropertyChange("error", this.error, this.error = error);
    }
    
    /**
     * Get whether this motor is using an encoder.
     * @return whether this motor is using an encoder
     */
    public Boolean getUsingEncoder() {
    	return usingEncoder;
    }
    
    /**
     * Set whether this motor is using an encoder.
     * @param usingEncoder whether this motor is using an encoder
     */
    public void setUsingEncoder(final Boolean usingEncoder) {
    	firePropertyChange("usingEncoder", this.usingEncoder, this.usingEncoder = usingEncoder);
    }

    /**
     * Get whether this motor is energised.
     * @return whether this motor is energised
     */
    public Boolean getEnergised() {
    	return energised;
    }
    
    /**
     * Set whether this motor is energised.
     * @param energised whether this motor is energised
     */
    public void setEnergised(final Boolean energised) {
    	firePropertyChange("energised", this.energised, this.energised = energised);
    }
}

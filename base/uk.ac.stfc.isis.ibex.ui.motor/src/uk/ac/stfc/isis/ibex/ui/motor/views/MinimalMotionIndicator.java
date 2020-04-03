
/*
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

package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.ui.ImageUtils;

/**
 * A bar of icons that give information about an axis at a glance.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MinimalMotionIndicator extends Composite {

    private static final String LOW_LIMIT_ENABLED_TOOLTIP = "Low (reverse) limit hit";
    private static final String LOW_LIMIT_DISABLED_TOOLTIP = "Low (reverse) limit not hit";
    
    private static final String LOW_MOVE_ENABLED_TOOLTIP = "Axis moving in negative (reverse) direction";
    private static final String LOW_MOVE_DISABLED_TOOLTIP = "Axis not moving in negative (reverse) direction";
    
    private static final String HIGH_LIMIT_ENABLED_TOOLTIP = "High (forward) limit hit";
    private static final String HIGH_LIMIT_DISABLED_TOOLTIP = "High (forward) limit not hit";
    
    private static final String HIGH_MOVE_ENABLED_TOOLTIP = "Axis moving in positive (forward) direction";
    private static final String HIGH_MOVE_DISABLED_TOOLTIP = "Axis not moving in positive (forward) direction";
    
    private static final String HOME_ENABLED_TOOLTIP = "Axis at home";
    private static final String HOME_DISABLED_TOOLTIP = "Axis not at home";

    private final BooleanImageLabel lowerDirectionLimit;
    private final BooleanImageLabel leftDirection;
    private final BooleanImageLabel home;
    private final BooleanImageLabel rightDirection;
    private final BooleanImageLabel upperDirectionLimit;
    
    private static final Image LOW_LIMIT_IMAGE_ENABLED = getImage("lower_limit_minimal.png");
    private static final Image LOW_LIMIT_IMAGE_DISABLED = ImageUtils.disabled(LOW_LIMIT_IMAGE_ENABLED);
    
    private static final Image LEFT_MOVE_IMAGE_ENABLED = getImage("arrow_left_minimal.png");
    private static final Image LEFT_MOVE_IMAGE_DISABLED = ImageUtils.disabled(LEFT_MOVE_IMAGE_ENABLED);
    
    private static final Image HOME_IMAGE_ENABLED = getImage("home_minimal.png");
    private static final Image HOME_IMAGE_DISABLED = ImageUtils.disabled(HOME_IMAGE_ENABLED);
    
    private static final Image RIGHT_MOVE_IMAGE_ENABLED = getImage("arrow_right_minimal.png");
    private static final Image RIGHT_MOVE_IMAGE_DISABLED = ImageUtils.disabled(RIGHT_MOVE_IMAGE_ENABLED);
    
    private static final Image UPPER_LIMIT_IMAGE_ENABLED = getImage("upper_limit_minimal.png");
    private static final Image UPPER_LIMIT_IMAGE_DISABLED = ImageUtils.disabled(UPPER_LIMIT_IMAGE_ENABLED);
    
    /**
     * Creates an indicator bar that gives information about an axis at a
     * glance.
     * 
     * @param parent
     *            The composite that this indicator is held in.
     * @param style
     *            The style of the indicator.
     */
	public MinimalMotionIndicator(Composite parent, int style) {
		super(parent, style);
        GridLayout container = new GridLayout(5, false);
		container.verticalSpacing = 0;
		container.marginWidth = 0;
		container.marginHeight = 0;
		container.horizontalSpacing = 0;
		setLayout(container);
		
        lowerDirectionLimit = new BooleanImageLabel(this, 
        		LOW_LIMIT_IMAGE_ENABLED, LOW_LIMIT_IMAGE_DISABLED, LOW_LIMIT_ENABLED_TOOLTIP, LOW_LIMIT_DISABLED_TOOLTIP);
        leftDirection = new BooleanImageLabel(this, 
        		LEFT_MOVE_IMAGE_ENABLED, LEFT_MOVE_IMAGE_DISABLED, LOW_MOVE_ENABLED_TOOLTIP, LOW_MOVE_DISABLED_TOOLTIP);
        home = new BooleanImageLabel(this, 
        		HOME_IMAGE_ENABLED, HOME_IMAGE_DISABLED, HOME_ENABLED_TOOLTIP, HOME_DISABLED_TOOLTIP);
        rightDirection = new BooleanImageLabel(this, 
        		RIGHT_MOVE_IMAGE_ENABLED, RIGHT_MOVE_IMAGE_DISABLED, HIGH_MOVE_ENABLED_TOOLTIP, HIGH_MOVE_DISABLED_TOOLTIP);
		upperDirectionLimit = new BooleanImageLabel(this, 
        		UPPER_LIMIT_IMAGE_ENABLED, UPPER_LIMIT_IMAGE_DISABLED, HIGH_LIMIT_ENABLED_TOOLTIP, HIGH_LIMIT_DISABLED_TOOLTIP);
        
		setInitialState();
	}

    /**
     * Set the motor that the indicator is pointing to.
     * 
     * @param motor
     *            The motor that the indicator is displaying information about.
     */
	public void setMotor(final Motor motor) {
		setArrows(motor);
		motor.addUiThreadPropertyChangeListener("direction", evt -> setArrows(motor));
		motor.addUiThreadPropertyChangeListener("moving", evt -> setArrows(motor));
		
		enableHome(motor.getAtHome());
		motor.addUiThreadPropertyChangeListener("atHome", evt -> enableHome(motor.getAtHome()));

		setLowerLimit(motor.getAtLowerLimitSwtich());
		motor.addUiThreadPropertyChangeListener("atLowerLimitSwitch", evt -> setLowerLimit(motor.getAtLowerLimitSwtich()));
		
		setUpperLimit(motor.getAtUpperLimitSwitch());
		motor.addUiThreadPropertyChangeListener("atUpperLimitSwitch", evt -> setUpperLimit(motor.getAtUpperLimitSwitch()));
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		lowerDirectionLimit.setBackground(color);
		leftDirection.setBackground(color);
		home.setBackground(color);
		rightDirection.setBackground(color);
		upperDirectionLimit.setBackground(color);
	}
	
	private void setInitialState() {
		setLowerLimit(false);
		setUpperLimit(false);
		enableHome(false);
		setArrows(MotorDirection.UNKNOWN, false);
	}
	
	private void setUpperLimit(final Boolean enable) {
        setLimit(upperDirectionLimit, enable);
	}
	
	private void setLowerLimit(final Boolean enable) {
        setLimit(lowerDirectionLimit, enable);
	}

    private void setLimit(final BooleanImageLabel limit, final Boolean enable) {
        limit.enable(disableIfNull(enable));
	}
	
	private void setArrows(final Motor motor) {
		setArrows(motor.getDirection(), motor.getMoving());
	}	
	
	private void setArrows(MotorDirection motorDirection, Boolean moving) {
		
		if (moving == null || !moving) {
            leftDirection.disable();
            rightDirection.disable();
			
			return;
		}
		
		switch (motorDirection) {
			case POSITIVE:
                leftDirection.disable();
                rightDirection.enable();
				return;
			case NEGATIVE:
                leftDirection.enable();
                rightDirection.disable();
				return;
            default:
			case UNKNOWN:
                leftDirection.disable();
                rightDirection.disable();
				return;
		}
	}
	
	private void enableHome(final Boolean enable) {
		home.enable(disableIfNull(enable));				
	}
	
	private static Image getImage(String image) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/" + image);
	}

	private static boolean disableIfNull(Boolean enable) {
		return enable != null && enable;
	}
}

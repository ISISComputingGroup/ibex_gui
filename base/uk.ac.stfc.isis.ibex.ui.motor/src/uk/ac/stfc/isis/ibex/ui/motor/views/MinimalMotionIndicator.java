
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;

/**
 * A bar of icons that give information about an axis at a glance.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MinimalMotionIndicator extends Composite {

	private final Display display = Display.getDefault();

    private EnableableImageLabel lowerDirectionLimit;
    private EnableableImageLabel leftDirection;
    private EnableableImageLabel home;
    private EnableableImageLabel rightDirection;
    private EnableableImageLabel upperDirectionLimit;

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
        GridLayout gridLayout1 = new GridLayout(5, false);
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 0;
		setLayout(gridLayout1);
		
        lowerDirectionLimit = new EnableableImageLabel(this, getImage("lower_limit_minimal.png"));

        leftDirection = new EnableableImageLabel(this, getImage("arrow_left_minimal.png"));
		
        home = new EnableableImageLabel(this, getImage("home_minimal.png"));
		
        rightDirection = new EnableableImageLabel(this, getImage("arrow_right_minimal.png"));
		
        upperDirectionLimit = new EnableableImageLabel(this, getImage("upper_limit_minimal.png"));
		
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
		motor.addPropertyChangeListener("direction", new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setArrows(motor);
			}
		});
		motor.addPropertyChangeListener("moving", new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setArrows(motor);
			}
		});
		
		enableHome(motor.getAtHome());
		motor.addPropertyChangeListener("atHome", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				enableHome(motor.getAtHome());
			}
		});

		setLowerLimit(motor.getAtLowerLimitSwtich());
		motor.addPropertyChangeListener("atLowerLimitSwitch", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setLowerLimit(motor.getAtLowerLimitSwtich());
			} });
		
		setUpperLimit(motor.getAtUpperLimitSwitch());
		motor.addPropertyChangeListener("atUpperLimitSwitch", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setUpperLimit(motor.getAtUpperLimitSwitch());
			} });
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

    private void setLimit(final EnableableImageLabel limit, final Boolean enable) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
                limit.enable(disableIfNull(enable));
			}
		});
	}
	
	private void setArrows(final Motor motor) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				setArrows(motor.getDirection(), motor.getMoving());
			}
		});
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
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				home.enable(disableIfNull(enable));				
			}
		});
	}
	
	private Image getImage(String image) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/" + image);
	}

	private static boolean disableIfNull(Boolean enable) {
		return enable != null && enable;
	}
}

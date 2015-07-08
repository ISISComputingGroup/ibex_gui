
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;

public class MinimalMotionIndicator extends Composite {

	private final Display display = Display.getDefault();

	private Label lowerDirectionLimit;
	private Label leftDirection;
	private Label home;
	private Label rightDirection;
	private Label upperDirectionLimit;
	
	private EnableableImage lowerLimit;
	private EnableableImage leftArrow;
	private EnableableImage homeImage;
	private EnableableImage rightArrow;
	private EnableableImage upperLimit;
	
	private Composite composite;
	
	public MinimalMotionIndicator(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout_1 = new GridLayout(1, false);
		gridLayout_1.verticalSpacing = 0;
		gridLayout_1.marginWidth = 0;
		gridLayout_1.marginHeight = 0;
		gridLayout_1.horizontalSpacing = 0;
		setLayout(gridLayout_1);
		
		composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, true, 1, 1));
		
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		lowerDirectionLimit = new Label(composite, SWT.NONE);
		lowerDirectionLimit.setAlignment(SWT.CENTER);
		lowerDirectionLimit.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/lower_limit_minimal.png"));
		
		leftDirection = new Label(composite, SWT.NONE);
		leftDirection.setAlignment(SWT.CENTER);
		leftDirection.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/arrow_left_minimal.png"));
		
		home = new Label(composite, SWT.NONE);
		home.setAlignment(SWT.CENTER);
		home.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/home_minimal.png"));
		
		rightDirection = new Label(composite, SWT.NONE);
		rightDirection.setAlignment(SWT.CENTER);
		rightDirection.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/arrow_right_minimal.png"));
		
		upperDirectionLimit = new Label(composite, SWT.NONE);
		upperDirectionLimit.setAlignment(SWT.CENTER);
		upperDirectionLimit.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/upper_limit_minimal.png"));
		
		setImages(parent);
		setInitialState();
		setMouseListeners();
	}

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
			}});
		
		setUpperLimit(motor.getAtUpperLimitSwitch());
		motor.addPropertyChangeListener("atUpperLimitSwitch", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setUpperLimit(motor.getAtUpperLimitSwitch());
			}});
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

	private void setMouseListeners() {
		final Composite self = this;
		final MouseAdapter forwardDoubleClick = new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Event event = new Event();
				event.widget = self;
		
				self.notifyListeners(SWT.MouseDoubleClick, event);
			}
		};
		
		composite.addMouseListener(forwardDoubleClick);
		lowerDirectionLimit.addMouseListener(forwardDoubleClick);
		leftDirection.addMouseListener(forwardDoubleClick);
		home.addMouseListener(forwardDoubleClick);
		rightDirection.addMouseListener(forwardDoubleClick);
		upperDirectionLimit.addMouseListener(forwardDoubleClick);
	}
	
	private void setInitialState() {
		setLowerLimit(false);
		setUpperLimit(false);
		enableHome(false);
		setArrows(MotorDirection.UNKNOWN, false);
	}
	
	private void setUpperLimit(final Boolean enable) {
		setLimit(upperDirectionLimit, upperLimit, enable);

	}
	
	private void setLowerLimit(final Boolean enable) {
		setLimit(lowerDirectionLimit, lowerLimit, enable);
	}

	private void setLimit(final Label limit, final EnableableImage image, final Boolean enable) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				limit.setImage(image.isEnabled(disableIfNull(enable)));
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
			leftDirection.setImage(leftArrow.disabled());
			rightDirection.setImage(rightArrow.disabled());
			
			return;
		}
		
		switch (motorDirection) {
			case POSITIVE:
				leftDirection.setImage(leftArrow.disabled());
				rightDirection.setImage(rightArrow.enabled());
				return;
			case NEGATIVE:
				leftDirection.setImage(leftArrow.enabled());
				rightDirection.setImage(rightArrow.disabled());
				return;
			case UNKNOWN:
				leftDirection.setImage(leftArrow.disabled());
				rightDirection.setImage(rightArrow.disabled());
				return;
		}
	}
	
	private void enableHome(final Boolean enable) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				home.setImage(homeImage.isEnabled(disableIfNull(enable)));				
			}
		});
	}
	
	private void setImages(Composite parent) {
		Display display = parent.getDisplay();
		lowerLimit = new EnableableImage(display, getImage("lower_limit_minimal.png"));
		leftArrow = new EnableableImage(display, getImage("arrow_left_minimal.png"));
		homeImage = new EnableableImage(display, getImage("home_minimal.png"));
		rightArrow = new EnableableImage(display, getImage("arrow_right_minimal.png"));
		upperLimit = new EnableableImage(display, getImage("upper_limit_minimal.png"));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		lowerLimit.dispose();
		leftArrow.dispose();
		homeImage.dispose();
		rightArrow.dispose();
		upperLimit.dispose();
	}
	
	private Image getImage(String image) {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/" + image);
	}

	private static boolean disableIfNull(Boolean enable) {
		return enable != null && enable;
	}
}

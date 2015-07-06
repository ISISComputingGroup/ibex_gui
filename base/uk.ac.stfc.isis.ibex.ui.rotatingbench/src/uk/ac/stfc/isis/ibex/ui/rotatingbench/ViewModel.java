
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

package uk.ac.stfc.isis.ibex.ui.rotatingbench;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.rotatingbench.Command;
import uk.ac.stfc.isis.ibex.rotatingbench.IRotatingBench;
import uk.ac.stfc.isis.ibex.rotatingbench.Status;

/**
 * View model for the RotatingBench
 * @author sjb99183
 *
 */
public class ViewModel extends ModelObject {

	private String angle;
	private String angleSP;
	private String angleSPRBV;
	private String status;
	private String liftStatus;
	private String movingStatus;
	private Color movingColour;
	private boolean hvCheck;
	private IRotatingBench model;
	private final Color goingColour;
	private final Color aboutToColour;
	private final Color stopColour;
	
	public ViewModel(final IRotatingBench model) {
		this.model = model;

		// Save the colours while we are on the UI thread
		Display display = Display.getCurrent();
		goingColour = display.getSystemColor(SWT.COLOR_GREEN);
		aboutToColour = display.getSystemColor(SWT.COLOR_DARK_YELLOW);
		stopColour = display.getSystemColor(SWT.COLOR_WHITE);
		
		angle = formatAngle(model.angle().getValue());
		model.angle().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange("angle", angle, angle = formatAngle(model.angle().getValue()));
			}
		});
		
		angleSP = formatAngle(model.angleSP().getValue());
		model.angleSP().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange("angleSP", angleSP, angleSP = formatAngle(model.angleSP().getValue()));
			}
		});
		
		angleSPRBV = formatAngle(model.angleSPRBV().getValue());
		model.angleSPRBV().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange("angleSPRBV", angleSPRBV, angleSPRBV = formatAngle(model.angleSPRBV().getValue()));
			}
		});
		
		status = formatStatus(model.status().getValue());
		model.status().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("status", status, status = formatStatus(model.status().getValue()));
				firePropertyChange("movingStatus", movingStatus, movingStatus = formatMovingStatus(model.status().getValue()));
				firePropertyChange("movingColour", movingColour, movingColour = formatMovingColour(model.status().getValue()));
			}
		});
		
		liftStatus = formatStatus(model.liftStatus().getValue());
		model.liftStatus().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("liftStatus", liftStatus, liftStatus = formatStatus(model.liftStatus().getValue()));
			}
		});
		
		hvCheck = formatHvCheck(model.command().getValue());
		model.command().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("hvCheck", hvCheck, hvCheck = formatHvCheck(model.command().getValue()));
			}
		});
		
	}
	
	public String getAngle() {
		return angle;
	}
	
	public String getAngleSP() {
		return angleSP;
	}
	
	public void setAngleSP(String text) {
		try {
			double value = Double.parseDouble(text);
			model.setAngleSP(value);
		} catch (NumberFormatException e) {
			
		}
	}
	
	public String getAngleSPRBV() {
		return angleSPRBV;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getLiftStatus() {
		return liftStatus;
	}

	public boolean getHvCheck() {
		return hvCheck;
	}
	
	public String getMovingStatus() {
		return movingStatus;
	}
	
	public Color getMovingColour() {
		return movingColour;
	}
	
	public void setHvCheck(boolean value) {
		model.setCommand(value);
	}
	
	private String formatAngle(Double value) {
		return value == null ? "Unknown" : String.format("%.3f", value);
	}
	
	private String formatStatus(Object value) {
		return value == null ? "UNKNOWN" : value.toString();
	}
	
	private boolean formatHvCheck(Command value) {
		return value == Command.YES;
	}
	
	// Reformat the status for the "bench moving" indicator
	private String formatMovingStatus(Status value) {
		if (value == null) {
			return "UNKNOWN";
		}
		
		switch (value) {
		case RAISING_BENCH:
		case HV_GOING_DOWN:
			return "Bench is about to move";
		case MOVING:
			return "Bench is moving";
		case DONE:
		case HV_COMING_UP:
		case LOWERING:
		default:
			return "Stationary";
		}
	}
	
	// Background colour for the "bench moving" indicator
	private Color formatMovingColour(Status value) {
		switch (value) {
		case RAISING_BENCH:
		case HV_GOING_DOWN:
			return aboutToColour;
		case MOVING:
			return goingColour;
		case DONE:
		case HV_COMING_UP:
		case LOWERING:
		default:
			return stopColour;
		}
	}
}

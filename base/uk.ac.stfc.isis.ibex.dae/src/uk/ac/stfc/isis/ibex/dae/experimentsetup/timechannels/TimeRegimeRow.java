
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

package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A row containing time regime information.
 */
public class TimeRegimeRow extends ModelObject {
	
	private double from;
	
	private double to;
	
	private double step;
	
	private TimeRegimeMode mode = TimeRegimeMode.BLANK;
	
	/**
	 * Gets the from value.
	 *
	 * @return the from value
	 */
	public double getFrom() {
		return from;
	}
	
	/**
	 * Gets the to value.
	 *
	 * @return the to value
	 */
	public double getTo() {
		return to;
	}
	
	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public double getStep() {
		return step;
	}
	
	/**
	 * Gets the time regime mode.
	 *
	 * @return the mode
	 */
	public TimeRegimeMode getMode() {
		return mode;
	}
	
	/**
	 * Sets the from value.
	 *
	 * @param value the new from value
	 */
	public void setFrom(double value) {
		firePropertyChange("from", from, from = value);
	}
	
	/**
	 * Sets the to value.
	 *
	 * @param value the new to value
	 */
	public void setTo(double value) {
		firePropertyChange("to", to, to = value);
	}
	
	/**
	 * Sets the step.
	 *
	 * @param value the new step
	 */
	public void setStep(double value) {
		firePropertyChange("step", step, step = value);
	}
	
	/**
	 * Sets the time regime mode.
	 *
	 * @param value the new mode
	 */
	public void setMode(TimeRegimeMode value) {
		firePropertyChange("mode", mode, mode = value);
	}
}

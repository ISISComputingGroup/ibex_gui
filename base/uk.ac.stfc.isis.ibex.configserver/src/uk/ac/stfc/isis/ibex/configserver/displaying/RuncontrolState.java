
/**
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

package uk.ac.stfc.isis.ibex.configserver.displaying;

/**
 * Enum for the overall state of run-control.
 */
public enum RuncontrolState {
	
    /**
     * The run control is disabled.
     */
    DISABLED("Runcontrol is not enabled for this value."),
    /**
     * The run control is enabled and in range.
     */
    ENABLED_IN_RANGE("Runcontrol is enabled and the value is within the specified range."),
    /**
     * The run control is enabled but out of range.
     */
    ENABLED_OUT_RANGE("Runcontrol is enabled and the value is outside the specified range."),
    /**
     * The run control is disconnected.
     */
    DISCONNECTED("Runcontrol status is unknown; there was a problem reading the status of runcontrol for this item.");
	
	private final String name;
	
	private RuncontrolState(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
}


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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui;

/**
 * Set of general utility methods and constants used in the UI.
 */
public final class Utils {

	/**
	 * Takes a value and if that value is in the range min_value to max_value returns that value
	 * If it is outside that range the return is constrained to be the closest of either min_value or max_value
	 * If the max value is greater than the min value then the max value is returned
	 * 
	 * @param value The value to constrain
	 * @param min_value The lower bound to constrain value to
	 * @param max_value The upper bound to constrain value to
	 * @return The constrained value
	 */
	public static int constrainIntToRange(int value, int min_value, int max_value) {
		// Return max value if less than min value
		if (max_value < min_value) {
			return max_value;
		}
		// Constrain value between min and max
		if (value > max_value) {
	        return max_value;
	    } else if (value < min_value) {
	        return min_value;
	    } else {
	        return value;
	    }
	}


}

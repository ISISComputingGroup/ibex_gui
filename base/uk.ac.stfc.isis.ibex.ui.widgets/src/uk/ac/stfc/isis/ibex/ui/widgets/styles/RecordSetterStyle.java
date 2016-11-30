
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
package uk.ac.stfc.isis.ibex.ui.widgets.styles;

/**
 * Enumeration values to be used in the constructor of a RecordSetter
 * to select options for its display and functionality.
 */
public enum RecordSetterStyle {
	
	/**
	 * Specifies that the RecordSetter will provide a 'Set' button
	 * that sends its current value to the record on click.
	 */
	BUTTON,
	
	/**
	 * Specifies that the RecordSetter will send its current value to
	 * the record when it loses focus.
	 */
	FOCUS,
	
	/**
	 * Specifies that the RecordSetter will provide a small icon that
	 * indicates the current status of the control (disconnected, 
	 * invalid data, valid data, and data sent).
	 */
	ICON,
	
	/**
     * Specifies that the RecordSetter will provide more prominent colour
     * highlighting to indicate status.
     */
	HIGHLIGHTING,
	
	/**
	 * Specifies that the RecordSetter will provide all available 
	 * functionality (set on focus loss, set button, status icon, and 
	 * status highlighting).
	 */
	FULL,
	
	/**
	 * Specifies that the RecordSetter will use default functionality
	 * and appearance(BUTTON + ICON).
	 */
	DEFAULT
}

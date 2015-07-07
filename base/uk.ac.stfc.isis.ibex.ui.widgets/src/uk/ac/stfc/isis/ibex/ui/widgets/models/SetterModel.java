
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
package uk.ac.stfc.isis.ibex.ui.widgets.models;

//import uk.ac.stfc.isis.ibex.ui.ModelObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public abstract class SetterModel { // extends ModelObject {
	
	/**
	 * Sends a request to write the currently stored value to
	 * the record.
	 */
	public abstract boolean setRecordValue();
	
	/**
	 * Sets the the value currently stored in this setter model but
	 * does not send a write request to the record.
	 */
	public abstract void setCurrentValue(String value);
	
	/**
	 * Gets a human-readable description of the record.
	 */
	public abstract String getDescription();
	
	/**
	 * Indicates whether or not the record pointed to by this setter
	 * is currently connected to its backing data store.
	 */
	public abstract boolean getIsConnected();
	
	/**
	 * Indicates whether or not the currentValue can be sent to the record
	 * at the present time.
	 */
	public abstract boolean getIsSendable();
	
	/**
	 * Gets a colour that is a signifier of the present state of the model.
	 */
	public abstract Color getColor();
	
	/**
	 * Gets an icon that is a signifier of the present state of the model.
	 */
	public abstract Image getImage();
	
	/**
	 * Gets a message describing the present state of the model.
	 */
	public abstract String getStatusMessage();
}

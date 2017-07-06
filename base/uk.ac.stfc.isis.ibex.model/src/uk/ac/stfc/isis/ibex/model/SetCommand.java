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

package uk.ac.stfc.isis.ibex.model;

import java.io.IOException;

/**
 * A class for sending a command to a model (usually from the UI). <br>
 * The class allows for cases when the command cannot be set for whatever
 * reason.
 * 
 * @param <T>
 *            The type of the data that should be sent
 */
public abstract class SetCommand<T> extends ModelObject {
		
	private boolean canSend;
	
	/**
	 * Sends the command.
	 * @param value the value to be sent
	 * @throws IOException if the send failed
	 */
	public abstract void send(T value) throws IOException;
	
	/**
	 * Sends the command and does not throw a checked exception.
	 * 
	 * This method should be avoided unless you are certain that the send cannot fail
	 * 
	 * @param value the value to be sent
	 */
	public abstract void uncheckedSend(T value);
	
	/**
	 * Whether values can be sent.
	 * @return true if values can be sent, false otherwise
	 */
	public boolean getCanSend() {
		return canSend;
	}

    /**
     * Sets whether a command can be sent.
     *
     * @param canSend
     *            True if a command can be sent; False otherwise
     */
	protected void setCanSend(boolean canSend) {
		firePropertyChange("canSend", this.canSend, this.canSend = canSend);
	}
}

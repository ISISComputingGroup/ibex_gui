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

package uk.ac.stfc.isis.ibex.validators;

/**
 * Error message class.
 *
 */
public class ErrorMessage {
	private String message;
	private boolean error;

	/**
     * Creator for an empty error message (ie when no error exists).
     *
     */
	public ErrorMessage() {
		error = false;
	}
	
	/**
     * Creator for an error message.
     *
     *@param error
     *              true if there is an error.
     *
     *@param message
     *                  the error message.
     */
	public ErrorMessage(boolean error, String message) {
		this.error = error;
		this.message = message;
	}

	/**
     * Allows to get the error message.
     *
     * @return
     *          the error message.
     */
	public String getMessage() {
		return message;
	}

	/**
     * Class used to set an error message..
     *
     * @param message
     *                  the error message to set.
     */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
     * Check if there is an error.
     *
     * @return
     *          true if there is an error, false otherwise.
     */
	public boolean isError() {
		return error;
	}

	/**
	    *Class to set the existence of an error.
	    *
	    * @param inError
	    *                  true if there is an error.
	    */
	public void setError(boolean inError) {
		this.error = inError;
	}
}

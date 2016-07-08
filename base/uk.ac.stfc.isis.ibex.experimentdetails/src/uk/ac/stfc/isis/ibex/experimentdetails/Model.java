
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The model for the experiment details information.
 */
public abstract class Model extends ModelObject {

	public abstract Collection<Parameter> getSampleParameters();
	
	protected abstract void setSampleParameters(Collection<Parameter> sampleParameters);

	public abstract Collection<Parameter> getBeamParameters();

	protected abstract void setBeamParameters(Collection<Parameter> beamParameters);
	
	/**
	 * @return An observable that tracks the RB number
	 */
	public abstract Observable<String> rbNumber();
	
	/**
	 * @return A writable for setting the RB number
	 */
	public abstract Writable<String> rbNumberSetter();
	
	/**
	 * @return The current user details in the model
	 */
	public abstract Collection<UserDetails> getUserDetails();

	/**
	 * Set the user details for the model.
	 * @param userDetails The new user details.
	 */
	public abstract void setUserDetails(Collection<UserDetails> userDetails);

	/**
	 * Send user details on to the DAE.
	 */
	public abstract void sendUserDetails();
	
	/**
	 * Add a new user to the model.
	 * The user will be added with default name etc.
	 * @return The newly added user.
	 */
	public abstract UserDetails addUser();
	
	/**
	 * Remove a specific user from the model.
	 * @param toRemove The user to remove.
	 */
	public abstract void removeUser(UserDetails toRemove);

	/**
	 * Remove all user details from the model.
	 */
	public abstract void clearUserDetails();
}

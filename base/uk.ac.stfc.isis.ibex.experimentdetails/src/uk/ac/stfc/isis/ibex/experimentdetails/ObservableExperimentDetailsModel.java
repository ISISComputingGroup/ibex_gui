
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.CollectionObserver;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.ExperimentDetailsVariables;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.UserDetailsConverter;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model for holding the current experiment details that is linked to a set of observables.
 */
public class ObservableExperimentDetailsModel extends ModelObject {
	
	private List<Parameter> sampleParameters = new ArrayList<>();
	private List<Parameter> beamParameters = new ArrayList<>();
	private Collection<UserDetails> userDetails = new ArrayList<>();
	
	private final String defaultUser = "DEFAULT_USER";
	private final String defaultOrg = "STFC";
		
	private final BaseObserver<Collection<Parameter>> sampleParametersObserver = new CollectionObserver<Parameter>() {
		@Override
		protected void updateCollection(Collection<Parameter> parameters) {
			setSampleParameters(parameters);
		}
	};
	
	private final BaseObserver<Collection<Parameter>> beamParametersObserver = new CollectionObserver<Parameter>() {
		@Override
		protected void updateCollection(Collection<Parameter> parameters) {
			setBeamParameters(parameters);
		}
		
	};
	
	private final BaseObserver<Collection<UserDetails>> userDetailsObserver = new CollectionObserver<UserDetails>() {
		@Override
		protected void updateCollection(Collection<UserDetails> values) {
			setUserDetails(values);
		}
	};
	
	private final ExperimentDetailsVariables variables;
	
	/**
	 * The constructor for the concrete Observable Model.
	 * @param variables The set of observables that this model should be linked to.
	 */
	public ObservableExperimentDetailsModel(ExperimentDetailsVariables variables) {
		variables.userDetails.subscribe(userDetailsObserver);
		variables.sampleParameters.subscribe(sampleParametersObserver);
		variables.beamParameters.subscribe(beamParametersObserver);
		this.variables = variables;
	}

    /**
     * @return the sample parameters
     */
    public Collection<Parameter> getSampleParameters() {
		return new ArrayList<>(sampleParameters);
	}

	/**
     * Sets the sample parameters.
     * 
     * @param sampleParameters the sample parameters to set.
     */
	protected void setSampleParameters(Collection<Parameter> sampleParameters) {
		firePropertyChange("sampleParameters", this.sampleParameters, this.sampleParameters = new ArrayList<>(sampleParameters));
	}

	/**
     * @return the beam parameters
     */
	public Collection<Parameter> getBeamParameters() {
		return new ArrayList<>(beamParameters);
	}

	/**
     * Sets the beam parameters.
     * 
     * @param beamParameters the beam parameters to set.
     */
	protected void setBeamParameters(Collection<Parameter> beamParameters) {
		firePropertyChange("beamParameters", this.beamParameters, this.beamParameters = new ArrayList<>(beamParameters));
	}

	/**
     * @return An observable that tracks the RB number
     */
    public ClosableObservable<String> rbNumber() {
		return variables.rbNumber;
	}

	/**
     * @return A writable for setting the RB number
     */
	public Writable<String> rbNumberSetter() {
		return variables.rbNumberSetter;
	}
	
	/**
     * @return An observable that tracks display title status
     */
    public ForwardingObservable<Boolean> displayTitle() {
		return variables.displayTitle;
	}

	/**
     * @return A writable for setting display title status
     */
	public Writable<Long> displayTitleSetter() {
		return variables.displayTitleSetter;
	}

	/**
     * @return The current user details in the model
     */
	public Collection<UserDetails> getUserDetails() {
		return new ArrayList<>(userDetails);
	}
	
	/**
     * Set the user details for the model.
     * @param values The new user details.
     */
	public void setUserDetails(Collection<UserDetails> values) {
		firePropertyChange("userDetails", this.userDetails, this.userDetails = new ArrayList<>(values));
	}

	/**
     * Send user details on to the DAE.
     */
	public void sendUserDetails() {
		variables.userDetailsSetter.uncheckedWrite(UserDetailsConverter.combineSameUsers(userDetails).toArray(new UserDetails[0]));
	}
	
	private String getDefaultUser() {
		String defaultName = defaultUser;
		int i = 1;
		
		List<String> userNames = new ArrayList<>();
		
		for (UserDetails user : userDetails) {
			userNames.add(user.getName());
		}
		
		while (userNames.contains(defaultName)) {
			defaultName = defaultUser + "_" + i++;
		}
		
		return defaultName;
	}
	
	/**
     * Add a new user to the model.
     * The user will be added with default name etc.
     */
	public void addDefaultUser() {
		ArrayList<UserDetails> newUsers = new ArrayList<>(userDetails);		
		newUsers.add(new UserDetails(getDefaultUser(), defaultOrg, Role.USER));
		setUserDetails(newUsers);
	}
	
	/**
     * Remove a specific group of users from the model.
     * @param toRemove The users to remove.
     */
	public void removeUsers(List<UserDetails> toRemove) {
		ArrayList<UserDetails> newUsers = new ArrayList<>(userDetails);
		newUsers.removeAll(toRemove);
		setUserDetails(newUsers);
	}
	
	/**
     * Remove all user details from the model.
     */
    public void clearUserDetails() {
        setUserDetails(new ArrayList<>());
    }
    
	/**
     * Checks if there are any user details in the model.
     * @return True if there aren't user details in the model, false if there are.
     */
    public boolean isUserDetailsEmpty() {
        return userDetails.isEmpty();
    }
}

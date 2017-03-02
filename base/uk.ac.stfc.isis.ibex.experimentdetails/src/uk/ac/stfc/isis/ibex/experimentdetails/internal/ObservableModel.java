
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

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Model;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetailsList;

/**
 * A model for holding the current experiment details that is linked to a set of observables.
 */
public class ObservableModel extends Model {
	
	private List<Parameter> sampleParameters = new ArrayList<>();
	private List<Parameter> beamParameters = new ArrayList<>();
	private UserDetailsList userDetails = new UserDetailsList();
	
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
	public ObservableModel(ExperimentDetailsVariables variables) {
		variables.userDetails.addObserver(userDetailsObserver);
		variables.sampleParameters.addObserver(sampleParametersObserver);
		variables.beamParameters.addObserver(beamParametersObserver);
		this.variables = variables;
	}

	@Override
	public Collection<Parameter> getSampleParameters() {
		return new ArrayList<>(sampleParameters);
	}

	@Override
	protected void setSampleParameters(Collection<Parameter> sampleParameters) {
		firePropertyChange("sampleParameters", this.sampleParameters, this.sampleParameters = new ArrayList<>(sampleParameters));
	}

	@Override
	public Collection<Parameter> getBeamParameters() {
		return new ArrayList<>(beamParameters);
	}

	@Override
	protected void setBeamParameters(Collection<Parameter> beamParameters) {
		firePropertyChange("beamParameters", this.beamParameters, this.beamParameters = new ArrayList<>(beamParameters));
	}

	@Override
    public ClosableObservable<String> rbNumber() {
		return variables.rbNumber;
	}

	@Override
	public Writable<String> rbNumberSetter() {
		return variables.rbNumberSetter;
	}

	@Override
	public Collection<UserDetails> getUserDetails() {
		return new ArrayList<>(userDetails);
	}
	
	@Override
	public void setUserDetails(Collection<UserDetails> values) {
		firePropertyChange("userDetails", this.userDetails, this.userDetails = new UserDetailsList(values));
	}

	@Override
	public void sendUserDetails() {
		userDetails.combineSameUsers();
		variables.userDetailsSetter.write(userDetails.toArray(new UserDetails[0]));
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
	
	@Override
	public void addDefaultUser() {
		Collection<UserDetails> originalUsers = getUserDetails();
		
		UserDetails user = new UserDetails(getDefaultUser(), defaultOrg, Role.USER);
		this.userDetails.add(user);
		firePropertyChange("userDetails", originalUsers, getUserDetails());
	}

	@Override
	public void clearUserDetails() {
		Collection<UserDetails> originalUsers = getUserDetails();
		this.userDetails = new UserDetailsList();
		firePropertyChange("userDetails", originalUsers, getUserDetails());
	}
	
	@Override
	public void removeUsers(List<UserDetails> toRemove) {
		Collection<UserDetails> originalUsers = getUserDetails();
		userDetails.removeAll(toRemove);
		firePropertyChange("userDetails", originalUsers, getUserDetails());
	}
}

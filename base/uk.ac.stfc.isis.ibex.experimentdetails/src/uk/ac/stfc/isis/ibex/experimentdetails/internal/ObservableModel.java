package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Model;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;

public class ObservableModel extends Model {
	
	private List<Parameter> sampleParameters = new ArrayList<>();
	private List<Parameter> beamParameters = new ArrayList<>();
	private List<UserDetails> userDetails = new ArrayList<>();
	
	private final String DEFAULT_USER = "DEFAULT_USER";
	private final String DEFAULT_ORG = "STFC";
		
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
	
	
	public ObservableModel(ExperimentDetailsVariables variables) {
		variables.userDetails.subscribe(userDetailsObserver);
		variables.sampleParameters.subscribe(sampleParametersObserver);
		variables.beamParameters.subscribe(beamParametersObserver);
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
	public CachingObservable<String> rbNumber() {
		return variables.rbNumber;
	}

	@Override
	public Writable<String> rbNumberSetter() {
		return variables.rbNumberSetter;
	}

	@Override
	public Collection<UserDetails> getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(Collection<UserDetails> values) {
		firePropertyChange("userDetails", this.userDetails, this.userDetails = new ArrayList<>(values));
	}

	@Override
	public void sendUserDetails() {
		variables.userDetailsSetter.write(userDetails.toArray(new UserDetails[0]));
	}
	
	public UserDetails addUser() {
		Collection<UserDetails> originalUsers = getUserDetails();
		
		UserDetails user = new UserDetails(DEFAULT_USER, DEFAULT_ORG, Role.BLANK);
		this.userDetails.add(user);
		firePropertyChange("userDetails", originalUsers, getUserDetails());
		return user;
	}

	@Override
	public void clearUserDetails() {
		Collection<UserDetails> originalUsers = getUserDetails();
		this.userDetails = new ArrayList<>();
		firePropertyChange("userDetails", originalUsers, getUserDetails());
	}
}

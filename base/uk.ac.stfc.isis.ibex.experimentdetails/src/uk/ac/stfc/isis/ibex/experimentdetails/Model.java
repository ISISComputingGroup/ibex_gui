package uk.ac.stfc.isis.ibex.experimentdetails;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class Model extends ModelObject {

	public abstract Collection<Parameter> getSampleParameters();
	
	protected abstract void setSampleParameters(Collection<Parameter> sampleParameters);

	public abstract Collection<Parameter> getBeamParameters();

	protected abstract void setBeamParameters(Collection<Parameter> beamParameters);
	
	public abstract CachingObservable<String> rbNumber();
	
	public abstract Writable<String> rbNumberSetter();
	
	public abstract Collection<UserDetails> getUserDetails();

	public abstract void setUserDetails(Collection<UserDetails> userDetails);

	public abstract void sendUserDetails();
	
	public abstract UserDetails addUser();

	public abstract void clearUserDetails();
}

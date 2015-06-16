package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;

public abstract class ParameterFieldObserver extends BaseObserver<String> {

	@Override
	public void onValue(String value) {
		updateField(value);
	}

	@Override
	public void onError(Exception e) {
		updateField(defaultValue());
	}

	@Override
	public void onConnectionChanged(boolean isConnected) {
		if (!isConnected) {
			updateField(defaultValue());
		}
		
	}
	
	protected abstract void updateField(String value);
	
	protected String defaultValue() {
		return "";
	}

}

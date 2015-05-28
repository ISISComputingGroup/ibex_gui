package uk.ac.stfc.isis.ibex.epics.adapters;

import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

import com.google.common.base.Strings;

public class TextUpdatedObservableAdapter extends UpdatedObservableAdapter<String> {

	private static final String UNKNOWN = "Unknown";
	private static final String EMPTY = "";
	
	public TextUpdatedObservableAdapter(CachingObservable<String> observable) {
		super(new InitialiseOnSubscribeObservable<>(observable));
	}
	
	public TextUpdatedObservableAdapter(
			InitialiseOnSubscribeObservable<String> observable) {
		super(observable);
	}
	
	@Override
	public void setValue(String value) { 
		super.setValue(Strings.isNullOrEmpty(value) ? EMPTY : value);	
	}

	@Override
	protected void error(Exception e) {
		setValue(UNKNOWN);
	}

	@Override
	protected void connectionChanged(boolean isConnected) {
		if (!isConnected) {
			setValue(UNKNOWN);
		}
	}
}

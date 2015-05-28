package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ReadableComponentProperty extends ComponentProperty {
	
	private final UpdatedValue<String> value; 
	
	public ReadableComponentProperty(String displayName, InitialiseOnSubscribeObservable<String> observable) {
		super(displayName);
		this.value = new TextUpdatedObservableAdapter(observable);
	}

	public UpdatedValue<String> value() {
		return value;
	}
}

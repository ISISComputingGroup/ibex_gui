package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;

public class EditableIocState extends IocState {
	
	private final InitialisableObserver<String> descriptionObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			EditableIocState.super.setDescription(value);
		}

		@Override
		public void onError(Exception e) {
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {			
		}
	};
	
	private final Writer<String> setDescription;

	public EditableIocState(
			IocState iocState, 
			InitialiseOnSubscribeObservable<String> description,
			Writer<String> setDescription) {
		super(iocState);
		description.subscribe(descriptionObserver);
		this.setDescription = setDescription;
	}
	
	@Override
	public void setDescription(String description) {
		setDescription.write(description);
	}
}

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;

public class ObservableParameter extends Parameter {
	
	private final BaseObserver<String> nameObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			setName(value);			
		}
		@Override
		protected String defaultValue() {
			return "Unknown";
		};
	};
	
	private final BaseObserver<String> unitsObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			setUnits(value);			
		}
	};
	
	private final BaseObserver<String> valueObserver = new ParameterFieldObserver() {
		@Override
		protected void updateField(String value) {
			ObservableParameter.super.setValue(value);			
		}
	};

	private final Writable<String> valueSetter;
	
	public ObservableParameter(
			InitialiseOnSubscribeObservable<String> name,
			InitialiseOnSubscribeObservable<String> units,
			InitialiseOnSubscribeObservable<String> value,
			Writable<String> valueSetter) {

		this.valueSetter = valueSetter;

		name.subscribe(nameObserver);
		units.subscribe(unitsObserver);
		value.subscribe(valueObserver);
	}
	
	@Override
	public void setValue(String value) {
		valueSetter.write(value);
	}
}

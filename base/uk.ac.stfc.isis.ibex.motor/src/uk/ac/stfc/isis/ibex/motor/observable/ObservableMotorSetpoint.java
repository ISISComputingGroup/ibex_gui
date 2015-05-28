package uk.ac.stfc.isis.ibex.motor.observable;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class ObservableMotorSetpoint extends MotorSetpoint {
	
	private final UpdatedObservableAdapter<Double> value;
	private final UpdatedObservableAdapter<Double> setpoint;
	private final UpdatedObservableAdapter<Boolean> canHome;
	private final SameTypeWriter<Double> home = new SameTypeWriter<>();
	private final SameTypeWriter<Double> setSetpoint = new SameTypeWriter<>();

	public ObservableMotorSetpoint(String name, MotorSetPointVariables variables) {
		super(name);
		
		value = adapt(variables.value, "value");
		setpoint = adapt(variables.setPoint, "setpoint");
		canHome = adapt(variables.canHome, "canHome");
		home.writeTo(variables.homeSetter);
		setSetpoint.writeTo(variables.setPointSetter);
	}

	@Override
	public Double getValue() {
		return value.getValue();
	}

	@Override
	public Double getSetpoint() {
		return setpoint.getValue();
	}

	@Override
	public Boolean canHome() {
		return canHome.getValue();
	}

	private <T> UpdatedObservableAdapter<T> adapt(InitialiseOnSubscribeObservable<T> variable, String field) {
		UpdatedObservableAdapter<T> adapted = new UpdatedObservableAdapter<>(variable);
		adapted.addPropertyChangeListener(raiseEventsFor(field));
		
		return adapted;
	}

	@Override
	public void home() {
		home.write(1.0);
	}

	@Override
	public void setSetpoint(double value) {
		setSetpoint.write(value);
	}
}

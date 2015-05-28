package uk.ac.stfc.isis.ibex.goniometer.internal;

import uk.ac.stfc.isis.ibex.goniometer.GoniometerModel;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;
import uk.ac.stfc.isis.ibex.motor.observable.ObservableMotorSetpoint;

public class ObservableGoniometerModel implements GoniometerModel {

	private final ObservableMotorSetpoint rUpper;
	private final ObservableMotorSetpoint rLower;
	private final ObservableMotorSetpoint cx;
	private final ObservableMotorSetpoint cy;
	private final ObservableMotorSetpoint theta;
	private final ObservableMotorSetpoint z;

	public ObservableGoniometerModel(Variables variables) {
		rUpper = new ObservableMotorSetpoint("RUPPER", variables.rUpper);
		rLower = new ObservableMotorSetpoint("RLOWER", variables.rLower);
		cx = new ObservableMotorSetpoint("CX", variables.cx);
		cy = new ObservableMotorSetpoint("CY", variables.cy);
		theta = new ObservableMotorSetpoint("THETA", variables.theta);
		z = new ObservableMotorSetpoint("Z", variables.z);
	}	
	
	@Override
	public MotorSetpoint rUpper() {
		return rUpper;
	}

	@Override
	public MotorSetpoint rLower() {
		return rLower;
	}

	@Override
	public MotorSetpoint cx() {
		return cx;
	}

	@Override
	public MotorSetpoint cy() {
		return cy;
	}

	@Override
	public MotorSetpoint theta() {
		return theta;
	}

	@Override
	public MotorSetpoint z() {
		return z;
	}
}

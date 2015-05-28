package uk.ac.stfc.isis.ibex.motor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.observable.MotorVariables;
import uk.ac.stfc.isis.ibex.motor.observable.ObservableMotor;

public class MotorsTable extends Closer {
	
	private static final String MOTOR_NAME_FORMAT = "MTR%02d%02d";
	private static final int CRATES = 7;
	private static final int MOTORS = 8;
	
	private List<Motor> motors = new ArrayList<>();
	
	public MotorsTable(Instrument instrument) {
		
		for (int crate = 1; crate <= CRATES; crate++) {
			for (int motorNumber = 1; motorNumber <= MOTORS; motorNumber++) {
				String name = motorName(crate, motorNumber);
				MotorVariables variables = registerForClose(new MotorVariables(name, instrument));
				Motor motor = new ObservableMotor(variables);

				motors.add(motor);
			}
		}
	}
	
	public Collection<Motor> motors() {
		return new ArrayList<>(motors);
	}
	
	private String motorName(int row, int column) {
		return String.format(MOTOR_NAME_FORMAT, row, column);
	}
}

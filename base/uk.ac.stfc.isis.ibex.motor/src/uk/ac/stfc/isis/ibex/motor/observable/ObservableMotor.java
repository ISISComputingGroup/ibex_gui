
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.motor.observable;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;

/**
 * A motor that is pointing to an actual device.
 */
public class ObservableMotor extends Motor {

	private final MotorVariables variables;
	private final TextUpdatedObservableAdapter description;
	private final UpdatedObservableAdapter<Double> setpoint;
	private final UpdatedObservableAdapter<Double> value;
	private final UpdatedObservableAdapter<MotorEnable> enabled;
	private final UpdatedObservableAdapter<Double> lowerLimit;
	private final UpdatedObservableAdapter<Double> upperLimit;
	private final UpdatedObservableAdapter<Double> offset;
	private final UpdatedObservableAdapter<Double> error;
	private final UpdatedObservableAdapter<MotorDirection> direction;
	private final UpdatedObservableAdapter<Boolean> moving;
	private final UpdatedObservableAdapter<Boolean> doneMoving;
	private final UpdatedObservableAdapter<Boolean> atHome;
	private final UpdatedObservableAdapter<Boolean> atLowerLimitSwitch;
	private final UpdatedObservableAdapter<Boolean> atUpperLimitSwitch;
	private final UpdatedObservableAdapter<Boolean> usingEncoder;
	private final UpdatedObservableAdapter<Boolean> energised;
	private final UpdatedObservableAdapter<Boolean> withinTolerance;
	
    /**
     * Creates a motor that is pointing to a backend device.
     * 
     * @param variables
     *            The PVs for the motor.
     */
	public ObservableMotor(MotorVariables variables) {
		this.variables = variables;
		description = textAdapt(variables.description, "description");
		setpoint = adapt(variables.setpoint, "setpoint");
		value = adapt(variables.value, "value");
		
		enabled = adapt(variables.enable, "enabled");		
		lowerLimit = adapt(variables.lowerLimit, "lowerLimit");
		upperLimit = adapt(variables.upperLimit, "upperLimit");

		offset = adapt(variables.offset, "offset");
		error = adapt(variables.error, "error");
		
		direction = adapt(variables.direction, "direction");
		moving = adapt(variables.moving, "moving");
		doneMoving = adapt(variables.doneMoving, "doneMoving");
		atHome = adapt(variables.atHome, "atHome");
		atLowerLimitSwitch = adapt(variables.atLowerLimitSwitch, "atLowerLimitSwitch");
		atUpperLimitSwitch = adapt(variables.atUpperLimitSwitch, "atUpperLimitSwitch");
		
		usingEncoder = adapt(variables.usingEncoder, "usingEncoder");
		energised = adapt(variables.energised, "energised");
		
		withinTolerance = adapt(variables.withinTolerance, "withinTolerance");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name() {
		return variables.motorName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String motorAddress() {
		return variables.motorAddress();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getSetpoint() {
		return setpoint.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getValue() {
		return value.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MotorEnable getEnabled() {
		return enabled.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getLowerLimit() {
		return lowerLimit.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getUpperLimit() {
		return upperLimit.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getOffset() {
		return offset.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getError() {
		return error.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MotorDirection getDirection() {
		return direction.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getMoving() {
		return moving.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getDoneMoving() {
		return doneMoving.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getAtHome() {
		return atHome.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getAtLowerLimitSwtich() {
		return atLowerLimitSwitch.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getAtUpperLimitSwitch() {
		return atUpperLimitSwitch.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getUsingEncoder() {
		return usingEncoder.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getEnergised() {
		return energised.getValue();
	}
	
	private <T> UpdatedObservableAdapter<T> adapt(ForwardingObservable<T> variable, String field) {
		UpdatedObservableAdapter<T> adapted = new UpdatedObservableAdapter<>(variable);
		adapted.addPropertyChangeListener(raiseEventsFor(field));
		
		return adapted;
	}
	
	private TextUpdatedObservableAdapter textAdapt(ForwardingObservable<String> variable, String field) {
		TextUpdatedObservableAdapter adapted = new TextUpdatedObservableAdapter(variable);
		adapted.addPropertyChangeListener(raiseEventsFor(field));
		
		return adapted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getWithinTolerance() {
		return withinTolerance.getValue();
	}
}

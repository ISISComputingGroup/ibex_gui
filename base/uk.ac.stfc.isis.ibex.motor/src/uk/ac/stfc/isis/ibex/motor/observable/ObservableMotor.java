
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
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class ObservableMotor extends Motor {

	private final MotorVariables variables;
	private final TextUpdatedObservableAdapter description;
	private final MotorSetpoint setpoint;
	private final UpdatedObservableAdapter<MotorEnable> enabled;
	private final UpdatedObservableAdapter<Double> lowerLimit;
	private final UpdatedObservableAdapter<Double> upperLimit;
	private final UpdatedObservableAdapter<MotorDirection> direction;
	private final UpdatedObservableAdapter<Boolean> moving;
	private final UpdatedObservableAdapter<Boolean> atHome;
	private final UpdatedObservableAdapter<Boolean> atLowerLimitSwitch;
	private final UpdatedObservableAdapter<Boolean> atUpperLimitSwitch;
	private final TextUpdatedObservableAdapter status;
	
	public ObservableMotor(MotorVariables variables) {
		this.variables = variables;
		description = textAdapt(variables.description, "description");
		setpoint = new ObservableMotorSetpoint(variables.motorName, variables.setpoint);
		
		enabled = adapt(variables.enable, "enabled");		
		lowerLimit = adapt(variables.lowerLimit, "lowerLimit");
		upperLimit = adapt(variables.upperLimit, "upperLimit");
		
		direction = adapt(variables.direction, "direction");
		moving = adapt(variables.moving, "moving");
		atHome = adapt(variables.atHome, "atHome");
		atLowerLimitSwitch = adapt(variables.atLowerLimitSwitch, "atLowerLimitSwitch");
		atUpperLimitSwitch = adapt(variables.atUpperLimitSwitch, "atUpperLimitSwitch");
		
		status = textAdapt(variables.status, "status");
	}

	@Override
	public String name() {
		return variables.motorName;
	}
	
	@Override
	public String motorAddress() {
		return variables.motorAddress();
	}
	
	@Override
	public String getDescription() {
		return description.getValue();
	}

	@Override
	public MotorSetpoint getSetpoint() {
		return setpoint;
	}

	@Override
	public MotorEnable getEnabled() {
		return enabled.getValue();
	}

	@Override
	public Double getLowerLimit() {
		return lowerLimit.getValue();
	}

	@Override
	public Double getUpperLimit() {
		return upperLimit.getValue();
	}

	@Override
	public MotorDirection getDirection() {
		return direction.getValue();
	}

	@Override
	public Boolean getMoving() {
		return moving.getValue();
	}

	@Override
	public Boolean getAtHome() {
		return atHome.getValue();
	}

	@Override
	public Boolean getAtLowerLimitSwtich() {
		return atLowerLimitSwitch.getValue();
	}

	@Override
	public Boolean getAtUpperLimitSwitch() {
		return atUpperLimitSwitch.getValue();
	}

	@Override
	public String getStatus() {
		return status.getValue();
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
}

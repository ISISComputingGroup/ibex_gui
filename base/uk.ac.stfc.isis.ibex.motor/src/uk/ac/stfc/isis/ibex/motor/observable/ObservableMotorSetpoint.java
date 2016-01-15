
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

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
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

	private <T> UpdatedObservableAdapter<T> adapt(ForwardingObservable<T> variable, String field) {
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

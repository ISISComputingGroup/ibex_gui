
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

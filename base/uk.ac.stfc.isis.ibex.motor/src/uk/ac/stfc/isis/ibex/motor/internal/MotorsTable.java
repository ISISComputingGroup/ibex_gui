
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

package uk.ac.stfc.isis.ibex.motor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.observable.MotorVariables;
import uk.ac.stfc.isis.ibex.motor.observable.ObservableMotor;

public class MotorsTable extends Closer {
	
	private static final String MOTOR_NAME_FORMAT = "MTR%02d%02d";
	private int numberMotors;
	private int numberCrates;
	
	private List<Motor> motors = new ArrayList<>();
	
    public MotorsTable(int numberCrates, int numberMotors, int startCrate) {
		this.numberMotors = numberMotors;
		this.numberCrates = numberCrates;
		
		for (int crate = startCrate; crate < startCrate + numberCrates; crate++) {
			for (int motorNumber = 1; motorNumber <= numberMotors; motorNumber++) {
				String name = motorName(crate, motorNumber);
                MotorVariables variables = registerForClose(new MotorVariables(name));
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
	
	public int getNumMotors() {
		return numberMotors;
	}
	
	public int getNumCrates() {
		return numberCrates;
	}
}

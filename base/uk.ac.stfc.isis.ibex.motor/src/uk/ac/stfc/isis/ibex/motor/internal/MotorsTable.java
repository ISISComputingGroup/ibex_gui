
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
import java.util.Map.Entry;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.observable.MotorVariables;
import uk.ac.stfc.isis.ibex.motor.observable.ObservableMotor;
import java.util.TreeMap;
/**
 * A table containing information about the motors.
 */
public class MotorsTable extends Closer {
	
	private static final String MOTOR_NAME_FORMAT = "MTR%02d%02d";

	private TreeMap<Integer, Integer> numMotorsPerControllers = new TreeMap<>();
	
	private TreeMap<Integer, ArrayList<Motor>> motors = new TreeMap<>();
	
	/**
	 * Create the table.
	 * 
	 * @param instrument the instrument
	 * @param numberCrates the number of controllers (crates) in each table. of crates
	 * @param numberMotors the number of motors for each controller
	 * @param startCrate the crate to start on
	 */
	public MotorsTable(Instrument instrument, int numberCrates, int numberMotors, int startCrate) {
		for (int crate = startCrate; crate < startCrate + numberCrates; crate++) {
			this.numMotorsPerControllers.put(crate, numberMotors); 
		}
		
		for (Entry<Integer, Integer> entry : this.numMotorsPerControllers.entrySet() ) {
			int crate = entry.getKey();
			int numMotors = entry.getValue();
			motors.put(crate, new ArrayList<Motor>());
			for (int motorNumber = 1; motorNumber <= numMotors; motorNumber++) {
				String name = motorName(crate, motorNumber);
				MotorVariables variables = registerForClose(new MotorVariables(name, instrument));
				Motor motor = new ObservableMotor(variables);
				motors.get(crate).add(motor);
			}
		}
	}
	
	/**
	 * Gets the motors in this table.
	 * @return the motors in this table
	 */
	public TreeMap<Integer,ArrayList<Motor>> motors() {
		return motors;
	}
	
	private String motorName(int row, int column) {
		return String.format(MOTOR_NAME_FORMAT, row, column);
	}
	
	/**
	 * @return the number of motors per controller/crate
	 */
	public int getNumMotors(int crateNum) {
		return numMotorsPerControllers.get(crateNum);
	}
	
	public int getTotalNumMotors() {
		int count = 0; 
		for (int numOfMotors : this.numMotorsPerControllers.values()) {
			count += numOfMotors;
		}
		return count;
	}
	
	/**
     * @return the number of crates/controllers
     */
	public int getNumCrates() {
		return numMotorsPerControllers.size();
	}
}

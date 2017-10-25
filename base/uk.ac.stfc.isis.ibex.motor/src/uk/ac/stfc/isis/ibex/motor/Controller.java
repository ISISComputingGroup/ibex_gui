 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.motor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.observable.MotorVariables;
import uk.ac.stfc.isis.ibex.motor.observable.ObservableMotor;

/**
 * A class that contains information for one motor controller.
 */
public class Controller extends ModelObject {
    private static final String MOTOR_NAME_FORMAT = "MTR%02d%02d";

    private int controllerNum;
    private List<Motor> motors = new ArrayList<>();

    /**
     * Creates a controller that controls a number of motors.
     * 
     * @param controllerNum
     *            The controller number, corresponds to which PVs to look at.
     * @param numberMotors
     *            The number of motors that this controller has.
     */
    public Controller(int controllerNum, int numberMotors) {
        this.controllerNum = controllerNum;
        for (int motorNumber = 1; motorNumber <= numberMotors; motorNumber++) {
            String name = motorName(motorNumber);
            MotorVariables variables = new MotorVariables(name);
            Motor motor = new ObservableMotor(this, variables);

            motors.add(motor);
        }
    }

    private String motorName(int motorNum) {
        return String.format(MOTOR_NAME_FORMAT, controllerNum, motorNum);
    }

    /**
     * Get the motors that this controller is connected to.
     * 
     * @return The motors connected to this controller.
     */
    public Collection<Motor> motors() {
        return motors;
    }
}

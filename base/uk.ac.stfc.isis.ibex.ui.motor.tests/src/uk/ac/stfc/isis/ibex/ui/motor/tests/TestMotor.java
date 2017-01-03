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
package uk.ac.stfc.isis.ibex.ui.motor.tests;

import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorDirection;
import uk.ac.stfc.isis.ibex.motor.MotorEnable;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class TestMotor extends Motor {

    public String name = "Motor name";
    public String address = "Address";
    public String description = "Description";
    public TestMotorSetpoint testMotorSetpoint = new TestMotorSetpoint();
    public MotorEnable enabled = MotorEnable.ENABLE;
    public Double lowerLimit = -5.0;
    public Double upperLimit = 5.0;
    public MotorDirection direction = MotorDirection.POSITIVE;
    public boolean moving = false;
    public boolean atHome = false;
    public boolean atLowerLimit = false;
    public boolean atUpperLimit = false;
    public String status = "Motor status";

    @Override
    public String name() {
        return name;
    }

    @Override
    public String motorAddress() {
        return address;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public MotorSetpoint getSetpoint() {
        return testMotorSetpoint;
    }

    @Override
    public MotorEnable getEnabled() {
        return enabled;
    }

    @Override
    public Double getLowerLimit() {
        return lowerLimit;
    }

    @Override
    public Double getUpperLimit() {
        return upperLimit;
    }

    @Override
    public MotorDirection getDirection() {
        return direction;
    }

    @Override
    public Boolean getMoving() {
        return moving;
    }

    @Override
    public Boolean getAtHome() {
        return atHome;
    }

    @Override
    public Boolean getAtLowerLimitSwtich() {
        return atLowerLimit;
    }

    @Override
    public Boolean getAtUpperLimitSwitch() {
        return atUpperLimit;
    }

    @Override
    public String getStatus() {
        return status;
    }

}

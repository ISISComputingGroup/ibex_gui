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

public class TestMotor extends Motor {

    public String name = "Motor name";
    public String address = "Address";
    public String description = "Description";
    public Double value = -1.23456;
    public Double setpoint = 1.23456;
    public MotorEnable enabled = MotorEnable.ENABLE;
    public Double lowerLimit = -5.0;
    public Double upperLimit = 5.0;
    public MotorDirection direction = MotorDirection.POSITIVE;
    public boolean moving = false;
    public boolean doneMoving = true;
    public boolean atHome = false;
    public boolean atLowerLimit = false;
    public boolean atUpperLimit = false;
	private Double offset = 0.0;
	private Double error = 0.0;
	private boolean usingEncoder = true;
	private boolean energised = true;
	private boolean withinTolerance = true;

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
    public Double getValue() {
        return value;
    }

    @Override
    public Double getSetpoint() {
        return setpoint;
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
	public Double getOffset() {
		return offset;
	}

	@Override
	public Double getError() {
		return error;
	}

	@Override
	public Boolean getUsingEncoder() {
		return usingEncoder;
	}

	@Override
	public Boolean getEnergised() {
		return energised;
	}

	@Override
	public Boolean getWithinTolerance() {
		return withinTolerance;
	}

	@Override
	public Boolean getDoneMoving() {
		return doneMoving;
	}

}


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

package uk.ac.stfc.isis.ibex.motor;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The abstract motor class.
 */
public abstract class Motor extends ModelObject {

    /**
     * Gets the motor name.
     * 
     * @return the name
     */
	public abstract String name();
	
    /**
     * Gets the motor address
     * 
     * @return the address
     */
	public abstract String motorAddress();

    /**
     * Gets the description.
     * 
     * @return the description
     */
	public abstract String getDescription();
	
    /**
     * Gets the current setpoint.
     * 
     * @return the setpoint
     */
	public abstract MotorSetpoint getSetpoint();
	
    /**
     * Gets whether the motor is enabled.
     * 
     * @return enabled or not
     */
	public abstract MotorEnable getEnabled();
	
    /**
     * Gets the lower limit.
     * 
     * @return the lower limit
     */
	public abstract Double getLowerLimit();
		
    /**
     * Gets the upper limit.
     * 
     * @return the upper limit
     */
	public abstract Double getUpperLimit();

    /**
     * Gets the motor direction.
     * 
     * @return the direction
     */
	public abstract MotorDirection getDirection();
	
    /**
     * Gets whether the motor is moving.
     * 
     * @return whether it is moving
     */
	public abstract Boolean getMoving();
	
    /**
     * Gets whether the motor is at the home position.
     * 
     * @return whether it is at home
     */
	public abstract Boolean getAtHome();

    /**
     * Gets whether the motor is at the lower limit switch.
     * 
     * @return whether it is at the limit switch
     */
	public abstract Boolean getAtLowerLimitSwtich();

    /**
     * Gets whether the motor is at the upper limit switch.
     * 
     * @return whether it is at the limit switch
     */
	public abstract Boolean getAtUpperLimitSwitch();
	
    /**
     * Gets the status of the motor.
     * 
     * @return the status
     */
	public abstract String getStatus();
}

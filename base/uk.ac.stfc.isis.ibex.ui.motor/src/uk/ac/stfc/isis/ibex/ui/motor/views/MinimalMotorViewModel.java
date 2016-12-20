
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */

package uk.ac.stfc.isis.ibex.ui.motor.views;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class MinimalMotorViewModel extends ModelObject {

	private Motor motor;
    // private Double value;
    private MotorSetpoint setpoint;

    public MinimalMotorViewModel() {
    }
	
    public String getSetpoint() {
        this.setpoint = motor.getSetpoint();
        if (this.setpoint != null) {
            return String.format("SP: %.2f", this.setpoint.getSetpoint());
		} else {
			return "";
		}
	}
	
    public void setSetpoint(Motor newMotor) {
        this.motor = newMotor;
        MotorSetpoint newSetpointValue = newMotor.getSetpoint();
        firePropertyChange("setpoint", this.setpoint, newSetpointValue);

        this.setpoint = newMotor.getSetpoint();
	}
	
    public void setMotor(Motor motor) {
        this.motor = motor;
    }
	
}

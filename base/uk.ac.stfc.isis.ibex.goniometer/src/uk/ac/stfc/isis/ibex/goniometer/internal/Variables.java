
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

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.motor.observable.MotorSetPointVariables;

public class Variables {

	private static final PVAddress GONIO = PVAddress.startWith("GONIO");

	public final MotorSetPointVariables rUpper;
	public final MotorSetPointVariables rLower;
	public final MotorSetPointVariables cx;
	public final MotorSetPointVariables cy;
	public final MotorSetPointVariables z;
	public final MotorSetPointVariables theta;
	
    public Variables() {
        WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

        rUpper = new MotorSetPointVariables(GONIO.append("RUPPER"), obsFactory, writeFactory);
        rLower = new MotorSetPointVariables(GONIO.append("RLOWER"), obsFactory, writeFactory);
        cx = new MotorSetPointVariables(GONIO.append("CX"), obsFactory, writeFactory);
        cy = new MotorSetPointVariables(GONIO.append("CY"), obsFactory, writeFactory);
        z = new MotorSetPointVariables(GONIO.append("Z"), obsFactory, writeFactory);
        theta = new MotorSetPointVariables(GONIO.append("THETA"), obsFactory, writeFactory);
	}
}

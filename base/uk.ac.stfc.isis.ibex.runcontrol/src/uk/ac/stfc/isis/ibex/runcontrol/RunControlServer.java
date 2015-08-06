package uk.ac.stfc.isis.ibex.runcontrol;

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

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;

public class RunControlServer extends Closer {
	private RunControlVariables variables;
	
	public RunControlServer(RunControlVariables variables) {
		this.variables = variables;
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlLowLimit(String blockName) {
		return variables.blockRunControlLowLimit(blockName);
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlHighLimit(String blockName) {
		return variables.blockRunControlHighLimit(blockName);
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlInRange(String blockName) {
		return variables.blockRunControlInRange(blockName);
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlEnabled(String blockName) {
		return variables.blockRunControlEnabled(blockName);
	}

}

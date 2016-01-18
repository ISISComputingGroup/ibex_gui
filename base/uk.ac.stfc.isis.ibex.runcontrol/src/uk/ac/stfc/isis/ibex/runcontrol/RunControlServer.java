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

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableSameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.runcontrol.internal.RunControlVariables;

/**
 * The run-control server.
 *
 */
public class RunControlServer extends Closer {
	private RunControlVariables variables;
	
	public RunControlServer(RunControlVariables variables) {
		this.variables = variables;
	}
	
	public ForwardingObservable<String> blockRunControlLowLimit(String blockName) {
		return variables.blockRunControlLowLimit(blockName);
	}
	
	public ForwardingObservable<String> blockRunControlHighLimit(String blockName) {
		return variables.blockRunControlHighLimit(blockName);
	}
	
	public ForwardingObservable<String> blockRunControlInRange(String blockName) {
		return variables.blockRunControlInRange(blockName);
	}
	
	public ForwardingObservable<String> blockRunControlEnabled(String blockName) {
		return variables.blockRunControlEnabled(blockName);
	}

	public Writer<String> blockRunControlLowLimitSetter(String blockName) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlLowLimitSetter(blockName)));
	}
	
	public Writer<String> blockRunControlHighLimitSetter(String blockName) {
        return registerForClose(
                ClosableSameTypeWriter.newInstance(variables.blockRunControlHighLimitSetter(blockName)));
	}
	
	public Writer<String> blockRunControlEnabledSetter(String blockName) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlEnabledSetter(blockName)));
	}
}

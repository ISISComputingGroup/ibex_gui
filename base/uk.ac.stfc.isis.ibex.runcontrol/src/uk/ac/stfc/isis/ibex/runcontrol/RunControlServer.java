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
	
	/**
	 * Constructor.
	 *
	 * @param variables the run-control variables
	 */
	public RunControlServer(RunControlVariables variables) {
		this.variables = variables;
	}
	
	/**
     * Gets an observable looking at the run-control low limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> blockRunControlLowLimit(String blockName) {
		return variables.blockRunControlLowLimit(blockName);
	}
	
	/**
     * Gets an observable looking at the run-control high limit PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Double> blockRunControlHighLimit(String blockName) {
		return variables.blockRunControlHighLimit(blockName);
	}
	
	/**
     * Gets an observable looking at the run-control suspend if invalid PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<Boolean> blockRunControlSuspendIfInvalid(String blockName) {
		return variables.blockRunControlSuspendIfInvalid(blockName);
	}
	
	/**
     * Gets an observable looking at the run-control in range PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<String> blockRunControlInRange(String blockName) {
		return variables.blockRunControlInRange(blockName);
	}
	
	/**
     * Gets an observable looking at the run-control enabled PV.
     *
     * @param blockName the name of the block
     * @return the forwarding observable
     */
	public ForwardingObservable<String> blockRunControlEnabled(String blockName) {
		return variables.blockRunControlEnabled(blockName);
	}

	/**
     * Gets a writable for the run-control low limit setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writer<Double> blockRunControlLowLimitSetter(String blockName) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlLowLimitSetter(blockName)));
	}
	
	/**
     * Gets a writable for the run-control high limit setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writer<Double> blockRunControlHighLimitSetter(String blockName) {
        return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlHighLimitSetter(blockName)));
	}
	
	/**
     * Gets a writable for the run-control enabled setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writer<String> blockRunControlEnabledSetter(String blockName) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlEnabledSetter(blockName)));
	}
	

	
	/**
     * Gets a writable for the run-control enabled setter PV.
     *
     * @param blockName the name of the block
     * @return the writable
     */
	public Writer<String> blockRunControlSuspendIfInvalidSetter(String blockName) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.blockRunControlSuspendIfInvalidSetter(blockName)));
	}
}

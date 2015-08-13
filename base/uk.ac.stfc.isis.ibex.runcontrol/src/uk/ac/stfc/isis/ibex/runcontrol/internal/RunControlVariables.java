
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

package uk.ac.stfc.isis.ibex.runcontrol.internal;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Creates the various run-control variables. 
 *
 */
public class RunControlVariables extends InstrumentVariables {

	private final RunControlAddresses runControlAddresses = new RunControlAddresses();
	
	public RunControlVariables(Channels channels) {
		super(channels);
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlLowLimit(String blockName) {
		return reader(new DefaultChannel(), runControlAddresses.getLowLimitPv(blockName));
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlHighLimit(String blockName) {
		return reader(new DefaultChannel(), runControlAddresses.getHighLimitPv(blockName));
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlInRange(String blockName) {
		return reader(new DefaultChannel(), runControlAddresses.getInRangePv(blockName));
	}
	
	public InitialiseOnSubscribeObservable<String> blockRunControlEnabled(String blockName) {
		return reader(new DefaultChannel(), runControlAddresses.getEnablePv(blockName));
	}
	
	public Writable<String> blockRunControlLowLimitSetter(String blockName) {
		return writable(new StringChannel(), runControlAddresses.getLowLimitPv(blockName));
	}
	
	public Writable<String> blockRunControlHighLimitSetter(String blockName) {
		return writable(new StringChannel(), runControlAddresses.getHighLimitPv(blockName));
	}
	
	public Writable<String> blockRunControlEnabledSetter(String blockName) {
		return writable(new StringChannel(), runControlAddresses.getEnablePv(blockName));
	}

}

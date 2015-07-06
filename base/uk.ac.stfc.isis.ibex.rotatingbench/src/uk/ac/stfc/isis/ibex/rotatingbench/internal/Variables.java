
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

package uk.ac.stfc.isis.ibex.rotatingbench.internal;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.rotatingbench.Command;
import uk.ac.stfc.isis.ibex.rotatingbench.LiftStatus;
import uk.ac.stfc.isis.ibex.rotatingbench.Status;

public class Variables extends InstrumentVariables {

	private static final String ANGLE_SP_ADDRESS = "ROTB:ANGLE:SP";
	
	public final InitialiseOnSubscribeObservable<Double> angle = reader(new DoubleChannel(), "ROTB:ANGLE");
	public final Writable<Double> angleSP = writable(new DoubleChannel(), ANGLE_SP_ADDRESS);
	public final InitialiseOnSubscribeObservable<Double> angleSPValue = reader(new DoubleChannel(), ANGLE_SP_ADDRESS);
	public final InitialiseOnSubscribeObservable<Double> angleSPRBV = reader(new DoubleChannel(), "ROTB:ANGLE:SP:RBV");

	public final InitialiseOnSubscribeObservable<Status> status = reader(new EnumChannel<Status>(Status.class), "ROTB:STATUS");
	public final InitialiseOnSubscribeObservable<Command> checkHV = reader(new EnumChannel<Command>(Command.class), "ROTB:CHECK_HV");
	public final Writable<String> writeCheckHV = writable(new StringChannel(), "ROTB:CHECK_HV");
	public final InitialiseOnSubscribeObservable<LiftStatus> liftStatus = reader(new EnumChannel<LiftStatus>(LiftStatus.class), "BENCH:STATUS");

	
	public Variables(Channels channels) {
		super(channels);
	}

}

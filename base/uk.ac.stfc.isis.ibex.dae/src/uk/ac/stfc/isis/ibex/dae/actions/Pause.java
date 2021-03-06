
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

package uk.ac.stfc.isis.ibex.dae.actions;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This action will pause the current run on the DAE.
 */
public final class Pause extends DaeAction {

	private static final Collection<DaeRunState> ALLOWED = new ArrayList<>();
	static {
		ALLOWED.add(DaeRunState.RUNNING);
		ALLOWED.add(DaeRunState.WAITING);
		ALLOWED.add(DaeRunState.VETOING);
	}
	
	/**
     * Constructor for the pause action.
     * 
     * @param target
     *            The PV that must be written to to abort the run.
     * @param inStateTransition
     *            An observable to check the DAE is not transitioning.
     * @param runState
     *            An observable on the current state of the PV.
     */    
	public Pause(Writable<String> target,
			ForwardingObservable<Boolean> inStateTransition,
			ForwardingObservable<DaeRunState> runState) {
		super(target, inStateTransition, runState);
	}

	@Override
	protected boolean allowed(DaeRunState runState) {
		return ALLOWED.contains(runState);
	}
}


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

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.dae.DaeWritables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.Action;

/**
 * Class that creates and contains all the state transitioning actions on the
 * DAE.
 */
public class DaeActions extends Closer {

    /**
     * Begin a run on the DAE.
     */
	public final Action begin;
    /**
     * End a run on the DAE.
     */
	public final Action end;
    /**
     * Pause a run on the DAE.
     */
	public final Action pause;
    /**
     * Resume a paused run on the DAE.
     */
	public final Action resume;
    /**
     * Abort the current run.
     */
	public final Action abort;
    /**
     * Cancel the abort of the current run.
     */
	public final Action cancelAbort;
    /**
     * Save a snapshot of the run to disk, without ending the run.
     */
	public final Action save;
	
    /**
     * Constructor for the actions class. Will create all the actions available
     * for the DAE.
     * 
     * @param targets
     *            The object containing the writables for the DAE.
     * @param observables
     *            The object containing the observables for the DAE.
     */
	public DaeActions(DaeWritables targets, DaeObservables observables) {
		begin = registerForClose(new Begin(targets.begin, observables.inStateTransition, observables.runState));
		end = registerForClose(new End(targets.end, observables.inStateTransition, observables.runState));
		pause = registerForClose(new Pause(targets.pause, observables.inStateTransition, observables.runState));
		resume = registerForClose(new Resume(targets.resume, observables.inStateTransition, observables.runState));
		abort = registerForClose(new Abort(targets.abort, observables.inStateTransition, observables.runState));
		cancelAbort = registerForClose(new CancelAbort(targets.recover, observables.inStateTransition, observables.runState));
		save = registerForClose(new Save(targets.save, observables.inStateTransition, observables.runState));
	}
}


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

public class DaeActions extends Closer {

	public final Action begin;
	public final Action end;
	public final Action pause;
	public final Action resume;
	public final Action abort;
	public final Action cancelAbort;
	public final Action save;
	
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


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

package uk.ac.stfc.isis.ibex.dae;

import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.dae.spectra.Spectra;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public class DaeModel extends Closer implements IDae {
		
	private final ExperimentSetup experimentSetup;
	private final DaeActions actions;
	private final DaeObservables observables;
	private final DaeWritables writables;
	private final Spectra spectra;
	
	private final InitialiseOnSubscribeObservable<Boolean> isRunning;
	
	public DaeModel(DaeWritables writables, DaeObservables observables) {		
		this.observables = observables;
		this.writables = writables;
		
		experimentSetup = registerForClose(new ExperimentSetup(observables, writables));
		actions = registerForClose(new DaeActions(writables, observables));
		spectra = new Spectra(observables);

		isRunning = registerForClose(new InitialiseOnSubscribeObservable<>(new DaeIsRunning(observables.runState)));
	}

	@Override
	public InitialiseOnSubscribeObservable<String> instrument() {
		return observables.instrumentName;
	}
	
	@Override
	public InitialiseOnSubscribeObservable<String> runNumber() {
		return observables.runNumber;
	}

	@Override
	public InitialiseOnSubscribeObservable<DaeRunState> runState() {
		return observables.runState;
	}

	@Override
	public InitialiseOnSubscribeObservable<Boolean> isRunning() {
		return isRunning;
	}
	
	@Override
	public InitialiseOnSubscribeObservable<String> isisCycle() {
		return observables.isisCycle;
	}

	@Override
	public InitialiseOnSubscribeObservable<String> title() {
		return observables.title;
	}
	
	@Override
	public Writable<String> setTitle() {
		return writables.title;
	}
	
	@Override
	public InitialiseOnSubscribeObservable<String> vetos() {
		return observables.vetos;
	}
	
	@Override
    public ExperimentSetup experimentSetup() {
		return experimentSetup;
	}
		
	@Override
	public DaeActions actions() {
		return actions;
	}
	
	@Override
    public Spectra spectra() {
		return spectra;
	}
}

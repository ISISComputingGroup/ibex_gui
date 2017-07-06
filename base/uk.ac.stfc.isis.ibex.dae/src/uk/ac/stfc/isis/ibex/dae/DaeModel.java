
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
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * The Class DaeModel is the back end model for the DAE perspective.
 */
public class DaeModel extends Closer implements IDae {
		
	private final ExperimentSetup experimentSetup;
	private final DaeActions actions;
	private final DaeObservables observables;
	private final DaeWritables writables;
	private final Spectra spectra;
	
	private final ForwardingObservable<Boolean> isRunning;
	
    /**
     * Instantiates a new DAE model.
     *
     * @param writables
     *            the writables associated with the model
     * @param observables
     *            the observables associated with the model
     */
	public DaeModel(DaeWritables writables, DaeObservables observables) {		
		this.observables = observables;
		this.writables = writables;
		
		experimentSetup = registerForClose(new ExperimentSetup(observables, writables));
		actions = registerForClose(new DaeActions(writables, observables));
		spectra = new Spectra(observables);

		isRunning = registerForClose(new ForwardingObservable<>(new DaeIsRunning(observables.runState)));
	}

    @Override
	public ForwardingObservable<String> instrument() {
		return observables.instrumentName;
	}
	
	@Override
	public ForwardingObservable<String> runNumber() {
		return observables.runNumber;
	}

	@Override
	public ForwardingObservable<DaeRunState> runState() {
		return observables.runState;
	}

	@Override
	public ForwardingObservable<Boolean> isRunning() {
		return isRunning;
	}
	
	@Override
	public ForwardingObservable<String> isisCycle() {
		return observables.isisCycle;
	}

	@Override
	public ForwardingObservable<String> title() {
		return observables.title;
	}

    @Override
    public ForwardingObservable<Boolean> displayTitle() {
        return observables.displayTitle;
    }

	@Override
	public Writable<String> setTitle() {
		return writables.title;
	}

    @Override
    public Writable<Long> setDisplayTitle() {
        return writables.displayTitle;
    }

	@Override
	public ForwardingObservable<String> vetos() {
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

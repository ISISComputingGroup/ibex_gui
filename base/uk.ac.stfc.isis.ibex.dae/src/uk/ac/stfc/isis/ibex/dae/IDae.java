
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
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * The Interface providing Dae observable, writables and methods.
 */
public interface IDae {

    /**
     * @return An observable on a string with the name of the instrument.
     */
	ForwardingObservable<String> instrument();
	
    /**
     * @return An observable on a string with the run number.
     */
	ForwardingObservable<String> runNumber();

    /**
     * @return An observable on a string with the status of the run.
     */
	ForwardingObservable<DaeRunState> runState();

    /**
     * @return an observable on whether the DAE is running
     */
	ForwardingObservable<Boolean> isRunning();
	
    /**
     * 
     * @return the isis cycle number observable
     */
	ForwardingObservable<String> isisCycle();

    /**
     * @return An observable on a string giving the title for the current run.
     */
	ForwardingObservable<String> title();

    /**
     * @return An observable on a boolean giving whether or not to display the
     *         title on the webpage.
     */
    ForwardingObservable<Boolean> displayTitle();

    /**
     * @return the writable for setting the run title
     */
	Writable<String> setTitle();

    /**
     * @return An writable to set whether or not to display the title on the
     *         webpage.
     */
    Writable<Long> setDisplayTitle();
		
    /**
     * 
     * @return observable for the vetos
     */
	ForwardingObservable<String> vetos();
		
    /**
     * 
     * @return experimental setup model
     */
	ExperimentSetup experimentSetup();

    /**
     * 
     * @return spectra model
     */
	Spectra spectra();
		
    /**
     * @return the DAE actions
     */
	DaeActions actions();

	/**
     * @return An observable on a boolean giving whether or not the DAE is in simulation mode.
     */
    ForwardingObservable<Boolean> simulationMode();

}

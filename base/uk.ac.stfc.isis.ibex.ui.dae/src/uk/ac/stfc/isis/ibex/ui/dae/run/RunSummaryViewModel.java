
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

package uk.ac.stfc.isis.ibex.ui.dae.run;

import uk.ac.stfc.isis.ibex.activemq.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.dae.IDae;
import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.BooleanWritableObservableAdapter;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.StringWritableObservableAdapter;

public class RunSummaryViewModel extends Closer {
	
	private IDae model;
	
	private UpdatedValue<String> instrument;
	private UpdatedValue<String> runStatus;
	private UpdatedValue<String> runNumber;
	private UpdatedValue<String> isisCycle;	
    private BooleanWritableObservableAdapter displayTitle;
    private StringWritableObservableAdapter title;
				
	private ILogMessageProducer logModel;

    /**
     * Binds Observables/Writables to model.
     * 
     * @param model the RunSummaryViewModel
     */
	public void bind(final IDae model) {
		this.model = model;
		
		instrument = registerForClose(new TextUpdatedObservableAdapter(model.instrument()));
		runStatus = registerForClose(new TextUpdatedObservableAdapter(registerForClose(new InstrumentState(model.runState()))));
		runNumber = registerForClose(new TextUpdatedObservableAdapter(model.runNumber()));
		isisCycle = registerForClose(new TextUpdatedObservableAdapter(model.isisCycle()));
        title = registerForClose(new StringWritableObservableAdapter(model.setTitle(), model.title()));
        displayTitle =
                registerForClose(new BooleanWritableObservableAdapter(model.setDisplayTitle(), model.displayTitle()));

		logModel = Log.getInstance().producer();
	}

    /**
     * @return an observable of the instrument name
     */
	public UpdatedValue<String> instrument() {
		return instrument;
	}

    /**
     * @return an observable of the run status
     */
	public UpdatedValue<String> runStatus() {
		return runStatus;
	}

    /**
     * @return an observable of the run number
     */
	public UpdatedValue<String> runNumber() {
		return runNumber;
	}

    /**
     * @return an observable of the isis cycle
     */
	public UpdatedValue<String> isisCycle() {
		return isisCycle;
	}

    /**
     * Returns an object linking the observable and writable connected to the
     * display title PV (specifies whether run title is visible on dataweb
     * dashboard page).
     * 
     * @return the object linking the observable and writable object
     */
    public BooleanWritableObservableAdapter displayTitle() {
        return displayTitle;
    }

    /**
     * Returns an object linking the observable and writable connected to the PV
     * containing the run title.
     * 
     * @return the object linking the observable and writable object
     */
	public StringWritableObservableAdapter title() {
		return title;
	}
	
	public DaeActions actions() {
		return model.actions();
	}
	
	public ILogMessageProducer logMessageSource() {
		return logModel;
	}
}

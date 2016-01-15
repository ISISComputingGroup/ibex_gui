
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

package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class TimePanelModel extends Closer {

	private final UpdatedValue<String> instrumentTime;
	private final UpdatedValue<String> runTime;
	private final UpdatedValue<String> period;

	public TimePanelModel(DashboardObservables observables) {
		instrumentTime = registerForClose(new TextUpdatedObservableAdapter(observables.instrumentTime));
		runTime = registerForClose(new TextUpdatedObservableAdapter(observables.dae.runTime));
		period = createPeriod(observables);
	}
	
	public UpdatedValue<String> instrumentTime() {
		return instrumentTime;
	}
	
	public UpdatedValue<String> runTime() {
		return runTime;
	}
	
	public UpdatedValue<String> period() {
		return period;
	}
	
	private UpdatedValue<String> createPeriod(DashboardObservables observables) {
		return registerForClose(
				new TextUpdatedObservableAdapter(
						new ForwardingObservable<String>(
								new ObservableSimpleRatio<>(
										new ObservablePair<>(observables.dae.currentPeriod, observables.dae.totalPeriods)))));
	}
}

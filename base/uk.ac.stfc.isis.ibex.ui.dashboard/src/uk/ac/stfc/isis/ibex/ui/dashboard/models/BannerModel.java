
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

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class BannerModel extends Closer {
		
	private final UpdatedValue<String> instrumentName;
	private final InstrumentState instrumentState;
	private final BannerText bannerText;
	private final UpdatedValue<String> runNumber;
	private final ShutterState shutterState;
	
	public BannerModel(DashboardObservables observables) {
		instrumentName = Instrument.getInstance().name();
		instrumentState = registerForClose(new InstrumentState(observables.dae.runState));
		bannerText = new BannerText(instrumentName, instrumentState.text());
		
		runNumber = registerForClose(new TextUpdatedObservableAdapter(observables.dae.runNumber));
		shutterState = registerForClose(new ShutterState(observables.shutter));
	}
	
	public UpdatedValue<String> bannerText() {
		return bannerText;
	}
	
	public UpdatedValue<String> instrumentName() {
		return instrumentName;
	}
	
	public UpdatedValue<String> runState() {
		return instrumentState.text();
	}
	
	public UpdatedValue<Color> background() {
		return instrumentState.color();
	}
	
	public UpdatedValue<String> runNumber() {
		return runNumber;
	}
	
	public UpdatedValue<String> shutter() {
		return shutterState.text();
	}
}

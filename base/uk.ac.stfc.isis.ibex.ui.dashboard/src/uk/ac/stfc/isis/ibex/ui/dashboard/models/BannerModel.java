
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

import java.util.EnumMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.dashboard.DashboardPv;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Model for the banner within the dashboard view.
 */
public class BannerModel extends Closer {
		
	private final UpdatedValue<String> instrumentName;
	private final InstrumentState instrumentState;
	private final BannerText bannerText;
	
	private final Map<DashboardPv, TextUpdatedObservableAdapter> labelAdapters = new EnumMap<>(DashboardPv.class);
	private final Map<DashboardPv, TextUpdatedObservableAdapter> valueAdapters = new EnumMap<>(DashboardPv.class);
	
	/**
	 * Constructor for the banner model.
	 * @param observables The observables for values displayed on the dashboard banner
	 */
	public BannerModel(DashboardObservables observables) {
		instrumentName = Instrument.getInstance().name();
		instrumentState = registerForClose(new InstrumentState(observables.runState));
		bannerText = new BannerText(instrumentName, instrumentState.text());
		
		for (DashboardPv pv : DashboardPv.values()) {
			labelAdapters.put(pv, registerForClose(new TextUpdatedObservableAdapter(observables.getLabelObservable(pv))));
			valueAdapters.put(pv, registerForClose(new TextUpdatedObservableAdapter(observables.getValueObservable(pv))));
		}
	}
	
	/**
	 * The text of the banner (e.g. IRIS is RUNNING).
	 * @return an updated value wrapping the text.
	 */
	public UpdatedValue<String> bannerText() {
		return bannerText;
	}
	
	/**
	 * The instrument name as a string (e.g. IRIS, LARMOR).
	 * @return an updated value wrapping the instrument name string.
	 */
	public UpdatedValue<String> instrumentName() {
		return instrumentName;
	}
	
	/**
	 * The run state as a string (e.g. RUNNING, SETUP).
	 * @return an updated value wrapping the run state string.
	 */
	public UpdatedValue<String> runState() {
		return instrumentState.text();
	}
	
	/**
	 * The colour of the dashboard.
	 * @return an updated value wrapping the dashboard colour.
	 */
	public UpdatedValue<Color> background() {
		return instrumentState.color();
	}
	
	/**
	 * Gets the label of a specified dashboard field, as a string.
	 * @return an updated value wrapping the label
	 */
	public UpdatedValue<String> dashboardLabel(DashboardPv pv) {
		return labelAdapters.get(pv);
	}
	
	/**
	 * Gets the value of a specified dashboard field, as a string.
	 * @return an updated value wrapping the value
	 */
	public UpdatedValue<String> dashboardValue(DashboardPv pv) {
		return valueAdapters.get(pv);
	}
}

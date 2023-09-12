
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

package uk.ac.stfc.isis.ibex.dashboard;

import java.util.EnumMap;
import java.util.Map;
import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

/**
 * Holds the Observables for the non-DAE part of the dashboard and holds a
 * reference to the class containing the DAE Observables.
 */
public class DashboardObservables extends Closer {

    private static final String USERS = "ED:SURNAME";
    private static final String DISPLAY_TITLE = "DAE:TITLE:DISPLAY";

    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    private final Map<DashboardPv, SwitchableObservable<String>> valueObservables = new EnumMap<>(DashboardPv.class);
    private final Map<DashboardPv, SwitchableObservable<String>> labelObservables = new EnumMap<>(DashboardPv.class);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
    
    /**
     * An observable for the list of users to be displayed on the dashboard.
     */
    public final ForwardingObservable<String> users;
    
    /**
     * An observable for the title on the dashboard.
     */
    public final ForwardingObservable<String> title;
    
    /**
     * An observable for the flag which denotes whether the title is displayed on the dashboard.
     */
    public final ForwardingObservable<Boolean> displayTitle;
    
    /**
     * An observable for the flag which denotes whether the title is displayed on the dashboard.
     */
    public final Writable<Long> displayTitleSetter;
    
    /**
     * An observable for the run state on the dashboard.
     */
    public final ForwardingObservable<DaeRunState> runState;

    private SwitchableObservable<String> getObservable(final String pv) {
	return registerForClose(obsFactory.getSwitchableObservable(new StringChannel(), pv));
    }

    /**
     * Constructor which initialises the observables.
     */
    public DashboardObservables() {
	users = registerForClose(obsFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
		InstrumentUtils.addPrefix(USERS)));
	
    displayTitleSetter = writeFactory.getSwitchableWritable(new LongChannel(),
    		InstrumentUtils.addPrefix(DISPLAY_TITLE));
	
    displayTitle = obsFactory.getSwitchableObservable(new BooleanChannel(),
            InstrumentUtils.addPrefix(DISPLAY_TITLE));
	final DaeObservables dae = new DaeObservables();
	title = registerForClose(dae.title);
	runState = registerForClose(dae.runState);

	for (DashboardPv dashboardPv : DashboardPv.values()) {
	    valueObservables.put(dashboardPv, getObservable(dashboardPv.getValuePV()));
	    labelObservables.put(dashboardPv, getObservable(dashboardPv.getLabelPV()));
	}
    }

    /**
     * Return an observable observing the label of the provided DashboardPv.
     * @param pv the dashboard pv to observe
     * @return the observable
     */
    public SwitchableObservable<String> getLabelObservable(DashboardPv pv) {
	return labelObservables.get(pv);
    }

    /**
     * Return an observable observing the value of the provided DashboardPv.
     * @param pv the dashboard pv to observe
     * @return the observable
     */
    public SwitchableObservable<String> getValueObservable(DashboardPv pv) {
	return valueObservables.get(pv);
    }
}

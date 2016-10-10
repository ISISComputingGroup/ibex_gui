
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
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * The model for the monitor panel in the dashboard.
 */
public class MonitorPanelModel extends Closer {

    private final UpdatedValue<String> goodOverRawFrames;
    private final UpdatedValue<String> currentOverTotal;
    private final UpdatedValue<String> monitorCounts;
    private static final int MAX_CURR_INT_DIGITS = 4;
    private static final int MAX_CURR_FRAC_DIGITS = 2;

    /**
     * The constructor.
     * 
     * @param observables the observables
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public MonitorPanelModel(DashboardObservables observables) {
        goodOverRawFrames = createGoodOverRawFrames(observables);
        currentOverTotal = createCurrentOverTotal(observables);
        monitorCounts = createMonitorCounts(observables);
    }

    /**
     * @return good / raw frames
     */
    public UpdatedValue<String> goodOverRawFrames() {
        return goodOverRawFrames;
    }

    /**
     * @return monitor counts
     */
    public UpdatedValue<String> monitorCounts() {
        return monitorCounts;
    }

    /**
     * @return current / total
     */
    public UpdatedValue<String> currentOverTotal() {
        return currentOverTotal;
    }

    private UpdatedValue<String> createGoodOverRawFrames(DashboardObservables observables) {
        ClosableObservable<Pair<Integer, Integer>> pair = registerForClose(
                new ObservablePair<>(observables.dae.goodFrames, observables.dae.rawFrames));
        ForwardingObservable<String> ratio = new ForwardingObservable<String>(
                new ObservableSimpleRatio<>(pair));

        return registerForClose(new TextUpdatedObservableAdapter(ratio));
    }

    private UpdatedValue<String> createCurrentOverTotal(DashboardObservables observables) {
        ClosableObservable<Pair<Number, Number>> pair = registerForClose(
                new ObservablePair<>(observables.dae.beamCurrent, observables.dae.goodCurrent));
        ForwardingObservable<String> ratio = new ForwardingObservable<String>(
                new ObservableDecimalRatio(pair, MAX_CURR_INT_DIGITS, MAX_CURR_FRAC_DIGITS));

        return registerForClose(new TextUpdatedObservableAdapter(ratio));
    }

    private UpdatedValue<String> createMonitorCounts(DashboardObservables observables) {
        ConvertingObservable<Integer, String> countsAsString = new ConvertingObservable<>(observables.dae.monitorCounts,
                new Converter<Integer, String>() {
                    @Override
                    public String convert(Integer value) throws ConversionException {
                        return value.toString();
                    }
                });

        return registerForClose(
                new TextUpdatedObservableAdapter(new ForwardingObservable<>(countsAsString)));
    }
}

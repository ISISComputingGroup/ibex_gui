
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
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MonitorPanelModel extends Closer {

    private final UpdatedValue<String> goodOverRawFrames;
    private final UpdatedValue<String> currentOverTotal;
    private final UpdatedValue<String> monitorCounts;

    public MonitorPanelModel(DashboardObservables observables) {
        goodOverRawFrames = createGoodOverRawFrames(observables);
        currentOverTotal = createCurrentOverTotal(observables);
        monitorCounts = createMonitorCounts(observables);
    }

    public UpdatedValue<String> goodOverRawFrames() {
        return goodOverRawFrames;
    }

    public UpdatedValue<String> monitorCounts() {
        return monitorCounts;
    }

    public UpdatedValue<String> currentOverTotal() {
        return currentOverTotal;
    }

    private UpdatedValue<String> createGoodOverRawFrames(DashboardObservables observables) {
        ClosableObservable<Pair<Integer, Integer>> pair = registerForClose(
                new ObservablePair<>(observables.dae.goodFrames, observables.dae.rawFrames));
        InitialiseOnSubscribeObservable<String> ratio = new InitialiseOnSubscribeObservable<String>(
                new ObservableSimpleRatio<>(pair));

        return registerForClose(new TextUpdatedObservableAdapter(ratio));
    }

    private UpdatedValue<String> createCurrentOverTotal(DashboardObservables observables) {
        ClosableObservable<Pair<Number, Number>> pair = registerForClose(
                new ObservablePair<>(observables.dae.beamCurrent, observables.dae.goodCurrent));
        InitialiseOnSubscribeObservable<String> ratio = new InitialiseOnSubscribeObservable<String>(
                new ObservableDecimalRatio(pair));

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
                new TextUpdatedObservableAdapter(new InitialiseOnSubscribeObservable<>(countsAsString)));
    }
}

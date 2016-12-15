 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.beamstatus.internal.Observables;
import uk.ac.stfc.isis.ibex.beamstatus.internal.OnOff;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.DateTimeChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * The observables for a generic ISIS end station.
 */
public abstract class EndStationObservables extends Observables {
    private final UpdatedValue<String> beam;
    private final UpdatedValue<String> pps;
    private final UpdatedValue<String> beamCurrent;
    private final UpdatedValue<String> uAHToday;
    private final UpdatedValue<String> lastBeamOff;
    private final UpdatedValue<String> lastBeamOn;

    /**
     * Constructor to register the observables for an ISIS end station.
     * 
     * @param prefix
     *            The prefix for that specific station.
     */
    protected EndStationObservables(PVAddress prefix) {

        beam = adaptEnum(
                obsFactory.getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), prefix.endWith("BEAM:STAT")));
        pps = adaptNumber(obsFactory.getSwitchableObservable(new NumberChannel(), prefix.endWith(FREQ)));
        beamCurrent = adaptNumber(
                obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), prefix.endWith(BEAM_CURRENT)));
        uAHToday = adaptNumber(
                obsFactory.getSwitchableObservable(new NumberWithPrecisionChannel(), prefix.endWith("BEAM:TOTAL")));
        lastBeamOff = adapt(obsFactory.getSwitchableObservable(new DateTimeChannel(), prefix.endWith("BEAMOFF:TIME")));
        lastBeamOn = adapt(obsFactory.getSwitchableObservable(new DateTimeChannel(), prefix.endWith("BEAMON:TIME")));
    }

    public UpdatedValue<String> beam() {
        return beam;
    }

    public UpdatedValue<String> pps() {
        return pps;
    }

    public UpdatedValue<String> beamCurrent() {
        return beamCurrent;
    }

    public UpdatedValue<String> uAHToday() {
        return uAHToday;
    }

    public UpdatedValue<String> lastBeamOff() {
        return lastBeamOff;
    }

    public UpdatedValue<String> lastBeamOn() {
        return lastBeamOn;
    }
}

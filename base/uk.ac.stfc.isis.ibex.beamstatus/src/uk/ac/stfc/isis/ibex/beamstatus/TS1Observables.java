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

import uk.ac.stfc.isis.ibex.beamstatus.internal.OnOff;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithPrecisionChannel;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Class that contains all the observables relating to TS1.
 */
public class TS1Observables extends EndStationObservables {
    /**
     * An updating value giving the temperature of the TS1 methane moderator.
     */
    public final UpdatedValue<String> methaneTemperature;

    /**
     * An updating value giving the temperature of the TS1 hydrogen moderator.
     */
    public final UpdatedValue<String> hydrogenTemperature;

    /**
     * An updating value giving the status of the muon kicker in TS1.
     */
    public final UpdatedValue<String> muonKicker;
    
    private static final String MOD_PREFIX = "TG:TS1:MOD:";

    /**
     * Constructor for the TS1 observables, registers values to PVs.
     */
    public TS1Observables() {
        super(PVAddress.startWith("AC").append("TS1"));
        
        ForwardingObservable<Number> methaneTempObs = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "METH:TEMP");
        methaneTemperature = adaptNumber(methaneTempObs);

        ForwardingObservable<Number> hydrogenTempObs = obsFactory
                .getSwitchableObservable(new NumberWithPrecisionChannel(), MOD_PREFIX + "HDGN:TEMP");
        hydrogenTemperature = adaptNumber(hydrogenTempObs);

        ForwardingObservable<OnOff> muonKickerObs = obsFactory
                .getSwitchableObservable(new EnumChannel<OnOff>(OnOff.class), "AC:MUON:KICKR:STAT");
        muonKicker = adaptEnum(muonKickerObs);
    }

}

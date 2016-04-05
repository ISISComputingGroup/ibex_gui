
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
package uk.ac.stfc.isis.ibex.instrument.list;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;

/**
 * Holds the connection to the Instrument List PV.
 * 
 */
public class InstrumentListObservable {
    
    private static final int MAX_RETRIES = 200;
    private static final int WAIT_FOR_OBSERVABLE = 100; // milliseconds

    private static final String ADDRESS = "NDW1298:IEW83206:CS:BLOCKSERVER:INSTRUMENT_NAMES";
    private final ForwardingObservable<Collection<InstrumentInfo>> instrumentsRBV;

    public InstrumentListObservable() {
        instrumentsRBV = convert(readCompressed(ADDRESS));
    }

    public Collection<InstrumentInfo> getInstruments() {
        return getValidInstruments();
    }

    public void close() {
        instrumentsRBV.close();
    }

    private ForwardingObservable<String> readCompressed(String address) {
        ClosableObservable<String> pvObservable = new CompressedCharWaveformChannel().reader(address);
        return new ForwardingObservable<>(pvObservable);
    }

    private ForwardingObservable<Collection<InstrumentInfo>> convert(ForwardingObservable<String> source) {
        Converter<String, Collection<InstrumentInfo>> converter = new JsonDeserialisingConverter<>(
                InstrumentInfo[].class).apply(Convert.<InstrumentInfo> toCollection());
        return new ForwardingObservable<>(new ConvertingObservable<>(source, converter));
    }

    private Collection<InstrumentInfo> getValidInstruments() {
        // Wait for the PV to be connected
        int i = 0;
        while (!instrumentsRBV.isConnected() && i < MAX_RETRIES) {
            try {
                Thread.sleep(WAIT_FOR_OBSERVABLE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        
        return InstrumentListUtils.filterValidInstruments(instrumentsRBV);
    }
}


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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
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
public class InstrumentListObservable extends ClosableObservable<Collection<InstrumentInfo>> {
    
    private static final int MAX_RETRIES = 200;
    private static final int WAIT_FOR_OBSERVABLE = 100; // milliseconds

    private BaseObserver<Collection<InstrumentInfo>> instrumentObserver =
            new BaseObserver<Collection<InstrumentInfo>>() {

                @Override
                public void onValue(Collection<InstrumentInfo> value) {
                    InstrumentListObservable.this.setValue(getValidInstruments(value));
                }

                @Override
                public void onError(Exception e) {
                    //
                }

                @Override
                public void onConnectionStatus(boolean isConnected) {
                    //
                }

            };

    /**
     * This is the PV that holds the list of instruments that can be selected.
     */
    private static final String ADDRESS = "CS:INSTLIST";

    private final ForwardingObservable<Collection<InstrumentInfo>> instrumentsRBV;
    private Logger logger;

    /**
     * Instantiate a new observable for the instrument list PV.
     * 
     * @param logger a logger to log information about reading and parsing the
     *            PV
     */
    public InstrumentListObservable(Logger logger) {
        this.logger = logger;

        instrumentsRBV = convert(readCompressed(ADDRESS));
        this.setValue(new ArrayList<InstrumentInfo>());
        instrumentsRBV.addObserver(instrumentObserver);

        // Wait for the PV to be connected
        int i = 0;
        while (!instrumentsRBV.isConnected() && instrumentsRBV.getValue() == null && i < MAX_RETRIES) {
            try {
                Thread.sleep(WAIT_FOR_OBSERVABLE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    @Override
    public void close() {
        instrumentsRBV.close();
    }

    private ForwardingObservable<String> readCompressed(String address) {
        ClosableObservable<String> pvObservable = new CompressedCharWaveformChannel().reader(address);
        return new ForwardingObservable<>(pvObservable);
    }

    private ForwardingObservable<Collection<InstrumentInfo>> convert(ForwardingObservable<String> source) {
        Converter<String, Collection<InstrumentInfo>> converter = new JsonDeserialisingConverter<>(
                InstrumentInfo[].class).apply(Convert.<InstrumentInfo>toCollection());
        return new ForwardingObservable<>(new ConvertingObservable<>(source, converter));
    }

    private Collection<InstrumentInfo> getValidInstruments(Collection<InstrumentInfo> instruments) {
        return InstrumentListUtils.filterValidInstruments(instruments, logger);
    }
}

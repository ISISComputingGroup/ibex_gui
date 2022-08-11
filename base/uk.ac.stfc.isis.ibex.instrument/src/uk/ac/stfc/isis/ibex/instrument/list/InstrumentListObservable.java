
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.instrument.list;

import java.util.Arrays;
import java.util.Collection;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;

/**
 * Holds the connection to the Instrument List PV.
 * 
 */
public class InstrumentListObservable extends ForwardingObservable<Collection<InstrumentInfo>> {

    /**
     * Reads the instrument list PV data. Note that we keep it as a variable to
     * prevent garbage collection of the PVReader. If we don't keep a reference
     * it gets GCed, potentially before the list has been read and we'll never
     * get the values.
     */
    private static ClosableObservable<String> PV_READER = null;

    /**
     * Logs messages regarding the instrument list.
     */
    private final Logger logger;

    /**
     * Instantiate a new observable for the instrument list PV.
     * 
     * @param logger a logger to log information about reading and parsing the
     *            PV
     */
    public InstrumentListObservable(Logger logger) {
        super(convert(readCompressed()));
        this.logger = logger;
    }

    @Override
    public void setConnectionStatus(boolean isConnected) {
        super.setConnectionStatus(isConnected);
    }

    @Override
    public void setValue(Collection<InstrumentInfo> value) {
        super.setValue(getValidInstruments(value));
    }

    /**
     * @return A forwarding observable of the instrument list PV reader.
     */
    private static synchronized ForwardingObservable<String> readCompressed() {
        if (PV_READER == null) {
            String instPV;
            // on IDAaaS linux look for local definition of correct INSTLIST to use
            try(BufferedReader instReader = new BufferedReader(new FileReader("/usr/local/ibex/etc/instpv.txt"))) {
                instPV = instReader.readLine();
            }
            catch(IOException e)
            {
                instPV = "CS:INSTLIST";
            }
            PV_READER = new CompressedCharWaveformChannel().reader(instPV);
        }
        return new ForwardingObservable<String>(PV_READER);
    }

    /**
     * @param source A forwarding observable for the instrument list as a JSON
     *            string
     * @return A forwarding observable for a collection of InstrumentInfo
     *         containing the list of instruments
     */
    private static ForwardingObservable<Collection<InstrumentInfo>> convert(ForwardingObservable<String> source) {
        Function<String, Collection<InstrumentInfo>> converter =
                new JsonDeserialisingConverter<>(InstrumentInfo[].class).andThen(Arrays::asList);
        return new ForwardingObservable<>(new ConvertingObservable<>(source, converter));
    }

    /**
     * @param instruments A list of instruments
     * @return The original list with any invalid elements filtered
     */
    private Collection<InstrumentInfo> getValidInstruments(Collection<InstrumentInfo> instruments) {
        return InstrumentListUtils.filterValidInstruments(instruments, logger);
    }
}

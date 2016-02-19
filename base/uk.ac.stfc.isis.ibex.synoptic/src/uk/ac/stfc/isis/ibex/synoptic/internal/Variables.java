
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.ConvertingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Holds all the PVs relating to the synoptic information.
 *
 */
public class Variables extends InstrumentVariables {
    private final WritableFactory closingWritableFactory;
    private final WritableFactory switchingWritableFactory;
    private final ObservableFactory closingObservableFactory;
    private final ObservableFactory switchingObservableFactory;

    private final String pvPrefix;

	private static final String SYNOPTIC_ADDRESS = "CS:BLOCKSERVER:SYNOPTICS:";
	private static final String GET_SYNOPTIC = ":GET";
	
    /**
     * The name associated with the "blank" synoptic from the BlockServer.
     */
    public static final String NONE_SYNOPTIC_NAME = "-- NONE --";
    public static final String NONE_SYNOPTIC_PV = "__BLANK__";

    public final Writable<String> setSynoptic;

    public final Writable<Collection<String>> deleteSynoptics;

    public final ForwardingObservable<Collection<SynopticInfo>> available;

    /**
     * An observable for the schema for the synoptics.
     */
    public ForwardingObservable<String> synopticSchema;

    /**
     * Default constructor.
     */
    public Variables() {
        this(new WritableFactory(OnInstrumentSwitch.CLOSE), new WritableFactory(OnInstrumentSwitch.SWITCH),
                new ObservableFactory(OnInstrumentSwitch.CLOSE), new ObservableFactory(OnInstrumentSwitch.SWITCH),
                null);
    }

    /**
     * Constructor used for testing which avoids making any .getInstance()
     * calls.
     * 
     * @param writableFactory
     *            A writable factory to be used, could be a mock.
     * @param closingObservableFactory
     *            An observable factory to be used, could be a mock.
     * @param instrumentSwitchers
     *            An instrument switcher instance to use. Could be a mock for
     *            testing.
     * @param pvPrefix
     *            A PV Prefix to be used in place of
     *            Instrument.getInstance().getPvPrefix().
     */
    public Variables(WritableFactory closingWritableFactory, WritableFactory switchingWritableFactory,
            ObservableFactory closingObservableFactory, ObservableFactory switchingObservableFactory, String pvPrefix) {
        this.closingWritableFactory = closingWritableFactory;
        this.switchingWritableFactory = switchingWritableFactory;
        this.closingObservableFactory = closingObservableFactory;
        this.switchingObservableFactory = switchingObservableFactory;
        
        this.pvPrefix = pvPrefix;

        setSynoptic = writeCompressed(SYNOPTIC_ADDRESS + "SET_DETAILS");
        deleteSynoptics = convert(writeCompressed(SYNOPTIC_ADDRESS + "DELETE"), namesToString());
        available = convert(readCompressed(SYNOPTIC_ADDRESS + "NAMES"), toSynopticInfo());
        synopticSchema = readCompressed(SYNOPTIC_ADDRESS + "SCHEMA");
    }

	public Converter<String, Collection<SynopticInfo>> toSynopticInfo() {
		return new JsonDeserialisingConverter<>(SynopticInfo[].class).apply(Convert.<SynopticInfo>toCollection());
	}	
	
	public ForwardingObservable<SynopticDescription> getSynopticDescription(String synopticPV) {		
		return convert(readCompressedClosing(getFullPV(synopticPV)), new InstrumentDescriptionParser());
	}
	
	private String getFullPV(String synopticPV) {
		return SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC;
	}	
	
	private Converter<Collection<String>, String> namesToString() {
		return Convert.toArray(new String[0]).apply(new JsonSerialisingConverter<String[]>(String[].class));
	}
	
	private Writable<String> writeCompressed(String address) {
        return switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
	}
	
	private <T> Writable<T> convert(Writable<String> destination, Converter<T, String> converter) {
		return new ConvertingWritable<>(destination, converter);
	}	
	
	// Some of the synoptic PVs are common to all instruments so should be switched
	private ForwardingObservable<String> readCompressed(String address) {
        return switchingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
	}
	
	private ForwardingObservable<String> readCompressedClosing(String address) {
        return closingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
	}	
	
	// The following readers/writers are for PVs on the synoptic

    public ForwardingObservable<String> defaultReaderRemote(String address) {
        // Synoptic variables are always remote
        return closingObservableFactory.getSwitchableObservable(new DefaultChannel(), address);
    }

	public ForwardingObservable<String> defaultReaderWithoutUnits(String address) {
        return closingObservableFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(),
                getPvPrefix() + address);
	}
	
    public Writable<String> defaultWritableRemote(String address) {
        // If it is local append the PV prefix, otherwise don't
        return closingWritableFactory.getSwitchableWritable(new StringChannel(), address);
	}
	
    private String getPvPrefix() {
        if (pvPrefix == null) {
            return Instrument.getInstance().getPvPrefix();
        } else {
            return pvPrefix;
        }
    }

}

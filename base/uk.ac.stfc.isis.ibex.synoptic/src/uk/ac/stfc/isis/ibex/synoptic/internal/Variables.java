
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ConcatenatingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannelWithoutUnits;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Holds all the PVs relating to the synoptic information.
 *
 */
public class Variables {
    private final WritableFactory closingWritableFactory;
    private final WritableFactory switchingWritableFactory;
    private final ObservableFactory closingObservableFactory;
    private final ObservableFactory switchingObservableFactory;

    private final String pvPrefix;

    private static final String SYNOPTIC_ADDRESS = "CS:SYNOPTICS:";
	private static final String GET_SYNOPTIC = ":GET";
	private static final String ALARM_FIELD = ".SEVR";
	
    /**
     * The name associated with the "blank" synoptic from the BlockServer.
     */
    public static final String NONE_SYNOPTIC_NAME = ConfigEditing.NONE_SYNOPTIC_NAME;

    /**
     * The pv associated with the "blank" synoptic from the BlockServer.
     */
    public static final String NONE_SYNOPTIC_PV = ConfigEditing.NONE_SYNOPTIC_PV;

    /**
     * Facilitates setting the current synoptic.
     */
    public final Writable<String> synopticSetter;

    /**
     * Facilitates deleting synoptics.
     */
    public final Writable<Collection<String>> synopticsDeleter;

    /**
     * A list of information about the currently available synoptics.
     */
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
     * @param closingWritableFactory
     *            A closing writable factory to be used, could be a mock.
     * @param switchingWritableFactory
     *            A switching writable factory to be used, could be a mock.
     * @param closingObservableFactory
     *            A closing observable factory to be used, could be a mock.
     * @param switchingObservableFactory
     *            A switching observable factory to be used, could be a mock.
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

        synopticSetter = writeCompressed(SYNOPTIC_ADDRESS + "SET_DETAILS");
        synopticsDeleter = InstrumentUtils.convert(writeCompressed(SYNOPTIC_ADDRESS + "DELETE"), namesToString());
        available = InstrumentUtils.convert(readCompressed(SYNOPTIC_ADDRESS + "NAMES"), toSynopticInfo());
        synopticSchema = readCompressed(SYNOPTIC_ADDRESS + "SCHEMA");
    }

    private Function<String, Collection<SynopticInfo>> toSynopticInfo() {
		return new JsonDeserialisingConverter<>(SynopticInfo[].class).andThen(Arrays::asList);
	}	

    /**
     * Parses all the information about a synoptic from its PV.
     * 
     * @param synopticPV synopticPV the PV for the synoptic
     * @return an object containing all the information about the synoptic
     */
    public ForwardingObservable<SynopticDescription> getSynopticDescription(String synopticPV) {
        return InstrumentUtils.convert(readCompressedClosing(getFullPV(synopticPV)),
                new SynopticParser());
	}
	
    // The following readers/writers are for PVs on the synoptic

    /**
     * Provides an observable for the PV corresponding to the input address.
     * 
     * @param address the PV address
     * @return an observable to the input PV
     */
    public ForwardingObservable<String> defaultReaderRemote(String address) {
        var value = closingObservableFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(), address);
        var units = closingObservableFactory.getSwitchableObservable(new StringChannel(), address + ".EGU");
        return new ForwardingObservable<String>(new ConcatenatingObservable(value, units));
    }

    /**
     * Provides an observable for the PV corresponding to the input address,
     * using a unitless channel.
     * 
     * @param address the PV address
     * @return an observable to the input PV, using a unitless channel
     */
    public ForwardingObservable<String> defaultReaderRemoteWithoutUnits(String address) {
        return closingObservableFactory.getSwitchableObservable(new DefaultChannelWithoutUnits(), address);
    }
    
    /**
     * Provides an observable for the PV alarm state corresponding to the input address.
     * 
     * @param address the PV address
     * @return an observable to the input PV alarm state
     */
    public ForwardingObservable<AlarmState> defaultReaderRemoteAlarm(String address) {
        return closingObservableFactory.getSwitchableObservable(new EnumChannel<>(AlarmState.class), address + ALARM_FIELD);
    }

    /**
     * Provides a writable for the PV corresponding to the input address.
     * 
     * @param address the PV address
     * @return a writable for the specified PV
     */
    public Writable<String> defaultWritableRemote(String address) {
        return closingWritableFactory.getSwitchableWritable(new StringChannel(), address);
    }

    private String getFullPV(String synopticPV) {
        return SYNOPTIC_ADDRESS + synopticPV + GET_SYNOPTIC;
    }

    private Function<Collection<String>, String> namesToString() {
        return Convert.toArray(new String[0]).andThen(new JsonSerialisingConverter<String[]>(String[].class));
    }

    private Writable<String> writeCompressed(String address) {
        return switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
	}

    // Some of the synoptic PVs are common to all instruments so should be
    // switched
    private ForwardingObservable<String> readCompressed(String address) {
        return switchingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
    }

    private ForwardingObservable<String> readCompressedClosing(String address) {
        return closingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(),
                getPvPrefix() + address);
    }

    private String getPvPrefix() {
        if (pvPrefix == null) {
            return Instrument.getInstance().getPvPrefix();
        } else {
            return pvPrefix;
        }
    }
}


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
package uk.ac.stfc.isis.ibex.devicescreens;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreenDescriptionToXmlConverter;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionXmlParser;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;

/**
 * Contains key variables for the device screens class.
 */
public class DeviceScreenVariables {
    
    private static final String BLOCKSERVER_ADDRESS = "CS:BLOCKSERVER:";
    private static final String GET_SCREENS_SUFFIX = "GET_SCREENS";
    private static final String SET_SCREENS_SUFFIX = "SET_SCREENS";
    private static final String SCHEMA_SUFFIX = "SCREENS_SCHEMA";

    private final ObservableFactory switchingObservableFactory;
    private final WritableFactory switchingWritableFactory;

    private final Observable<DeviceScreensDescription> deviceScreensObservable;
    private final Writable<DeviceScreensDescription> deviceScreensWritable;
    private final ForwardingObservable<String> deviceScreenSchema;

    private final String pvPrefix;

    private LocalDeviceScreens localDeviceScreens = new LocalDeviceScreens();

    /**
     * Default constructor.
     */
    public DeviceScreenVariables() {
        this(new ObservableFactory(OnInstrumentSwitch.SWITCH), new WritableFactory(OnInstrumentSwitch.SWITCH), null);
    }


    /**
     * Constructor used for testing, so we can mock out the factories.
     *
     * @param switchingObservableFactory an observable factory to be used, could
     *            be a mock
     * @param switchingWritableFactory a writable factory to be used, could be a
     *            mock
     * @param pvPrefix the PV prefix - will use the one from Instrument if null
     */
    public DeviceScreenVariables(ObservableFactory switchingObservableFactory, WritableFactory switchingWritableFactory,
            String pvPrefix) {
        this.switchingObservableFactory = switchingObservableFactory;
        this.switchingWritableFactory = switchingWritableFactory;
        this.pvPrefix = pvPrefix;
        
        deviceScreensObservable =
                InstrumentUtils.convert(readCompressed(getPvPrefix() + BLOCKSERVER_ADDRESS + GET_SCREENS_SUFFIX),
                        new DeviceScreensDescriptionXmlParser());
        
        deviceScreensWritable =
                InstrumentUtils.convert(writeCompressed(getPvPrefix() + BLOCKSERVER_ADDRESS + SET_SCREENS_SUFFIX),
                new DeviceScreenDescriptionToXmlConverter());

        deviceScreenSchema = readCompressed(getPvPrefix() + BLOCKSERVER_ADDRESS + SCHEMA_SUFFIX);
    }

    /**
     * Gets the observable for the get device screens PV.
     * 
     * @return an observable pointing at the get device screens PV
     */
    public Observable<DeviceScreensDescription> getDeviceScreens() {
        return deviceScreensObservable;
    }

    /**
     * Gets the writable to the set device screens PV.
     * 
     * @return a writable pointing to the set device screens PV
     */
    public Writable<DeviceScreensDescription> getDeviceScreensSetter() {

        return deviceScreensWritable;
    }

    /**
     * Gets the observable for the PV containing the device screens schema.
     * 
     * @return an observable pointing at the device screens schema PV
     */
    public ForwardingObservable<String> getDeviceScreensSchema() {
        return deviceScreenSchema;
    }

    private ForwardingObservable<String> readCompressed(String address) {
        return switchingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), address);
    }

    private Writable<String> writeCompressed(String address) {
        return switchingWritableFactory.getSwitchableWritable(new CompressedCharWaveformChannel(), address);
    }

    /**
     * This method allows us to pass a pvPrefix in the constructor during the
     * tests, when the pvPrefix cannot come from Instrument.
     * 
     * @return the PV prefix
     */
    private String getPvPrefix() {
        if (pvPrefix == null) {
            return Instrument.getInstance().getPvPrefix();
        } else {
            return pvPrefix;
        }
    }

    public LocalDeviceScreens getLocalDeviceScreens() {
        return localDeviceScreens;
    }

}

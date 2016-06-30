
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

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionParser;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;

/**
 * Contains key variables for the device screens class.
 */
public class DeviceScreenVariables implements Closable {
    
    private static final String BLOCKSERVER_ADDRESS = "CS:BLOCKSERVER:";
    private static final String GET_SCREENS_SUFFIX = "GET_SCREENS";
    private static final String SET_SCREENS_SUFFIX = "SET_SCREENS";

    private final ObservableFactory switchingObservableFactory;
    private final WritableFactory switchingWritableFactory;

    private ForwardingObservable<DeviceScreensDescription> deviceScreensObservable;

    private final String pvPrefix;

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
     */
    public DeviceScreenVariables(ObservableFactory switchingObservableFactory, WritableFactory switchingWritableFactory,
            String pvPrefix) {
        this.switchingObservableFactory = switchingObservableFactory;
        this.switchingWritableFactory = switchingWritableFactory;
        this.pvPrefix = pvPrefix;
    }

    public ForwardingObservable<DeviceScreensDescription> getDeviceScreens() {
        close();

        deviceScreensObservable = InstrumentVariables.convert(
                readCompressedClosing(getPvPrefix() + BLOCKSERVER_ADDRESS + GET_SCREENS_SUFFIX),
                new DeviceScreensDescriptionParser());

        return deviceScreensObservable;
    }

    private ForwardingObservable<String> readCompressedClosing(String address) {
        return switchingObservableFactory.getSwitchableObservable(new CompressedCharWaveformChannel(), address);
    }

    @Override
    public void close() {
        if (deviceScreensObservable != null) {
            deviceScreensObservable.close();
        }
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

}

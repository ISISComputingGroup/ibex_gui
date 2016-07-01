
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
package uk.ac.stfc.isis.ibex.devicescreens.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreenVariables;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

/**
 * This class tests DeviceScreensVariables.
 */
@SuppressWarnings({ "checkstyle:methodname", "unchecked" })
public class DeviceScreenVariablesTest {

    private static final String pvPrefix = "PVPrefix";
    private static final String GET_SCREENS_SUFFIX = "CS:BLOCKSERVER:GET_SCREENS";

    private DeviceScreenVariables variables;
    private ObservableFactory switchingObservableFactory;
    private WritableFactory switchingWritableFactory;
    private SwitchableObservable mockSwitchableObservable;

    @Before
    public void set_up() {
        // Arrange
        SwitchableObservable defaultSwitchableObservable = mock(SwitchableObservable.class);
        mockSwitchableObservable = mock(SwitchableObservable.class);

        switchingObservableFactory = mock(ObservableFactory.class);
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class), anyString()))
                .thenReturn(defaultSwitchableObservable);

        Writable defaultWritable = mock(Writable.class);
        switchingWritableFactory = mock(WritableFactory.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class), anyString()))
                .thenReturn(defaultWritable);
        }

    private DeviceScreenVariables createVariables() {
        return new DeviceScreenVariables(switchingObservableFactory, switchingWritableFactory, pvPrefix);
    }

    @Test
    public void GIVEN_new_variables_WHEN_get_device_screens_called_THEN_result_is_parsed_from_get_screens_pv() {
        // Arrange
        String expectedName = "A name";
        when(mockSwitchableObservable.getValue()).thenReturn(getXMLText(expectedName));
        when(mockSwitchableObservable.currentError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(pvPrefix + GET_SCREENS_SUFFIX))).thenReturn(mockSwitchableObservable);
        
        variables = createVariables();

        // Act
        ForwardingObservable<DeviceScreensDescription> descriptionsObservable = variables.getDeviceScreens();

        // Assert
        List<DeviceDescription> devices = descriptionsObservable.getValue().getDevices();
        assertEquals(1, devices.size());
        assertEquals(expectedName, devices.get(0).getName());
    }

    private String getXMLText(String name) {
        return "<?xml version=\"1.0\" ?>" + "<devices xmlns=\"http://epics.isis.rl.ac.uk/schema/screens/1.0/\">"
                + "<device>" + "<name>" + name + "</name>" + "<key>Eurotherm</key>" + "<type>OPI</type>"
                        + "<properties>" + "<property>" + "<key>EURO</key>" + "<value>EUROTHERM1</value>"
                        + "</property>" + "</properties>" + "</device>" + "</devices>";

    }
}

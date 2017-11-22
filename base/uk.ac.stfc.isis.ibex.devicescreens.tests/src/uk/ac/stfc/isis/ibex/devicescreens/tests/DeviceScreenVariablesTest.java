
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

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreenVariables;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.devicescreens.tests.xmldata.DeviceScreensXmlProvider;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
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

    private static final String PV_PREFIX = "PVPrefix";
    private static final String BLOCKSERVER_ADDRESS = "CS:BLOCKSERVER:";
    private static final String GET_SCREENS_SUFFIX = "GET_SCREENS";
    private static final String SET_SCREENS_SUFFIX = "SET_SCREENS";
    private static final String SCHEMA_SUFFIX = "SCREENS_SCHEMA";

    private static String deviceName = "device name";
    private static String deviceKey = "device key";
    private static String deviceType = "OPI";
    private static String propertyKey = "property key";
    private static String propertyValue = "property value";

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
        return new DeviceScreenVariables(switchingObservableFactory, switchingWritableFactory, PV_PREFIX);
    }

    @Test
    public void GIVEN_new_variables_WHEN_get_device_screens_called_THEN_result_is_parsed_from_get_screens_pv() {
        // Arrange
        String expectedName = deviceName;

        when(mockSwitchableObservable.getValue())
                .thenReturn(DeviceScreensXmlProvider.getXML(expectedName, deviceKey, deviceType, propertyKey,
                        propertyValue));
        when(mockSwitchableObservable.currentError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);

        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(PV_PREFIX + BLOCKSERVER_ADDRESS + GET_SCREENS_SUFFIX))).thenReturn(mockSwitchableObservable);
        
        variables = createVariables();

        // Act
        Observable<DeviceScreensDescription> descriptionsObservable = variables.getDeviceScreens();

        // Assert
        List<DeviceDescription> devices = descriptionsObservable.getValue().getDevices();
        assertEquals(1, devices.size());
        assertEquals(expectedName, devices.get(0).getName());
    }

    @Test
    public void GIVEN_new_variables_WHEN_get_device_screens_schema_called_THEN_result_is_content_of_schema_pv() {
        // Arrange
        String expectedSchema = "This is a test schema";
        when(mockSwitchableObservable.getValue()).thenReturn(expectedSchema);
        when(mockSwitchableObservable.currentError()).thenReturn(null);
        when(mockSwitchableObservable.isConnected()).thenReturn(true);
        
        when(switchingObservableFactory.getSwitchableObservable(any(ChannelType.class),
                eq(PV_PREFIX + BLOCKSERVER_ADDRESS + SCHEMA_SUFFIX)))
                .thenReturn(mockSwitchableObservable);

        variables = createVariables();

        // Act
        ForwardingObservable<String> schemaObservable = variables.getDeviceScreensSchema();

        // Assert
        assertEquals(expectedSchema, schemaObservable.getValue());
    }

    @Test
    public void GIVEN_new_variable_WHEN_set_device_screens_THEN_PV_is_written_to() throws IOException {
        // Arrange
        Writable expectedDestination = mock(Writable.class);
        when(switchingWritableFactory.getSwitchableWritable(any(ChannelType.class),
                eq(PV_PREFIX + BLOCKSERVER_ADDRESS + SET_SCREENS_SUFFIX))).thenReturn(expectedDestination);

        DeviceScreensDescription inputValue =
                getDeviceScreensDescription(deviceName, deviceKey, propertyKey, propertyValue);
        String expectedConvertedValue =
                DeviceScreensXmlProvider.getXML(deviceName, deviceKey, deviceType, propertyKey, propertyValue);

        variables = createVariables();

        // Act
        Writable<DeviceScreensDescription> setter = variables.getDeviceScreensSetter();
        verify(expectedDestination, never()).write(any());
        setter.write(inputValue);

        // Assert
        verify(expectedDestination).write(expectedConvertedValue);
    }

    private DeviceScreensDescription getDeviceScreensDescription(String deviceName, String deviceKey,
            String propertyKey, String propertyValue) {
        DeviceScreensDescription deviceScreensDescription = new DeviceScreensDescription();

        DeviceDescription deviceDescription = new DeviceDescription();
        deviceDescription.setName(deviceName);
        deviceDescription.setType("OPI");
        deviceDescription.setKey(deviceKey);
        deviceDescription.addProperty(new PropertyDescription(propertyKey, propertyValue));

        deviceScreensDescription.addDevice(deviceDescription);

        return deviceScreensDescription;
    }

}

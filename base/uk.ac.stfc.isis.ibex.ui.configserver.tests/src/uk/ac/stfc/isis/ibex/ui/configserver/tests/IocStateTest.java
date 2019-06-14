
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

package uk.ac.stfc.isis.ibex.ui.configserver.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlockLogSettingsViewModel;

@SuppressWarnings("checkstyle:methodname")
public class IocStateTest {

    private EditableBlock mockBlock;
    private ConfigServer mockServer;
    private ForwardingObservable componentDetails;
    private ForwardingObservable currentConfig;
    private static final String IOC_NAME = "Ioc name";

    @Before
    public void setUp() {
        mockBlock = mock(EditableBlock.class);
        mockServer = mock(ConfigServer.class);
        componentDetails = mock(ForwardingObservable.class);
        currentConfig = mock(ForwardingObservable.class);
        when(mockServer.currentConfig()).thenReturn(currentConfig);
        when(mockServer.componentDetails()).thenReturn(componentDetails);
    }

    @Test
    public void GIVEN_no_iocs_in_config_or_component_WHEN_call_getInCurrentConfig_THEN_false() {

        // Arrange
        when(currentConfig.getValue()).thenReturn(new Configuration(IOC_NAME, "desc"));
        when(componentDetails.getValue()).thenReturn(Collections.emptySet());

        IocState iocState = new IocState(mockServer, IOC_NAME, true, "desc");

        // Act
        boolean isInCurrentConfig = iocState.getInCurrentConfig();

        // Assert
        assertEquals(isInCurrentConfig, false);
    }

    @Test
    public void GIVEN_ioc_in_config_WHEN_call_getInCurrentConfig_THEN_true() {

        // Arrange
        Configuration configWithIoc =
                new Configuration("Config name", "desc", "", Collections.singleton(new Ioc(IOC_NAME)),
                        Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        when(currentConfig.getValue()).thenReturn(configWithIoc);
        when(componentDetails.getValue()).thenReturn(Collections.emptySet());

        IocState iocState = new IocState(mockServer, IOC_NAME, true, "desc");

        // Act
        boolean isInCurrentConfig = iocState.getInCurrentConfig();

        // Assert
        assertEquals(isInCurrentConfig, true);
    }

    @Test
    public void GIVEN_ioc_in_selected_component_WHEN_call_getInCurrentConfig_THEN_true() {

        // Arrange
        Configuration configWithComp = new Configuration("Config name", "desc", "", Collections.emptySet(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.singleton(new ComponentInfo("Selected component")), Collections.emptySet());

        Collection<Configuration> components = Collections.singleton(new Configuration("Selected component", "desc", "",
                Collections.singleton(new Ioc(IOC_NAME)), Collections.emptySet(), Collections.emptySet(),
                Collections.emptySet(), Collections.emptySet()));

        when(currentConfig.getValue()).thenReturn(configWithComp);
        when(componentDetails.getValue()).thenReturn(components);

        IocState iocState = new IocState(mockServer, IOC_NAME, true, "desc");

        // Act
        boolean isInCurrentConfig = iocState.getInCurrentConfig();

        // Assert
        assertEquals(isInCurrentConfig, true);
    }

    @Test
    public void GIVEN_ioc_in_not_selected_component_WHEN_call_getInCurrentConfig_THEN_false() {

        // Arrange
        Configuration configWithNoComp = new Configuration("Config name", "desc");

        Collection<Configuration> components = Collections.singleton(new Configuration("Not selected component", "desc",
                "", Collections.singleton(new Ioc(IOC_NAME)), Collections.emptySet(), Collections.emptySet(),
                Collections.emptySet(), Collections.emptySet()));

        when(currentConfig.getValue()).thenReturn(configWithNoComp);
        when(componentDetails.getValue()).thenReturn(components);

        IocState iocState = new IocState(mockServer, IOC_NAME, true, "desc");

        // Act
        boolean isInCurrentConfig = iocState.getInCurrentConfig();

        // Assert
        assertEquals(isInCurrentConfig, false);
    }
}

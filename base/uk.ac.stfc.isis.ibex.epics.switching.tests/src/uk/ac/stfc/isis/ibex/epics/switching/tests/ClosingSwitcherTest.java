
/**
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

package uk.ac.stfc.isis.ibex.epics.switching.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ClosingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnSwitchBehaviour;
import uk.ac.stfc.isis.ibex.epics.switching.SwitcherProvider;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ClosingSwitcherTest {
    
    private static final String PV_ADDRESS = "PREFIX:SUFFIX";

    private ObservableFactory<String> obsFactory;
    private ClosingSwitcher closingSwitcher;
    private ClosableCachingObservable<String> closableCachingObservable;
    private ChannelType<String> channelType;
    private InstrumentInfo instrumentInfo;

    @Before
    public void setUp() {
        // Arrange
        instrumentInfo = mock(InstrumentInfo.class);
        when(instrumentInfo.pvPrefix()).thenReturn("PREFIX:");

        closableCachingObservable = mock(ClosableCachingObservable.class);
        
        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);

        closingSwitcher = new ClosingSwitcher();

        SwitcherProvider switcherProvider = mock(SwitcherProvider.class);
        when(switcherProvider.getObservableSwitcher(OnSwitchBehaviour.CLOSING)).thenReturn(closingSwitcher);

        obsFactory = new ObservableFactory<String>(channelType, OnSwitchBehaviour.CLOSING, switcherProvider);
    }

    @Test
    public void calling_close_calls_close_on_source_observable() {
        // Act
        obsFactory.getPVObserverable(PV_ADDRESS);
        closingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        verify(closableCachingObservable, times(1)).close();
    }

}

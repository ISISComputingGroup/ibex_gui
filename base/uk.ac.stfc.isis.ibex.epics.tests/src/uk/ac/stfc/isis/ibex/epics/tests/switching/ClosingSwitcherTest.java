
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

package uk.ac.stfc.isis.ibex.epics.tests.switching;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ClosingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ClosingSwitcherTest {
    
    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_ADDRESS = PV_PREFIX + "SUFFIX";
    private static final String PV_ADDRESS_2 = PV_PREFIX + "SUFFIX_2";

    private ObservableFactory obsFactory;
    private ClosingSwitcher closingSwitcher;
    private ClosableObservable<String> closableCachingObservable;
    private ClosableObservable<String> closableCachingObservable2;
    private ChannelType<String> channelType;
    private InstrumentInfo instrumentInfo;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Arrange
        instrumentInfo = mock(InstrumentInfo.class);
        when(instrumentInfo.pvPrefix()).thenReturn(PV_PREFIX);

        closableCachingObservable = mock(ClosableObservable.class);
        closableCachingObservable2 = mock(ClosableObservable.class);
        
        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);
        when(channelType.reader(PV_ADDRESS_2)).thenReturn(closableCachingObservable2);

        closingSwitcher = new ClosingSwitcher();

        InstrumentSwitchers instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.CLOSE)).thenReturn(closingSwitcher);

        obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);
    }

    @Test
    public void switcher_with_no_observables_does_nothing_when_switching() {
        // Act
        closingSwitcher.switchInstrument(instrumentInfo);
    }

    @Test
    public void observable_factory_registers_pv_with_switcher() {
        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);

        // Assert
        assertTrue(closingSwitcher.getSwitchables().contains(switchable));
    }

    @Test
    public void switching_calls_close_on_observable() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        closingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        verify(closableCachingObservable, Mockito.atLeast(1)).close();
    }

    @Test
    public void switching_unregisters_observable_from_switcher() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        closingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(0, closingSwitcher.getSwitchables().size());
    }

    @Test
    public void switching_unregisters_multiple_observables_from_switcher() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS_2);
        closingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(0, closingSwitcher.getSwitchables().size());
    }

    @Test
    public void switching_twice_correctly_switches_observables_added_after_first_switch() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        closingSwitcher.switchInstrument(instrumentInfo);

        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS_2);
        closingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        verify(closableCachingObservable2, Mockito.atLeast(1)).close();
    }

    @Test
    public void closing_observable_manually_unregisters_observable_from_switcher() {
        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<String> switchable2 = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);

        switchable.close();

        // Assert
        assertFalse(closingSwitcher.getSwitchables().contains(switchable));
        assertTrue(closingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void closing_observable_manually_twice_does_nothing() {
        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<String> switchable2 = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);

        switchable.close();
        switchable.close();

        // Assert
        assertFalse(closingSwitcher.getSwitchables().contains(switchable));
        assertTrue(closingSwitcher.getSwitchables().contains(switchable2));
    }

}

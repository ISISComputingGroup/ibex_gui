
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.ObservablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

@SuppressWarnings("checkstyle:methodname")
public class ObservablePrefixChangingSwitcherTest {

    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_PREFIX_2 = "PREFIX_2:";
    private static final String PV_PREFIX_3 = "PREFIX_3:";
    private static final String SUFFIX = "SUFFIX";
    private static final String PV_ADDRESS = PV_PREFIX + SUFFIX;
    private static final String PV_ADDRESS_2 = PV_PREFIX_2 + SUFFIX;
    private static final String PV_ADDRESS_3 = PV_PREFIX_3 + SUFFIX;

    private ObservableFactory obsFactory;
    private ObservablePrefixChangingSwitcher observablePrefixChangingSwitcher;
    private ClosableObservable<String> closableCachingObservable;
    private ClosableObservable<String> closableCachingObservable2;
    private ClosableObservable<String> closableCachingObservable3;
    private ChannelType<String> channelType;
    private InstrumentInfo instrumentInfo;
    private InstrumentInfo instrumentInfo2;
    private InstrumentInfo instrumentInfo3;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Arrange
        instrumentInfo = mock(InstrumentInfo.class);
        when(instrumentInfo.pvPrefix()).thenReturn(PV_PREFIX);
        
        instrumentInfo2 = mock(InstrumentInfo.class);
        when(instrumentInfo2.pvPrefix()).thenReturn(PV_PREFIX_2);

        instrumentInfo3 = mock(InstrumentInfo.class);
        when(instrumentInfo3.pvPrefix()).thenReturn(PV_PREFIX_3);

        closableCachingObservable = mock(ClosableObservable.class);
        closableCachingObservable2 = mock(ClosableObservable.class);
        closableCachingObservable3 = mock(ClosableObservable.class);

        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);
        when(channelType.reader(PV_ADDRESS_2)).thenReturn(closableCachingObservable2);
        when(channelType.reader(PV_ADDRESS_3)).thenReturn(closableCachingObservable3);

        observablePrefixChangingSwitcher = new ObservablePrefixChangingSwitcher();
        // need to initalise to an instrument
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        InstrumentSwitchers instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.SWITCH))
                .thenReturn(observablePrefixChangingSwitcher);

        obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);
    }

    @Test
    public void switcher_with_no_observables_does_nothing_when_switching() {
        // Act
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);
    }

    @Test
    public void observable_factory_registers_pv_with_switcher() {
        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);

        // Assert
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(switchable));
    }

    @Test
    public void switching_instrument_calls_close_on_old_observable() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        verify(closableCachingObservable, times(1)).close();
    }

    @Test
    public void switching_does_not_unregister_observable_from_switcher() {
        // Act
        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(observable));
    }

    @Test
    public void switching_does_not_unregister_multiple_observables_from_switcher() {
        // Act
        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<String> observable2 = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(observable));
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(observable2));
    }

    @Test
    public void switching_instrument_creates_new_observable_for_new_pv() {
        // Act
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);
        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        // Assert
        assertEquals(closableCachingObservable2, observable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observable_twice() {
        // Act
        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(closableCachingObservable, observable.getSource());
    }

    @Test
    public void switching_twice_to_third_instrument_correctly_switches_observable_twice() {
        // Act
        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);
        assertEquals(closableCachingObservable2, observable.getSource());
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo3);

        // Assert
        assertEquals(closableCachingObservable3, observable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observables_added_after_first_switch() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        SwitchableObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(closableCachingObservable, observable.getSource());
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
        assertFalse(observablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
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
        assertFalse(observablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
    }
}

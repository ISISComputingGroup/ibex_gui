
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

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.ObservablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.OnSwitchBehaviour;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableInitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.switching.SwitcherProvider;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class ObservablePrefixChangingSwitcherTest {

    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_PREFIX_2 = "PREFIX_2:";
    private static final String SUFFIX = "SUFFIX";
    private static final String PV_ADDRESS = PV_PREFIX + SUFFIX;
    private static final String PV_ADDRESS_2 = PV_PREFIX_2 + SUFFIX;

    private ObservableFactory obsFactory;
    private ObservablePrefixChangingSwitcher observablePrefixChangingSwitcher;
    private ClosableCachingObservable<String> closableCachingObservable;
    private ClosableCachingObservable<String> closableCachingObservable2;
    private ChannelType<String> channelType;
    private InstrumentInfo instrumentInfo;
    private InstrumentInfo instrumentInfo2;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Arrange
        instrumentInfo = mock(InstrumentInfo.class);
        when(instrumentInfo.pvPrefix()).thenReturn(PV_PREFIX);
        
        instrumentInfo2 = mock(InstrumentInfo.class);
        when(instrumentInfo2.pvPrefix()).thenReturn(PV_PREFIX_2);

        closableCachingObservable = mock(ClosableCachingObservable.class);
        closableCachingObservable2 = mock(ClosableCachingObservable.class);

        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);
        when(channelType.reader(PV_ADDRESS_2)).thenReturn(closableCachingObservable2);

        observablePrefixChangingSwitcher = new ObservablePrefixChangingSwitcher();

        SwitcherProvider switcherProvider = mock(SwitcherProvider.class);
        when(switcherProvider.getObservableSwitcher(OnSwitchBehaviour.SWITCHING))
                .thenReturn(observablePrefixChangingSwitcher);

        obsFactory = new ObservableFactory(OnSwitchBehaviour.SWITCHING, switcherProvider);
    }

    @Test
    public void switcher_with_no_observables_does_nothing_when_switching() {
        // Act
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);
    }

    @Test
    public void observable_factory_registers_pv_with_switcher() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
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
        SwitchableInitialiseOnSubscribeObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(observable));
    }

    @Test
    public void switching_does_not_unregister_multiple_observables_from_switcher() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableInitialiseOnSubscribeObservable<String> observable2 = obsFactory.getSwitchableObservable(channelType,
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
        SwitchableInitialiseOnSubscribeObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        // Assert
        assertEquals(closableCachingObservable2, observable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observable_twice() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(closableCachingObservable, observable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observables_added_after_first_switch() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        SwitchableInitialiseOnSubscribeObservable<String> observable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);
        observablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(closableCachingObservable, observable.getSource());
    }

    @Test
    public void closing_observable_manually_unregisters_observable_from_switcher() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableInitialiseOnSubscribeObservable<String> switchable2 = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);

        switchable.close();

        // Assert
        assertFalse(observablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void closing_observable_manually_twice_does_nothing() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableInitialiseOnSubscribeObservable<String> switchable2 = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS_2);

        switchable.close();
        switchable.close();

        // Assert
        assertFalse(observablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(observablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
    }
}


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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ClosingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.NothingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.ObservablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

@SuppressWarnings("checkstyle:methodname")
public class ObservableFactoryTest {
    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_ADDRESS = PV_PREFIX + "SUFFIX";
    private static final String PV_ADDRESS_2 = PV_PREFIX + "SUFFIX_2";

    private ChannelType<String> channelType;
    private ChannelType<Integer> channelType2;
    private ClosingSwitcher closingSwitcher;
    private NothingSwitcher nothingSwitcher;
    private ObservablePrefixChangingSwitcher switchingSwitcher;
    private ClosableObservable<String> closableCachingObservable;
    private ClosableObservable<Integer> closableCachingObservable2;
    private InstrumentSwitchers instrumentSwitchers;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        closableCachingObservable = mock(ClosableObservable.class);
        closableCachingObservable2 = mock(ClosableObservable.class);

        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);
        channelType2 = mock(ChannelType.class);
        when(channelType2.reader(PV_ADDRESS_2)).thenReturn(closableCachingObservable2);

        closingSwitcher = new ClosingSwitcher();
        nothingSwitcher = new NothingSwitcher();
        switchingSwitcher = new ObservablePrefixChangingSwitcher();

        instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.CLOSE)).thenReturn(closingSwitcher);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.NOTHING)).thenReturn(nothingSwitcher);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.SWITCH)).thenReturn(switchingSwitcher);
    }

    @Test
    public void create_an_observable_assigns_it_to_closing_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        SwitchableObservable<String> obs = obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(closingSwitcher.getSwitchables().contains(obs));
    }

    @Test
    public void create_an_observable_does_not_assign_it_to_the_other_switchers() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        SwitchableObservable<String> obs = obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);

        // Assert
        assertFalse(switchingSwitcher.getSwitchables().contains(obs));
        assertFalse(nothingSwitcher.getSwitchables().contains(obs));
    }

    @Test
    public void create_an_observable_assigns_it_to_nothing_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.NOTHING, instrumentSwitchers);

        // Act
        SwitchableObservable<String> obs = obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(nothingSwitcher.getSwitchables().contains(obs));
    }

    @Test
    public void create_an_observable_assigns_it_to_switching_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);

        // Act
        SwitchableObservable<String> obs = obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(switchingSwitcher.getSwitchables().contains(obs));
    }

    @Test
    public void observable_factory_registers_multiple_pvs_with_closing_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<Integer> switchable2 = obsFactory
                .getSwitchableObservable(channelType2,
                PV_ADDRESS_2);

        // Assert
        assertTrue(closingSwitcher.getSwitchables().contains(switchable));
        assertTrue(closingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void observable_factory_registers_multiple_pvs_with_nothing_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.NOTHING, instrumentSwitchers);

        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<Integer> switchable2 = obsFactory
                .getSwitchableObservable(channelType2,
                PV_ADDRESS_2);

        // Assert
        assertTrue(nothingSwitcher.getSwitchables().contains(switchable));
        assertTrue(nothingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void observable_factory_registers_multiple_pvs_with_switching_switcher() {
        // Arrange
        ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);

        // Act
        SwitchableObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);
        SwitchableObservable<Integer> switchable2 = obsFactory
                .getSwitchableObservable(channelType2, PV_ADDRESS_2);

        // Assert
        assertTrue(switchingSwitcher.getSwitchables().contains(switchable));
        assertTrue(switchingSwitcher.getSwitchables().contains(switchable2));
    }

}

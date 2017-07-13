
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

package uk.ac.stfc.isis.ibex.epics.switching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableWritable;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.WritablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

@SuppressWarnings("checkstyle:methodname")
public class WritablePrefixChangingSwitcherTest {

    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_PREFIX_2 = "PREFIX_2:";
    private static final String PV_PREFIX_3 = "PREFIX_3:";
    private static final String SUFFIX = "SUFFIX";
    private static final String PV_ADDRESS = PV_PREFIX + SUFFIX;
    private static final String PV_ADDRESS_2 = PV_PREFIX_2 + SUFFIX;
    private static final String PV_ADDRESS_3 = PV_PREFIX_3 + SUFFIX;

    private WritableFactory wrtFactory;
    private WritablePrefixChangingSwitcher writablePrefixChangingSwitcher;
    private BaseWritable<String> writable;
    private BaseWritable<String> writable2;
    private BaseWritable<String> writable3;
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

        writable = mock(BaseWritable.class);
        writable2 = mock(BaseWritable.class);
        writable3 = mock(BaseWritable.class);

        channelType = mock(ChannelType.class);
        when(channelType.writer(PV_ADDRESS)).thenReturn(writable);
        when(channelType.writer(PV_ADDRESS_2)).thenReturn(writable2);
        when(channelType.writer(PV_ADDRESS_3)).thenReturn(writable3);

        writablePrefixChangingSwitcher = new WritablePrefixChangingSwitcher();
        // need to initalise to an instrument
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        InstrumentSwitchers instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.SWITCH))
                .thenReturn(writablePrefixChangingSwitcher);

        wrtFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);
    }

    @Test
    public void switcher_with_no_observables_does_nothing_when_switching() {
        // Act
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);
    }

    @Test
    public void writable_factory_registers_pv_with_switcher() {
        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(switchable));
    }

    @Test
    public void switching_instrument_calls_close_on_old_writable() {
        // Act
        wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        verify(writable, times(1)).close();
    }

    @Test
    public void switching_does_not_unregister_writable_from_switcher() {
        // Act
        Writable<String> observable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(observable));
    }

    @Test
    public void switching_does_not_unregister_multiple_observables_from_switcher() {
        // Act
        Writable<String> observable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<String> observable2 = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS_2);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(observable));
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(observable2));
    }

    @Test
    public void switching_instrument_creates_new_observable_for_new_pv() {
        // Act
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);
        SwitchableWritable<String> switchableWritable = (SwitchableWritable<String>) wrtFactory
                .getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        // Assert
        assertEquals(writable2, switchableWritable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observable_twice() {
        // Act
        SwitchableWritable<String> switchableWritable = (SwitchableWritable<String>) wrtFactory
                .getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(writable, switchableWritable.getSource());
    }

    @Test
    public void switching_twice_to_third_instrument_correctly_switches_observable_twice() {
        // Act
        SwitchableWritable<String> switchableWritable = (SwitchableWritable<String>) wrtFactory
                .getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);
        assertEquals(writable2, switchableWritable.getSource());
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo3);

        // Assert
        assertEquals(writable3, switchableWritable.getSource());
    }

    @Test
    public void switching_twice_correctly_switches_observables_added_after_first_switch() {
        // Act
        wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo2);

        SwitchableWritable<String> switchableWritable = (SwitchableWritable<String>) wrtFactory
                .getSwitchableWritable(channelType, PV_ADDRESS_2);
        writablePrefixChangingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(writable, switchableWritable.getSource());
    }

    @Test
    public void closing_observable_manually_unregisters_observable_from_switcher() {
        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<String> switchable2 = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS_2);

        switchable.close();

        // Assert
        assertFalse(writablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void closing_observable_manually_twice_does_nothing() {
        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<String> switchable2 = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS_2);

        switchable.close();
        switchable.close();

        // Assert
        assertFalse(writablePrefixChangingSwitcher.getSwitchables().contains(switchable));
        assertTrue(writablePrefixChangingSwitcher.getSwitchables().contains(switchable2));
    }
}

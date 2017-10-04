
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

import uk.ac.stfc.isis.ibex.epics.switching.ClosingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.NothingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.WritablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

@SuppressWarnings("checkstyle:methodname")
public class WritableFactoryTest {

    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_ADDRESS = PV_PREFIX + "SUFFIX";
    private static final String PV_ADDRESS_2 = PV_PREFIX + "SUFFIX_2";

    private ChannelType<String> channelType;
    private ChannelType<Integer> channelType2;
    private ClosingSwitcher closingSwitcher;
    private NothingSwitcher nothingSwitcher;
    private WritablePrefixChangingSwitcher switchingSwitcher;
    private BaseWritable<String> writer;
    private BaseWritable<Integer> writer2;
    private InstrumentSwitchers instrumentSwitchers;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        writer = mock(BaseWritable.class);
        writer2 = mock(BaseWritable.class);

        channelType = mock(ChannelType.class);
        when(channelType.writer(PV_ADDRESS)).thenReturn(writer);
        channelType2 = mock(ChannelType.class);
        when(channelType2.writer(PV_ADDRESS_2)).thenReturn(writer2);

        closingSwitcher = new ClosingSwitcher();
        nothingSwitcher = new NothingSwitcher();
        switchingSwitcher = new WritablePrefixChangingSwitcher();

        instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.CLOSE)).thenReturn(closingSwitcher);
        when(instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.NOTHING)).thenReturn(nothingSwitcher);
        when(instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.SWITCH)).thenReturn(switchingSwitcher);
    }

    @Test
    public void create_a_writable_assigns_it_to_closing_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        Writable<String> wrt = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(closingSwitcher.getSwitchables().contains(wrt));
    }

    @Test
    public void create_a_writable_does_not_assign_it_to_the_other_switchers() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        Writable<String> wrt = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);

        // Assert
        assertFalse(switchingSwitcher.getSwitchables().contains(wrt));
        assertFalse(nothingSwitcher.getSwitchables().contains(wrt));
    }

    @Test
    public void create_a_writable_assigns_it_to_nothing_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.NOTHING, instrumentSwitchers);

        // Act
        Writable<String> wrt = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(nothingSwitcher.getSwitchables().contains(wrt));
    }

    @Test
    public void create_a_writable_assigns_it_to_switching_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);

        // Act
        Writable<String> wrt = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);

        // Assert
        assertTrue(switchingSwitcher.getSwitchables().contains(wrt));
    }

    @Test
    public void writable_factory_registers_multiple_pvs_with_closing_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.CLOSE, instrumentSwitchers);

        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<Integer> switchable2 = wrtFactory.getSwitchableWritable(channelType2, PV_ADDRESS_2);

        // Assert
        assertTrue(closingSwitcher.getSwitchables().contains(switchable));
        assertTrue(closingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void writable_factory_registers_multiple_pvs_with_nothing_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.NOTHING, instrumentSwitchers);

        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<Integer> switchable2 = wrtFactory.getSwitchableWritable(channelType2, PV_ADDRESS_2);

        // Assert
        assertTrue(nothingSwitcher.getSwitchables().contains(switchable));
        assertTrue(nothingSwitcher.getSwitchables().contains(switchable2));
    }

    @Test
    public void wrtiable_factory_registers_multiple_pvs_with_switching_switcher() {
        // Arrange
        WritableFactory wrtFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, instrumentSwitchers);

        // Act
        Writable<String> switchable = wrtFactory.getSwitchableWritable(channelType, PV_ADDRESS);
        Writable<Integer> switchable2 = wrtFactory.getSwitchableWritable(channelType2, PV_ADDRESS_2);

        // Assert
        assertTrue(switchingSwitcher.getSwitchables().contains(switchable));
        assertTrue(switchingSwitcher.getSwitchables().contains(switchable2));
    }

}

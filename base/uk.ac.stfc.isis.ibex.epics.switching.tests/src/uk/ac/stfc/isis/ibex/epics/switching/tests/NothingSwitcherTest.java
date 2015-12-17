
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.NothingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableInitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;

public class NothingSwitcherTest {
    
    private static final String PV_PREFIX = "PREFIX:";
    private static final String PV_ADDRESS = PV_PREFIX + "SUFFIX";
    private static final String PV_ADDRESS_2 = PV_PREFIX + "SUFFIX_2";

    private ObservableFactory obsFactory;
    private NothingSwitcher nothingSwitcher;
    private ClosableCachingObservable<String> closableCachingObservable;
    private ClosableCachingObservable<String> closableCachingObservable2;
    private ChannelType<String> channelType;
    private InstrumentInfo instrumentInfo;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Arrange
        instrumentInfo = mock(InstrumentInfo.class);
        when(instrumentInfo.pvPrefix()).thenReturn(PV_PREFIX);

        closableCachingObservable = mock(ClosableCachingObservable.class);
        closableCachingObservable2 = mock(ClosableCachingObservable.class);
        
        channelType = mock(ChannelType.class);
        when(channelType.reader(PV_ADDRESS)).thenReturn(closableCachingObservable);
        when(channelType.reader(PV_ADDRESS_2)).thenReturn(closableCachingObservable2);

        nothingSwitcher = new NothingSwitcher();

        InstrumentSwitchers instrumentSwitchers = mock(InstrumentSwitchers.class);
        when(instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.NOTHING)).thenReturn(nothingSwitcher);

        obsFactory = new ObservableFactory(OnInstrumentSwitch.NOTHING, instrumentSwitchers);
    }

    @Test
    public void switcher_with_no_observables_does_nothing_when_switching() {
        // Act
        nothingSwitcher.switchInstrument(instrumentInfo);
    }

    @Test
    public void observable_factory_registers_pv_with_switcher() {
        // Act
        SwitchableInitialiseOnSubscribeObservable<String> switchable = obsFactory.getSwitchableObservable(channelType,
                PV_ADDRESS);

        // Assert
        assertTrue(nothingSwitcher.getSwitchables().contains(switchable));
    }

     @Test
    public void switching_does_not_unregister_observable_from_switcher() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        nothingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(1, nothingSwitcher.getSwitchables().size());
    }

    @Test
    public void switching_does_not_unregister_multiple_observables_from_switcher() {
        // Act
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS);
        obsFactory.getSwitchableObservable(channelType, PV_ADDRESS_2);
        nothingSwitcher.switchInstrument(instrumentInfo);

        // Assert
        assertEquals(2, nothingSwitcher.getSwitchables().size());
    }
}

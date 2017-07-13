
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.switching.ClosingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.NothingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.ObservablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.Switcher;
import uk.ac.stfc.isis.ibex.epics.switching.WritablePrefixChangingSwitcher;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

@SuppressWarnings("checkstyle:methodname")
public class InstrumentSwitchersTest {

    InstrumentSwitchers instrumentSwitchers = new InstrumentSwitchers();
    InstrumentInfo instrumentInfo = new InstrumentInfo("instrument", null, null);

    @Test
    public void get_nothing_observable_switcher_returns_a_nothing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.NOTHING);
        
        // Assert
        assertEquals(NothingSwitcher.class, switcher.getClass());
    }

    @Test
    public void get_closing_observable_switcher_returns_a_closing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.CLOSE);

        // Assert
        assertEquals(ClosingSwitcher.class, switcher.getClass());
    }

    @Test
    public void get_switching_observable_switcher_returns_an_observable_prefix_changing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getObservableSwitcher(OnInstrumentSwitch.SWITCH);

        // Assert
        assertEquals(ObservablePrefixChangingSwitcher.class, switcher.getClass());
    }

    @Test
    public void get_nothing_writable_switcher_returns_a_nothing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.NOTHING);

        // Assert
        assertEquals(NothingSwitcher.class, switcher.getClass());
    }

    @Test
    public void get_closing_writable_switcher_returns_a_closing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.CLOSE);

        // Assert
        assertEquals(ClosingSwitcher.class, switcher.getClass());
    }

    @Test
    public void get_switching_writable_switcher_returns_a_writable_prefix_changing_switcher() {
        // Act
        Switcher switcher = instrumentSwitchers.getWritableSwitcher(OnInstrumentSwitch.SWITCH);

        // Assert
        assertEquals(WritablePrefixChangingSwitcher.class, switcher.getClass());
    }
}


/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.instrumentinfo.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.MachineName;

@SuppressWarnings("checkstyle:methodname")
public class LocalHostInstrumentInfoTest {

    @Test
    public final void WHEN_local_host_instrument_info_is_constructed_THEN_it_has_a_valid_host_name() {
        // Arrange

        // Act

        // Assert
        assertTrue(new LocalHostInstrumentInfo().hasValidHostName());
    }

    @Test
    public final void WHEN_local_host_instrument_info_is_constructed_THEN_host_name_is_localhost() {
        // Arrange

        // Act
        
        // Assert
        assertEquals("localhost", new LocalHostInstrumentInfo().hostName());
    }

    @Test
    public final void GIVEN_machine_name_WHEN_local_host_instrument_info_is_constructed_THEN_name_is_machine_name() {
        // Arrange

        // Act

        // Assert
        assertEquals(MachineName.get(), new LocalHostInstrumentInfo().name());
    }

    @Test
    public void GIVEN_locahost_WHEN_get_THEN_PV_prefix_is_TE() {
        String hostname = "HOSTNAME";
        String expected = "TE:" + hostname + ":";
        LocalHostInstrumentInfo hostInfo = new LocalHostInstrumentInfo(hostname);

        String result = hostInfo.pvPrefix();

        assertEquals(expected, result);

    }

    @Test
    public void GIVEN_locahost_is_NDE_instrument_WHEN_get_THEN_PV_prefix_is_IN() {
        String hostname = "NDEMUONFE";
        String expected = "IN:" + "MUONFE" + ":";
        LocalHostInstrumentInfo hostInfo = new LocalHostInstrumentInfo(hostname);

        String result = hostInfo.pvPrefix();

        assertEquals(expected, result);

    }

    @Test
    public void GIVEN_locahost_is_NDX_instrument_WHEN_get_THEN_PV_prefix_is_IN() {
        String hostname = "NDXINST";
        String expected = "IN:" + "INST" + ":";
        LocalHostInstrumentInfo hostInfo = new LocalHostInstrumentInfo(hostname);

        String result = hostInfo.pvPrefix();

        assertEquals(expected, result);

    }
}

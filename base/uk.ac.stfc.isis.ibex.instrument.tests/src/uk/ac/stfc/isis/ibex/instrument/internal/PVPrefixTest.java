
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

package uk.ac.stfc.isis.ibex.instrument.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("checkstyle:methodname")
public class PVPrefixTest {
    

    @Before
    public void setUp() {
        
    }

    @Test
    public void GIVEN_local_machine_name_WHEN_get_THEN_PV_prefix_is_host_name_prefixed_by_TE() {
        String hostname = "HOSTNAME";
        String expected = "TE:" + hostname + ":";
        PVPrefix pvprefix = new PVPrefix(hostname);

        String result = pvprefix.toString();

        assertEquals(expected, result);

    }

    @Test
    public void GIVEN_instrument_machine_name_WHEN_get_THEN_PV_prefix_is_host_name_prefixed_by_IN() {
        String instrument = "MACHINE";
        String hostname = "NDX" + instrument;
        String expected = "IN:" + instrument + ":";
        PVPrefix pvprefix = new PVPrefix(hostname);

        String result = pvprefix.toString();

        assertEquals(expected, result);

    }

    @Test
    public void GIVEN_long_local_machine_name_WHEN_get_THEN_PV_prefix_is_host_name_with_CRC8_prefixed_by_TE() {
        String hostname = "NDWBLAH_REALLY_LONG";
        String expected = "TE:NDWBLA3C:";
        PVPrefix pvprefix = new PVPrefix(hostname);

        String result = pvprefix.toString();

        assertEquals(expected, result);

    }
    
}


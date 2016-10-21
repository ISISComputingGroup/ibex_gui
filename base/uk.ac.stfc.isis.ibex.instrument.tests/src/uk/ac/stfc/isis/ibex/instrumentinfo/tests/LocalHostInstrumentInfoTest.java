
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
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.MachineName;
import uk.ac.stfc.isis.ibex.instrument.internal.UserName;

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
        final String name = "my_machine";
        mock(MachineName.class);
        Mockito.doReturn(name).when(MachineName.get());

        // Act

        // Assert
        assertEquals(name, new LocalHostInstrumentInfo().name());
    }

    @Test
    public final void
            GIVEN_machine_name_and_user_name_WHEN_local_host_instrument_info_is_constructed_THEN_pv_prefix_is_machine_name_colon_user_name() {
        // Arrange
        final String machine = "my_machine";
        final String user = "i_am_the_user";
        mock(MachineName.class);
        Mockito.doReturn(machine).when(MachineName.get());
        mock(UserName.class);
        Mockito.doReturn(user).when(UserName.get());

        // Act

        // Assert
        assertEquals(machine + ":" + user, new LocalHostInstrumentInfo().pvPrefix());
    }
}

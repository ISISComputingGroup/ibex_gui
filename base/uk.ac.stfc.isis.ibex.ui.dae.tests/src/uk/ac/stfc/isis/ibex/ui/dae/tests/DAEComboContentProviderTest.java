
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.dae.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DAEComboContentProvider;

@SuppressWarnings({ "unchecked", "checkstyle:methodname", "checkstyle:magicnumber" })
public class DAEComboContentProviderTest {

	private static final String MOCK_TABLE_DIR = "C:\\Instrument\\Settings\\config\\NDXMOCK\\tables";
    private ForwardingObservable<String> mockSource;

    @Before
    public void setUp() {
        // Arrange
        mockSource = mock(ForwardingObservable.class);
        when(mockSource.currentError()).thenReturn(null);
        when(mockSource.isConnected()).thenReturn(true);
    }

    @Test
    public void
            GIVEN_no_list_WHEN_content_requested_THEN_returned_value_contains_error_text() {
    	
    	// Arrange
    	DAEComboContentProvider contentProvider = new DAEComboContentProvider(mockSource);
    	
        // Act
        String[] content = contentProvider.getContent(null, "mock");

        // Assert
        assertTrue(Arrays.asList(content).stream().map(String::toLowerCase).collect(Collectors.joining(" ")).contains("error"));
    }
}
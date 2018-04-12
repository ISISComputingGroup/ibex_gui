
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DAEComboContentProvider;

import org.junit.Before;
import org.junit.Test;

final class FileListStub extends UpdatedValue<Collection<String>> {
	// Done with a stub rather than a mock because getValue returns a final that Mockito can't handle
	
	@Override
	public void setValue(Collection<String> value) {
		super.setValue(value);
	}
}

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
    	when(mockSource.getValue()).thenReturn(MOCK_TABLE_DIR);
    }

    @Test
    public void GIVEN_no_list_WHEN_content_requested_THEN_returned_value_contains_error_text() {
    	
    	// Arrange
    	DAEComboContentProvider contentProvider = new DAEComboContentProvider(mockSource);
    	
        // Act
        String[] content = contentProvider.getContent(null, "mock");

        // Assert
        boolean containsError = false;
        for (String line : content) {
        	if (line.toLowerCase().contains("error")) {
        		containsError = true;
        		break;
        	}
        }
        assertTrue(containsError);
    }

    @Test
    public void GIVEN_list_has_no_value_WHEN_content_requested_THEN_returned_value_contains_mock_dir() {
    	
    	// Arrange
    	DAEComboContentProvider contentProvider = new DAEComboContentProvider(mockSource);
    	UpdatedValue<Collection<String>>mockFileList = mock(UpdatedValue.class);
    	when(mockFileList.getValue()).thenReturn(null);
    	
        // Act
        String[] content = contentProvider.getContent(mockFileList, "mock");

        // Assert
        boolean containsDir = false;
        for (String line : content) {
        	if (line.contains(MOCK_TABLE_DIR)) {
        		containsDir = true;
        		break;
        	}
        }
        assertTrue(containsDir);
    }

    @Test
    public void GIVEN_list_with_values_matching_pattern_WHEN_content_requested_THEN_returned_value_contains_all_list_values() {
    	
    	// Arrange
    	DAEComboContentProvider contentProvider = new DAEComboContentProvider(mockSource);
    	String pattern = "mock";

    	FileListStub fileList = new FileListStub();
    	final Collection<String> listValues = new ArrayList<String>();
    	listValues.add(pattern + "Table1");
    	listValues.add(pattern + "Table2");
    	listValues.add(pattern + "Table3");
    	fileList.setValue(listValues);
    	
    	String [] expectedValue = {pattern + "Table1", pattern + "Table2", pattern + "Table3"};
    	
        // Act
        String[] content = contentProvider.getContent(fileList, pattern);

        // Assert
        assertArrayEquals(expectedValue, content);
    }

    @Test
    // The current GUI logic relies on the DAE IOC to filter out non-matching files
    public void GIVEN_list_with_values_not_matching_pattern_WHEN_content_requested_THEN_returned_value_contains_all_list_values() {
    	
    	// Arrange
    	DAEComboContentProvider contentProvider = new DAEComboContentProvider(mockSource);
    	String pattern = "mock";

    	FileListStub fileList = new FileListStub();
    	final Collection<String> listValues = new ArrayList<String>();
    	listValues.add("Table1");
    	listValues.add("Table2");
    	listValues.add("Table3");
    	fileList.setValue(listValues);
    	
    	String [] expectedValue = {"Table1", "Table2", "Table3"};
    	
        // Act
        String[] content = contentProvider.getContent(fileList, pattern);

        // Assert
        assertArrayEquals(expectedValue, content);
    }
}
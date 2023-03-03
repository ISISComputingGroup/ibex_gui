
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2022 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.IocPatternFilter;

@SuppressWarnings("checkstyle:methodname")
@RunWith(org.mockito.junit.MockitoJUnitRunner.StrictStubs.class)
public class IocPatternFilterTest {
	
	private IocPatternFilter filter;
	private EditableIoc editableIoc;
    
	@Before
	public void setUp() {
		filter = new IocPatternFilter();
		editableIoc = Mockito.mock(EditableIoc.class);
		Mockito.when(editableIoc.getName()).thenReturn("Name");
		Mockito.when(editableIoc.getDescription()).thenReturn("Description");
	}
	
	@Test
	public void WHEN_search_for_empty_string_THEN_no_filtering() {
		filter.setPattern("");
		
		assertTrue(filter.isLeafMatch(null, editableIoc));
	}
	
	@Test
	public void WHEN_search_by_name_THEN_ioc_match() {
		filter.setPattern("name");
		
		assertTrue(filter.isLeafMatch(null, editableIoc));
	}
	
	@Test
	public void WHEN_search_by_description_THEN_ioc_match() {
		filter.setPattern("desc");
		
		assertTrue(filter.isLeafMatch(null, editableIoc));
	}
	
	@Test
	public void WHEN_search_by_non_existant_name_THEN_ioc_no_match() {
		filter.setPattern("I will not match.");
		
		assertFalse(filter.isLeafMatch(null, editableIoc));
	}
	
	@Test
	public void WHEN_search_by_non_existant_decription_THEN_ioc_no_match() {
		filter.setPattern("I will not match.");
		
		assertFalse(filter.isLeafMatch(null, editableIoc));
	}
}

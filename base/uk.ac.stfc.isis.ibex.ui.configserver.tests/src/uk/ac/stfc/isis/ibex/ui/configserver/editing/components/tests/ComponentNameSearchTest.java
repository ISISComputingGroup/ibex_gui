/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.components.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentNameSearch;

public class ComponentNameSearchTest {

	private ComponentNameSearch search;
	
	private final String COMPONENT_NAME = "COMP_1";
	private final Configuration component = mockComponent(COMPONENT_NAME);
	
	private Configuration mockComponent(String name) {
		Configuration component = mock(Configuration.class);
		when(component.getName()).thenReturn(name);
		return component;
	}


	@Before
	public void setUp() {
		search = new ComponentNameSearch();
	}
	
	@Test
	public void GIVEN_empty_match_WHEN_search_THEN_match() {
		search.setSearchText("");
		assertTrue(search.select(null, null, component));
	}
	
	@Test
	public void GIVEN_full_match_WHEN_search_THEN_match() {
		search.setSearchText(COMPONENT_NAME);
		assertTrue(search.select(null, null, component));
	}
	
	@Test
	public void GIVEN_partial_match_WHEN_search_THEN_match() {
		search.setSearchText(COMPONENT_NAME.substring(0, 2));
		assertTrue(search.select(null, null, component));
	}
	
	@Test
	public void GIVEN_no_match_WHEN_search_THEN_no_match() {
		search.setSearchText("CONFIG_1");
		assertFalse(search.select(null, null, component));
	}
}

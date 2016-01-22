
/*
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

package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import java.util.Arrays;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

@SuppressWarnings("checkstyle:methodname")
public class ComponentsTest extends EditableConfigurationTest {

	@Test
	public void a_configuration_can_have_components() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertContains(edited.asConfiguration().getComponents(), MOTOR);
	}
	
	@Test
	public void a_component_has_no_components_of_its_own() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertEmpty(edited.asComponent().getComponents());
	}
	
	@Test
	public void an_empty_configuration_has_no_components_selected() {
		EditableConfiguration edited = edit(emptyConfig());
		assertEmpty(edited.getEditableComponents().getSelected());
	}
	
	@Test
	public void config_components_are_initially_selected() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertContains(edited.getEditableComponents().getSelected(), MOTOR);
	}
	
	@Test
	public void a_component_can_be_added_or_removed() {
		EditableConfiguration edited = edit(emptyConfig());
		assertDoesNotContain(edited.getEditableComponents().getSelected(), MOTOR);
		
		edited.getEditableComponents().toggleSelection(Arrays.asList(MOTOR));
		assertContains(edited.getEditableComponents().getSelected(), MOTOR);
		
		edited.getEditableComponents().toggleSelection(Arrays.asList(MOTOR));
		assertDoesNotContain(edited.getEditableComponents().getSelected(), MOTOR);
	}
}

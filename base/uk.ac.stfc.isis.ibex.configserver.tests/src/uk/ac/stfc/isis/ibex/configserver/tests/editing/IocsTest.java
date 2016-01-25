
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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

@SuppressWarnings("checkstyle:methodname")
public class IocsTest extends EditableConfigurationTest {
	@Test
	public void an_editable_config_has_all_available_iocs_to_edit() {
		allIocs.add(GALIL01);
		EditableConfiguration edited = edit(config());
		assertContains(edited.getEditableIocs(), new EditableIoc(GALIL01));
	}
	
	@Test
	public void redundant_iocs_are_removed_from_the_config() {
		// Arrange
		GALIL01.setAutostart(false);
		GALIL01.setRestart(false);
		iocs.add(GALIL01);
		EditableConfiguration edited = edit(config());
		
		// Assert
		assertDoesNotContain(edited.asConfiguration().getIocs(), GALIL01);
	}
	
	@Test
	public void an_ioc_is_added_to_the_config_after_it_has_been_edited() {
		GALIL01.setAutostart(false);
		allIocs.add(GALIL01);
		
		EditableConfiguration edited = edit(config());
		assertDoesNotContain(edited.asConfiguration().getIocs(), GALIL01);
		
		EditableIoc galil = getFirst(edited.getEditableIocs());
		galil.setAutostart(true);
		
		assertContains(edited.asConfiguration().getIocs(), GALIL01);
	}
}

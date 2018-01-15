
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;

@SuppressWarnings("checkstyle:methodname")
public class ConfigurationTest extends EditableConfigurationTest {
	
	@Test
	public void doing_nothing_to_an_empty_configuration_leaves_the_configuration_unchanged() {
		EditableConfiguration edited = edit(emptyConfig());		
		assertAreEqual(emptyConfig(), edited.asConfiguration());
	}
	
	@Test
	public void doing_nothing_to_a_configuration_leaves_the_configuration_unchanged() {
		populateConfig();
		EditableConfiguration edited = edit(config());		
		assertAreEqual(config(), edited.asConfiguration());
	}
	
	@Test
	public void GIVEN_blank_default_synoptic_WHEN_creating_configuration_THEN_default_synoptic_name_is_NONE() {
		// Arrange
		String defaultSynoptic = "";
		
		// Act
		Configuration config = new Configuration("name", "description", defaultSynoptic, iocs, blocks, groups, components, history);
		
		// Assert
		assertEquals(ConfigEditing.NONE_SYNOPTIC_NAME, config.synoptic());
		
	}
}

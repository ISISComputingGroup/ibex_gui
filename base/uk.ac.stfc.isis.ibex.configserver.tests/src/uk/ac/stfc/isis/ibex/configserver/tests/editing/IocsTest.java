
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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

@SuppressWarnings("checkstyle:methodname")
public class IocsTest extends EditableConfigurationTest {

    @Test
    public void GIVEN_new_blank_configuration_THEN_selected_iocs_are_empty() {

        // Act
        EditableConfiguration edited = edit(config());

        // Assert
        assertTrue(edited.getAddedIocs().size() == 0);
    }

    @Test
    public void GIVEN_ioc_is_in_config_WHEN_creating_editable_configuration_THEN_ioc_is_part_of_saved_config() {
        // Arrange
        iocs.add(GALIL01);

        // Act
        EditableConfiguration edited = edit(config());

        // Assert
        assertContains(edited.asConfiguration().getIocs(), GALIL01);
    }

	@Test
    public void GIVEN_a_blank_ioc_WHEN_saving_config_THEN_blank_ioc_is_part_of_config() {
		// Arrange
        GALIL01.setAutostart(false);
        GALIL01.setRestart(false);
        iocs.add(GALIL01);

        // Act
        EditableConfiguration edited = edit(config());
		
		// Assert
        assertContains(edited.asConfiguration().getIocs(), GALIL01);
	}

    @Test
    public void GIVEN_ioc_was_added_to_config_THEN_ioc_is_unavailable_to_be_added() {
        // Arrange
        iocs.add(GALIL01);

        // Act
        EditableConfiguration edited = edit(config());

        // Assert
        assertDoesNotContain(edited.getAvailableIocs(), GALIL01);
    }

    @Test
    public void GIVEN_ioc_was_removed_from_editable_configuration_THEN_ioc_is_not_part_of_saved_configuration() {
        // Arrange
        iocs.add(GALIL01);
        EditableConfiguration edited = edit(config());
        
        // Act
        List<EditableIoc> toRemove = new ArrayList<EditableIoc>();
        toRemove.add(GALIL01);
        edited.removeIocs(toRemove);

        // Assert
        assertDoesNotContain(edited.getAddedIocs(), GALIL01);
    }

    @Test
    public void GIVEN_ioc_was_removed_from_editable_configuration_THEN_ioc_is_available_to_be_added() {
        // Arrange
        iocs.add(GALIL01);
        EditableConfiguration edited = edit(config());

        // Act
        List<EditableIoc> toRemove = new ArrayList<EditableIoc>();
        toRemove.add(GALIL01);
        edited.removeIocs(toRemove);

        // Assert
        assertContains(edited.getAvailableIocs(), GALIL01);
    }

}

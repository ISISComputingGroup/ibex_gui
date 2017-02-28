
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

import java.util.Collection;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

@SuppressWarnings("checkstyle:methodname")
public class EditableIocTest {

	@Test
	public void getting_available_macros_when_none_returns_empty_list() {
		// Arrange
		EditableIoc ioc = new EditableIoc("testioc");
		
		// Act
		Collection<Macro> avail = ioc.getAvailableMacros();
		
		// Assert
		assertEquals(avail.size(), 0);
		
	}

    @Test
    public void WHEN_editable_ioc_created_THEN_description_is_set() {

        // Arrange
        final String expected = "this is the description";
        Ioc ioc = new Ioc("");

        // Act
        EditableIoc editableIoc = new EditableIoc(ioc, expected);

        // Assert
        assertEquals(editableIoc.getDescription(), expected);
    }

    @Test
    public void WHEN_editable_ioc_created_from_ioc_THEN_both_have_the_same_name() {

        // Arrange
        final String expected = "name";
        Ioc ioc = new Ioc(expected);

        // Act
        EditableIoc editableIoc = new EditableIoc(ioc, "");

        // Assert
        assertEquals(editableIoc.getName(), expected);
    }

    @Test
    public void WHEN_editable_ioc_created_from_ioc_THEN_both_have_the_same_sim_level() {

        // Arrange
        final SimLevel expected = SimLevel.RECSIM;
        Ioc ioc = new Ioc("");
        ioc.setSimLevel(expected);

        // Act
        EditableIoc editableIoc = new EditableIoc(ioc, "");

        // Assert
        assertEquals(editableIoc.getSimLevel(), expected);
    }

    @Test
    public void WHEN_editable_ioc_created_from_ioc_THEN_both_have_the_same_autostart() {

        // Arrange
        final boolean expected = true;
        Ioc ioc = new Ioc("");
        ioc.setAutostart(expected);

        // Act
        EditableIoc editableIoc = new EditableIoc(ioc, "");

        // Assert
        assertEquals(editableIoc.getAutostart(), expected);
    }

    @Test
    public void WHEN_editable_ioc_created_from_ioc_THEN_both_have_the_same_restart() {

        // Arrange
        final boolean expected = true;
        Ioc ioc = new Ioc("");
        ioc.setRestart(expected);

        // Act
        EditableIoc editableIoc = new EditableIoc(ioc, "");

        // Assert
        assertEquals(editableIoc.getRestart(), expected);
    }
}

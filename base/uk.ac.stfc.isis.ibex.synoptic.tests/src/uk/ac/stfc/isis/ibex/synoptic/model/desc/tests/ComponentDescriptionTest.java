
/**
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

package uk.ac.stfc.isis.ibex.synoptic.model.desc.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public class ComponentDescriptionTest {

    private static final String COMPONENT_NAME = "name";
    private static final String COPIED_NAME_SUFFIX = " (copy)";
    private static final String NEW_NAME = "new name";

    ComponentDescription reference;

    @Before
    public void setUp() {
        // Arrange
        reference = new ComponentDescription();
        reference.setParent(new ComponentDescription());
        reference.setName(COMPONENT_NAME);
    }

    @Test
    public void copied_object_has_same_parent_as_reference_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(reference);

        // Assert
        assertSame(reference.getParent(), copied.getParent());
    }

    @Test
    public void copy_constructor_sets_name_correctly() {
        // Act
        ComponentDescription copied = new ComponentDescription(reference);

        // Assert
        assertTrue((reference.name() + COPIED_NAME_SUFFIX).equals(copied.name()));
    }

    @Test
    public void copy_constructor_creates_name_that_is_not_linked_to_reference_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(reference);
        copied.setName(NEW_NAME);

        // Assert
        assertTrue(reference.name().equals(COMPONENT_NAME));
        assertTrue(copied.name().equals(NEW_NAME));
    }
}

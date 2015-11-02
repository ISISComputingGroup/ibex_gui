
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;

public class ComponentDescriptionTest {

    private static final String COMPONENT_NAME = "name";
    private static final String COPIED_NAME_SUFFIX = " (copy)";
    private static final String NEW_NAME = "new name";
    private static final String OTHER_NAME = "other name";

    private static final String PV_NAME_1 = "pv1";
    private static final String PV_NAME_2 = "pv2";

    private static final String CHILD_NAME_1 = "child1";
    private static final String CHILD_NAME_2 = "child2";

    private PV pv1;
    private PV pv2;

    private ComponentDescription child1;
    private ComponentDescription child2;

    ComponentDescription source;

    @Before
    public void setUp() {
        // Arrange
        source = new ComponentDescription();
        source.setParent(new ComponentDescription());
        source.setName(COMPONENT_NAME);
        source.setType(ComponentType.UNKNOWN);
        TargetDescription targetDescription = new TargetDescription();
        targetDescription.setName(NEW_NAME);
        source.setTarget(targetDescription);
        
        pv1 = new PV();
        pv1.setDisplayName(PV_NAME_1);
        pv2 = new PV();
        pv2.setDisplayName(PV_NAME_2);
        source.addPV(pv1);
        source.addPV(pv2);

        child1 = new ComponentDescription();
        child1.setName(CHILD_NAME_1);
        child2 = new ComponentDescription();
        child2.setName(CHILD_NAME_2);
        source.addComponent(child1);
        source.addComponent(child2);
    }

    @Test
    public void copied_object_has_same_parent_as_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        assertSame(source.getParent(), copied.getParent());
    }

    @Test
    public void copied_object_has_name_appended_with_copy() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        assertTrue((source.name() + COPIED_NAME_SUFFIX).equals(copied.name()));
    }

    @Test
    public void copied_object_has_name_that_is_not_linked_to_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);
        copied.setName(NEW_NAME);

        // Assert
        assertTrue(source.name().equals(COMPONENT_NAME));
        assertTrue(copied.name().equals(NEW_NAME));
    }

    @Test
    public void copied_object_has_same_type_as_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        assertSame(source.type(), copied.type());

    }

    @Test
    public void copied_object_has_type_that_is_not_liked_to_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);
        copied.setType(ComponentType.GONIOMETER);

        // Assert
        assertNotSame(source.type(), copied.type());
    }

    @Test
    public void copied_object_has_same_target_as_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        assertEquals(source.target().name(), copied.target().name());
    }

    @Test
    public void copied_object_has_target_that_is_not_liked_to_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);
        TargetDescription otherTargetDescription = copied.target();
        otherTargetDescription.setName(OTHER_NAME);

        // Assert
        assertNotSame(source.target(), copied.target());
        assertEquals(NEW_NAME, source.target().name());
        assertEquals(OTHER_NAME, copied.target().name());
    }

    @Test
    public void copied_object_with_null_target_still_has_null_target() {
        // Act
        source.setTarget(null);
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        assertSame(source.target(), copied.target());
    }

    @Test
    public void copied_object_has_same_pvs_as_source_object() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);
        
        // Assert
        for (int i = 1; i < source.pvs().size(); i++) {
            assertEquals(source.pvs().get(i).displayName(), copied.pvs().get(i).displayName());
        }
    }

    @Test
    public void copied_object_pvs_not_linked_to_source_pvs() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        for (int i = 1; i < source.pvs().size(); i++) {
            copied.pvs().get(i).setDisplayName(NEW_NAME);
            assertFalse(source.pvs().get(i).displayName().equals(NEW_NAME));
            assertTrue(copied.pvs().get(i).displayName().equals(NEW_NAME));
        }
    }

    @Test
    public void copied_object_has_same_children_as_source_object_with_no_name_changes() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        for (int i = 1; i < source.components().size(); i++) {
            assertEquals(source.components().get(i).name(), copied.components().get(i).name());
        }
    }

    @Test
    public void copied_object_children_not_linked_to_source_children() {
        // Act
        ComponentDescription copied = new ComponentDescription(source);

        // Assert
        for (int i = 1; i < source.pvs().size(); i++) {
            copied.components().get(i).setName(NEW_NAME);
            assertFalse(source.components().get(i).name().equals(NEW_NAME));
            assertTrue(copied.components().get(i).name().equals(NEW_NAME));
        }
    }


}

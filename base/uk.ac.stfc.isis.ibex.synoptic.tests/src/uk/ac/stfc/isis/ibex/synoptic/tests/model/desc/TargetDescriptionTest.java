
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

package uk.ac.stfc.isis.ibex.synoptic.tests.model.desc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

@SuppressWarnings("checkstyle:methodname")
public class TargetDescriptionTest {
    
    private static final String NAME = "name";
    private static final String NEW_NAME = "new name";
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final String NEW_KEY = "new value";
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    private static final String NEW_VALUE = "new value";
    
    private TargetDescription source;
    
    @Before
    public void setUp() {
        source = new TargetDescription();
        source.setName(NAME);
        source.setType(TargetType.OPI);

        Property prop1 = new Property(KEY1, VALUE1);
        source.addProperty(prop1);
        Property prop2 = new Property(KEY2, VALUE2);
        source.addProperty(prop2);
    }

    @Test
    public void copied_object_has_same_name_as_source_object() {
        // Act
        TargetDescription copied = new TargetDescription(source);

        // Assert
        assertEquals(source.name(), copied.name());
    }

    @Test
    public void copied_object_has_name_that_is_not_linked_to_source_object() {
        // Act
        TargetDescription copied = new TargetDescription(source);
        copied.setName(NEW_NAME);

        // Assert
        assertEquals(NAME, source.name());
        assertEquals(NEW_NAME, copied.name());
    }

    @Test
    public void copied_object_has_same_target_type_as_source_object() {
        // Act
        TargetDescription copied = new TargetDescription(source);

        // Assert
        assertEquals(source.type(), copied.type());
    }

    @Test
    public void copied_object_has_target_type_that_is_not_linked_to_source_object() {
        // Act
        TargetDescription copied = new TargetDescription(source);
        copied.setType(TargetType.COMPONENT);

        // Assert
        assertEquals(TargetType.OPI, source.type());
        assertEquals(TargetType.COMPONENT, copied.type());
    }

    @Test
    public void copied_object_has_same_properties_as_source_object_with_no_name_changes() {
        // Act
        TargetDescription copied = new TargetDescription(source);

        // Assert
        for (int i = 1; i < source.properties().size(); i++) {
            copied.removeProperty(copied.properties().get(i));
            copied.addProperty(new Property(NEW_KEY, NEW_VALUE));
            assertTrue(copied.properties().get(i).value().equals(NEW_VALUE));
            assertFalse(source.properties().get(i).equals(NEW_VALUE));
        }
    }
}

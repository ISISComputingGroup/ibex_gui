
/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.synoptic.tests.model.desc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

@SuppressWarnings("checkstyle:methodname")
public class TargetDescriptionTest {
    
    private static final String NAME = "name";
    private static final String NEW_NAME = "new name";
    private static final String KEY_0 = "key1";
    private static final String KEY_1 = "key2";
    private static final String VALUE_0 = "value1";
    private static final String VALUE_1 = "value2";
    
    private static final Property PROPERTY_0 = new Property(KEY_0, VALUE_0);
    private static final Property PROPERTY_1 = new Property(KEY_1, VALUE_1);

    private TargetDescription source;
    
    @Before
    public void setUp() {
        source = new TargetDescription();
        source.setName(NAME);
        source.setType(TargetType.OPI);

        List<String> propertyKeys = new ArrayList<>();
        propertyKeys.add(KEY_0);
        propertyKeys.add(KEY_1);
        source.addProperties(propertyKeys);

        source.replaceOrAddProperty(PROPERTY_0);
        source.replaceOrAddProperty(PROPERTY_1);
    }

    @Test
    public void can_get_list_of_properties() {
        // Act
        List<Property> properties = source.getProperties();
        
        // Assert
        assertTrue(properties.size() == 2);
    }

    @Test
    public void list_of_properties_contains_set_key() {
        // Act
        List<Property> properties = source.getProperties();

        // Assert
        assertTrue(KEY_0.equals(properties.get(0).key()));
    }

    @Test
    public void list_of_properties_contains_set_values() {
        // Act
        List<Property> properties = source.getProperties();

        // Assert
        assertTrue(VALUE_0.equals(properties.get(0).value()));
    }

    @Test
    public void if_key_is_in_property_list_contains_return_true() {
        // Assert
        assertTrue(source.containsProperty(KEY_0));
    }

    @Test
    public void if_key_is_not_in_property_list_contains_return_false() {
        // Assert
        assertFalse(source.containsProperty("not a key"));
    }

    @Test
    public void clear_properties_clears_the_list_of_properties() {
        // Act
        source.clearProperties();

        // Assert
        assertTrue(source.getProperties().size() == 0);
    }

    @Test
    public void clear_properties_on_an_empty_list_still_returns_empty_list() {
        // Act
        source.clearProperties();
        source.clearProperties();

        // Assert
        assertTrue(source.getProperties().size() == 0);
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
        for (int i = 1; i < source.getProperties().size(); i++) {
            String sourceKey = source.getProperties().get(i).key();
            String sourceValue = source.getProperties().get(i).value();
            assertTrue(copied.getProperties().get(i).key().equals(sourceKey));
            assertTrue(copied.getProperties().get(i).value().equals(sourceValue));
        }
    }

    @Test
    public void GIVEN_property_in_target_WHEN_property_replaced_THEN_property_is_changed() {
        // Arrange
        Property newProperty = new Property(KEY_0, "new value");

        // Act
        source.replaceOrAddProperty(newProperty);

        // Assert
        Property defaultProperty = new Property();
        assertSame(newProperty, source.getProperty(KEY_0, defaultProperty));
    }

    @Test
    public void GIVEN_property_not_in_target_WHEN_property_replaced_THEN_property_is_added() {
        // Arrange
        String new_key = "new key";
        Property newProperty = new Property(new_key, "new value");

        // Act
        source.replaceOrAddProperty(newProperty);

        // Assert
        Property defaultProperty = new Property();
        assertSame(newProperty, source.getProperty(new_key, defaultProperty));
        assertEquals("Number of properties in the list", 3, source.getProperties().size());
    }

}

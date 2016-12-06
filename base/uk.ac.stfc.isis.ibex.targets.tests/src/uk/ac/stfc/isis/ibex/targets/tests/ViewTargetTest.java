
/*
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

package uk.ac.stfc.isis.ibex.targets.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.targets.ViewTarget;

/**
 * This class is responsible for testing the target class.
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class ViewTargetTest {

    private static final String TARGET_NAME = "test_name";

    @Test
    public void GIVEN_name_WHEN_view_target_is_contstucted_with_name_THEN_view_target_name_matches_constructor_name() {
        
        // Act
        ViewTarget vt = new ViewTarget(TARGET_NAME);
        
        // Assert
        assertEquals(TARGET_NAME, vt.name());
    }

    @Test
    public void GIVEN_name_WHEN_view_target_is_constructed_with_name_THEN_view_target_as_string_is_name() {

        // Act
        ViewTarget vt = new ViewTarget(TARGET_NAME);

        // Assert
        assertEquals(TARGET_NAME, vt.toString());
    }

    @Test
    public void WHEN_view_target_is_constructed_THEN_properties_are_empty() {

        // Act
        ViewTarget vt = new ViewTarget(TARGET_NAME);

        // Assert
        assertTrue(vt.properties().values().isEmpty());
    }

    @Test
    public void
            GIVEN_property_and_propertyless_view_target_WHEN_property_is_added_to_view_target_THEN_properties_contains_property_and_only_property() {

        // Arrange
        final String property_key = "key";
        final String property_value = "value";
        ViewTarget vt = new ViewTarget(TARGET_NAME);

        // Act
        vt.addProperty(property_key, property_value);

        // Assert
        assertTrue(vt.properties().values().size() == 1);
        assertTrue(vt.properties().containsKey(property_key));
        assertEquals(vt.properties().get(property_key), property_value);
    }

    @Test
    public void
            GIVEN_property_and_view_target_with_pre_existing_properties_WHEN_property_is_added_to_view_target_THEN_properties_contains_original_properties_and_new_one() {

        // Arrange
        ViewTarget vt = new ViewTarget(TARGET_NAME);
        
        final String existing_key_1 = "key1";
        final String existing_value_1 = "value1";
        final String existing_key_2 = "key2";
        final String existing_value_2 = "value2";
        
        vt.addProperty(existing_key_1, existing_value_1);
        vt.addProperty(existing_key_2, existing_value_2);

        final String new_property_key = "key";
        final String new_property_value = "value";

        // Act
        vt.addProperty(new_property_key, new_property_value);

        // Assert
        assertTrue(vt.properties().values().size() == 3);

        assertTrue(vt.properties().containsKey(existing_key_1));
        assertTrue(vt.properties().containsKey(existing_key_1));
        assertTrue(vt.properties().containsKey(new_property_key));

        assertEquals(vt.properties().get(new_property_key), new_property_value);
        assertEquals(vt.properties().get(existing_key_1), existing_value_1);
        assertEquals(vt.properties().get(existing_key_2), existing_value_2);
    }

    @Test
    public void
            GIVEN_view_target_with_property_WHEN_property_is_added_with_same_key_and_different_value_THEN_property_is_updated_with_new_value() {

        // Arrange
        ViewTarget vt = new ViewTarget(TARGET_NAME);

        final String key = "key";
        final String old_value = "old value";
        final String new_value = "new value";

        vt.addProperty(key, old_value);

        // Act
        vt.addProperty(key, new_value);

        // Assert
        assertTrue(vt.properties().values().size() == 1);
        assertTrue(vt.properties().containsKey(key));
        assertEquals(vt.properties().get(key), new_value);
    }

}

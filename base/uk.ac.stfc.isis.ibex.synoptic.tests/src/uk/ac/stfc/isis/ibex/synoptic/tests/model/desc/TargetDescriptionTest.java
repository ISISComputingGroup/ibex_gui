
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

import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

@SuppressWarnings("checkstyle:methodname")
public class TargetDescriptionTest {
    
    private static final String NAME = "name";
    private static final String NEW_NAME = "new name";
    private static final String KEY_0 = "key1";
    private static final String KEY_1 = "key2";

    private TargetDescription source;
    
    @Before
    public void setUp() {
        source = new TargetDescription();
        source.setName(NAME);
        source.setType(TargetType.OPI);

        List<MacroInfo> propertyKeys = new ArrayList<>();
        propertyKeys.add(new MacroInfo(KEY_0, ""));
        propertyKeys.add(new MacroInfo(KEY_1, ""));
        source.addProperties(propertyKeys);
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
        assertTrue(KEY_0.equals(properties.get(0).getKey()));
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
    public void copied_object_has_macros_that_are_not_linked_to_source_object() {
    	final String sourcePropertyValue = "SOURCE";
    	final String copiedPropertyValue = "COPIED";
    	
    	// Arrange
    	source.getProperties().get(0).setValue(sourcePropertyValue);
    	TargetDescription copied = new TargetDescription(source);
    	
        // Act (should not affect property of source).
        copied.getProperties().get(0).setValue(copiedPropertyValue);

        // Assert
        assertEquals(source.getProperties().get(0).getValue(), sourcePropertyValue);
        assertEquals(copied.getProperties().get(0).getValue(), copiedPropertyValue);
    }

    @Test
    public void copied_object_has_same_properties_as_source_object_with_no_name_changes() {
        // Act
        TargetDescription copied = new TargetDescription(source);

        // Assert
        for (int i = 1; i < source.getProperties().size(); i++) {
            String sourceKey = source.getProperties().get(i).getKey();
            String sourceValue = source.getProperties().get(i).getValue();
            assertTrue(copied.getProperties().get(i).getKey().equals(sourceKey));
            assertTrue(copied.getProperties().get(i).getValue().equals(sourceValue));
        }
    }

}

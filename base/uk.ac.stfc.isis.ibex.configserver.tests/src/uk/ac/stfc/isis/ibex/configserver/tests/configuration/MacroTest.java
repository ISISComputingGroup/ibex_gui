
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;

@SuppressWarnings("checkstyle:methodname")
public class MacroTest {
    
    @Test
    public void test_GIVEN_macro_with_default_WHEN_get_default_display_THEN_default_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "defaultValue", HasDefault.YES);
        
        String defaultText = m.getDefaultDisplay();
        
        assertEquals("defaultValue", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_empty_string_default_WHEN_get_default_display_THEN_empty_string_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "", HasDefault.YES);
        
        String defaultText = m.getDefaultDisplay();
        
        assertEquals("(default is the empty string)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_no_default_WHEN_get_default_display_THEN_no_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.NO);
        
        String defaultText = m.getDefaultDisplay();
        
        assertEquals("(no default)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_unknown_default_WHEN_get_default_display_THEN_unknown_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.UNKNOWN);
        
        String defaultText = m.getDefaultDisplay();
        
        assertEquals("(default unknown)", defaultText);
    }
	
	@Test
	public void test_WHEN_creating_a_macro_with_string_parameters_THEN_macro_has_correct_parameters() {
		// Arrange
		// Act
		Macro m = new Macro("macro1", "value1", "a test macro", ".+", "", null);
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue().get(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void test_GIVEN_a_macro_WHEN_setting_a_new_value_THEN_value_is_set() {
		// Arrange
		Macro m = new Macro("macro1", "value1", "a test macro", ".+", "", null);
		
		// Act
		m.setValue(Optional.of("new value"));
		
		// Assert
		assertEquals(m.getValue().get(), "new value");
	}
	
	@Test
	public void test_GIVEN_a_macro_WHEN_create_new_macro_with_copy_constructor_THEN_new_macro_has_correct_parameters() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+", "", null);
		
		// Act
		Macro m = new Macro(n);
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue().get(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void test_GIVEN_a_copied_macro_WHEN_setting_a_new_value_THEN_value_is_set() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+", "", null);
		Macro m = new Macro(n);
		
		// Act
		m.setValue(Optional.of("new value"));
		
		// Assert
		assertEquals(m.getValue().get(), "new value");
	}
}

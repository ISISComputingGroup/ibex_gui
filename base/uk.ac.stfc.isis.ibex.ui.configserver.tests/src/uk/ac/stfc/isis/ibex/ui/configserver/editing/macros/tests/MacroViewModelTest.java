
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.MacroViewModel;

@SuppressWarnings("checkstyle:methodname")
public class MacroViewModelTest {
    
    @Test
    public void test_GIVEN_macro_with_default_WHEN_get_default_display_THEN_default_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "defaultValue", HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("defaultValue", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_empty_string_default_WHEN_get_default_display_THEN_empty_string_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "", HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(default is the empty string)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_no_default_WHEN_get_default_display_THEN_no_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.NO, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(no default)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_unknown_default_WHEN_get_default_display_THEN_unknown_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.UNKNOWN, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(default unknown)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_null_WHEN_view_model_created_THEN_display_value_is_default() {
    	Macro m = new Macro("name", null, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayValue();
        
        assertEquals("(default)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_null_WHEN_view_model_created_THEN_use_default_is_true() {
    	Macro m = new Macro("name", null, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        boolean useDefault = macroViewModel.getUseDefault();
        
        assertTrue(useDefault);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_empty_string_WHEN_view_model_created_THEN_use_default_is_false() {
    	Macro m = new Macro("name", "", "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        boolean useDefault = macroViewModel.getUseDefault();
        
        assertTrue(useDefault);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_WHEN_view_model_created_THEN_use_default_is_false() {
    	Macro m = new Macro("name", "A_VALUE", "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        boolean useDefault = macroViewModel.getUseDefault();
        
        assertFalse(useDefault);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_WHEN_view_model_created_THEN_display_value_is_value() {
    	String expectedMacroValue = "A_VALUE";
    	Macro m = new Macro("name", expectedMacroValue, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String macroValue = macroViewModel.getDisplayValue();
        
        assertEquals(expectedMacroValue, macroValue);
    }
    
    @Test
    public void test_GIVEN_macro_with_value_WHEN_use_default_set_THEN_macro_value_set_to_null() {
    	Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        macroViewModel.setUseDefault(true);
        
        assertEquals("", m.getValue());
    }
    
    @Test
    public void test_GIVEN_macro_with_value_WHEN_use_default_set_false_THEN_macro_value_unchanged() {
    	String expectedMacroValue = "A_VALUE";
    	Macro m = new Macro("name", expectedMacroValue, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        macroViewModel.setUseDefault(false);
        
        assertEquals(expectedMacroValue, m.getValue());
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_null_WHEN_use_default_set_false_THEN_macro_value_set_to_empty_string() {
    	Macro m = new Macro("name", null, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        macroViewModel.setUseDefault(false);
        
        assertEquals("", m.getValue());
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_null_WHEN_use_default_set_true_THEN_macro_value_still_null() {
    	Macro m = new Macro("name", null, "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        macroViewModel.setUseDefault(true);
        
        assertEquals("", m.getValue());
    }
    
    @Test
    public void test_GIVEN_macro_with_value_of_null_WHEN_value_text_box_entered_THEN_macro_value_set_to_empty_string_and_use_default_enabled() {
    	Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.YES, true);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        macroViewModel.setUseDefault(true);
        
        assertEquals("", m.getValue());
    }
}

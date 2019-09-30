
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

import java.util.Optional;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.MacroViewModel;

@SuppressWarnings("checkstyle:methodname")
public class MacroViewModelTest {
    
    @Test
    public void test_GIVEN_macro_with_default_WHEN_get_default_display_THEN_default_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "defaultValue", HasDefault.YES);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("defaultValue", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_empty_string_default_WHEN_get_default_display_THEN_empty_string_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", "", HasDefault.YES);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(default is the empty string)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_no_default_WHEN_get_default_display_THEN_no_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.NO);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(no default)", defaultText);
    }
    
    @Test
    public void test_GIVEN_macro_with_unknown_default_WHEN_get_default_display_THEN_unknown_default_message_returned() {
        Macro m = new Macro("name", "value", "description", "pattern", null, HasDefault.UNKNOWN);
        MacroViewModel macroViewModel = new MacroViewModel(m);
        
        String defaultText = macroViewModel.getDisplayDefault();
        
        assertEquals("(default unknown)", defaultText);
    }
}

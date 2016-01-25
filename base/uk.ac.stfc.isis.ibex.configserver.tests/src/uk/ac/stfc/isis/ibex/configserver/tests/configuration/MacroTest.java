
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

package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;

@SuppressWarnings("checkstyle:methodname")
public class MacroTest {
	
	@Test
	public void create_macro_using_string_parameters_constructor_works() {
		// Arrange
		// Act
		Macro m = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void string_parameters_constructor_change_value_works() {
		// Arrange
		Macro m = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Act
		m.setValue("new value");
		
		// Assert
		assertEquals(m.getValue(), "new value");
	}
	
	@Test
	public void create_macro_using_copy_constructor_works() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Act
		Macro m = new Macro(n);
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void copy_constructor_change_value_works() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+");
		Macro m = new Macro(n);
		
		// Act
		m.setValue("new value");
		
		// Assert
		assertEquals(m.getValue(), "new value");
	}
}

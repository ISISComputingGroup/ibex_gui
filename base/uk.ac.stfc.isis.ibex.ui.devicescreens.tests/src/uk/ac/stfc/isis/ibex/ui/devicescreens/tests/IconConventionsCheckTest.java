//CHECKSTYLE:OFF

/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2020 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.devicescreens.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;

/**
 * Unit tests for the icon conventions checks
 */
public class IconConventionsCheckTest {
	
	private static final String DEFAULT_ICON = "cog.png";
	private static final String DEFAULT_THUMB = "cog_tb.png";
	
	@Test
	public void GIVEN_unknown_type_WHEN_thumbnailNameForType_THEN_return_default_thumbnail() {
		// Arrange
		ComponentType type = Enum.valueOf(ComponentType.class, "UNKNOWN");
		
		// Act
		String result = ComponentIcons.thumbnailNameForType(type);
		
		// Assert
		assertEquals(DEFAULT_THUMB, result);
	}
	
	@Test
	public void GIVEN_type_WHEN_thumbnailNameForType_THEN_return_correct_thumbnail_name() {
		// Arrange
		ComponentType type = Enum.valueOf(ComponentType.class, "LAKESHORE");
		String expected = "lakeshore_tb.png";
		
		// Act
		String result = ComponentIcons.thumbnailNameForType(type);
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void GIVEN_unknown_type_WHEN_iconNameForType_THEN_return_default_icon() {
		// Arrange
		ComponentType type = Enum.valueOf(ComponentType.class, "UNKNOWN");
		
		// Act
		String result = ComponentIcons.iconNameForType(type);
		
		// Assert
		assertEquals(DEFAULT_ICON, result);
	}
	
	
	@Test
	public void GIVEN_type_WHEN_iconNameForType_THEN_return_correct_icon_name() {
		// Arrange
		ComponentType type = Enum.valueOf(ComponentType.class, "LAKESHORE");
		String expected = "lakeshore.png";
		
		// Act
		String result = ComponentIcons.iconNameForType(type);
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void GIVEN_type_for_icon_WHEN_typeNameToFileName_THEN_return_correct_icon_name() {
		// Arrange
		String type = "LAKESHORE";
		String expected = "lakeshore.png";
		
		// Act
		String result = IconConventionsCheck.typeNameToFileName(type, "icon");
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void GIVEN_invalid_type_for_icon_WHEN_typeNameToFileName_THEN_return_default_icon() {
		// Arrange
		String type = "";
		String expected = "cog.png";
		
		// Act
		String result = IconConventionsCheck.typeNameToFileName(type, "icon");
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void GIVEN_type_for_thumbnail_WHEN_typeNameToFileName_THEN_return_correct_thumbnail() {
		// Arrange
		String type = "LAKESHORE";
		String expected = "lakeshore_tb.png";
		
		// Act
		String result = IconConventionsCheck.typeNameToFileName(type, "thumbnail");
		
		// Assert
		assertEquals(expected, result);
	}
	
	@Test
	public void GIVEN_invalid_type_for_thumbnail_WHEN_typeNameToFileName_THEN_return_default_thumbnail() {
		// Arrange
		String type = "";
		String expected = "cog_tb.png";
		
		// Act
		String result = IconConventionsCheck.typeNameToFileName(type, "thumbnail");
		
		// Assert
		assertEquals(expected, result);
	}
	
}

//CHECKSTYLE:ON

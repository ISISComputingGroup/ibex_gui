
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

package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.MacroValueValidator;

public class MacroValueValidatorTest {
	
	private static final String VALID_VALUE = "123.123";
	private static final String INVALID_VALUE = "123.123.123";
	private static final String PATTERN = "^[0-9]+\\.[0-9]+$";
	private static final String INVALID_PATTERN = "+++";
	
	Label messageDisplayer;
	
	PropertyChangeListener mockNameIsValidListener;
	PropertyChangeListener showWarningIconListener;
	
	Macro macro;
	Macro macroWithInvalidPattern;

	@Captor
	private ArgumentCaptor<PropertyChangeEvent> changeCaptor;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		messageDisplayer = mock(Label.class);
		mockNameIsValidListener = mock(PropertyChangeListener.class);
		showWarningIconListener = mock(PropertyChangeListener.class);
		
		macro = new Macro("name", "value", "description", PATTERN);
		macroWithInvalidPattern = new Macro("name", "value", "description", INVALID_PATTERN);
	}
	
	public MacroValueValidator getValidator(Macro macro) {
		MacroValueValidator validator = new MacroValueValidator(macro, messageDisplayer);
		
		validator.addPropertyChangeListener(MacroValueValidator.NAME_IS_VALID, mockNameIsValidListener);
		validator.addPropertyChangeListener(MacroValueValidator.SHOW_WARNING_ICON, showWarningIconListener);
		
		return validator ;
	}
	
	@Test
	public void if_macro_is_null_then_not_OK() {
		// Arrange
		MacroValueValidator nullMacroValidator = getValidator(null);
		
		// Act
		IStatus status =  nullMacroValidator.validate("");
		
		// Assert
		assertFalse(status.isOK());
	}
	
	@Test
	public void if_macro_is_null_then_blank_warning_message() {
		// Arrange
		MacroValueValidator nullMacroValidator = getValidator(null);
				
		// Act
		IStatus status =  nullMacroValidator.validate("");
		
		// Assert
		assertEquals(MacroValueValidator.NO_MESSAGE, status.getMessage());
	}
	
	@Test
	public void if_macro_is_null_then_nameIsValid_property_change_to_false() {
		// Arrange
		MacroValueValidator nullMacroValidator = getValidator(null);
		
		// Act
		nullMacroValidator.validate("");
		
		// Assert
		verify(mockNameIsValidListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(false, changeCaptor.getValue().getNewValue());
	}
	
	@Test
	public void if_macro_is_null_then_showWarningIcon_property_left_as_false() {
		// Arrange
		MacroValueValidator nullMacroValidator = getValidator(null);
		
		// Act
		nullMacroValidator.validate("");
		
		// Assert
		verify(showWarningIconListener, times(0)).propertyChange(any(PropertyChangeEvent.class));
	}
	
	@Test
	public void if_validation_string_empty_then_not_OK() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate("");
		
		// Assert
		assertFalse(status.isOK());
	}
	
	@Test
	public void if_validation_string_empty_then_blank_warning_message() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate("");
		
		// Assert
		assertEquals(MacroValueValidator.NO_MESSAGE, status.getMessage());
	}
	
	@Test
	public void if_validation_string_empty_then_nameIsValid_property_change_to_false() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate("");
		
		// Assert
		verify(mockNameIsValidListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(false, changeCaptor.getValue().getNewValue());
	}
	
	@Test
	public void if_validation_string_empty_then_showWarningIcon_property_left_as_false() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate("");
		
		// Assert
		verify(showWarningIconListener, times(0)).propertyChange(any(PropertyChangeEvent.class));
	}
	
	@Test
	public void if_validation_string_valid_then_OK() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate(VALID_VALUE);
		
		// Assert
		assert(status.isOK());
	}
	
	@Test
	public void if_validation_string_valid_then_blank_warning_message() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate(VALID_VALUE);
		
		// Assert
		assertEquals("OK", status.getMessage());
	}
	
	@Test
	public void if_validation_string_valid_then_nameIsValid_property_stays_as_true() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate(VALID_VALUE);
		
		// Assert
		verify(mockNameIsValidListener, times(0)).propertyChange(any(PropertyChangeEvent.class));
	}
	
	@Test
	public void if_validation_string_valid_then_showWarningIcon_property_stays_as_false() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate(VALID_VALUE);
		
		// Assert
		verify(showWarningIconListener, times(0)).propertyChange(any(PropertyChangeEvent.class));
	}
	
	@Test
	public void if_validation_string_invalid_then_not_OK() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate(INVALID_VALUE);
		
		// Assert
		assertFalse(status.isOK());
	}
	
	@Test
	public void if_validation_string_invalid_then_blank_warning_message() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		IStatus status =  macroValidator.validate(INVALID_VALUE);
		
		// Assert
		assertEquals(MacroValueValidator.PATTERN_MISMATCH_MESSAGE, status.getMessage());
	}
	
	@Test
	public void if_validation_string_invalid_then_nameIsValid_property_change_to_false() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate(INVALID_VALUE);
		
		// Assert
		verify(mockNameIsValidListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(false, changeCaptor.getValue().getNewValue());
	}
	
	@Test
	public void if_validation_string_invalid_then_showWarningIcon_property_change_to_true() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macro);
		
		// Act
		macroValidator.validate(INVALID_VALUE);
		
		// Assert
		verify(showWarningIconListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(true, changeCaptor.getValue().getNewValue());
	}
	
	@Test
	public void if_pattern_invalid_invalid_then_not_OK() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macroWithInvalidPattern);
		
		// Act
		IStatus status =  macroValidator.validate(INVALID_VALUE);
		
		// Assert
		assertFalse(status.isOK());
	}
	
	@Test
	public void if_pattern_invalid_then_blank_warning_message() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macroWithInvalidPattern);
		
		// Act
		IStatus status =  macroValidator.validate(INVALID_VALUE);
		
		// Assert
		assertEquals(MacroValueValidator.PATTERN_INVALID, status.getMessage());
	}
	
	@Test
	public void if_pattern_invalid_then_nameIsValid_property_change_to_false() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macroWithInvalidPattern);
		
		// Act
		macroValidator.validate(INVALID_VALUE);
		
		// Assert
		verify(mockNameIsValidListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(false, changeCaptor.getValue().getNewValue());
	}
	
	@Test
	public void if_pattern_invalid_then_showWarningIcon_property_change_to_true() {
		// Arrange
		MacroValueValidator macroValidator = getValidator(macroWithInvalidPattern);
		
		// Act
		macroValidator.validate(INVALID_VALUE);
		
		// Assert
		verify(showWarningIconListener, times(1)).propertyChange(changeCaptor.capture());
		assertEquals(true, changeCaptor.getValue().getNewValue());
	}
}

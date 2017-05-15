
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.MacroValueValidator;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

@SuppressWarnings("checkstyle:methodname")
public class MacroValueValidatorTest {
	
	private static final String VALID_VALUE = "123.123";
	private static final String INVALID_VALUE = "123.123.123";
	private static final String PATTERN = "^[0-9]+\\.[0-9]+$";
	private static final String INVALID_PATTERN = "+++";

    PropertyChangeListener mockErrorListener;
	
	Macro macro;
	Macro macroWithInvalidPattern;

    String errorPrefix;

	@Captor
	private ArgumentCaptor<PropertyChangeEvent> changeCaptor;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
        mockErrorListener = mock(PropertyChangeListener.class);
		
		macro = new Macro("name", "value", "description", PATTERN);
		macroWithInvalidPattern = new Macro("name", "value", "description", INVALID_PATTERN);

        errorPrefix = macro.getName() + ": ";
    }

    private void createValidator(Macro macro) {
        MacroValueValidator validator = new MacroValueValidator(macro);

        validator.addPropertyChangeListener("error", mockErrorListener);
	}
	
    private ErrorMessage getErrorMessage() {
        verify(mockErrorListener, times(1)).propertyChange(changeCaptor.capture());
        return (ErrorMessage) changeCaptor.getValue().getNewValue();
	}
	
	@Test
    public void if_validation_string_empty_then_OK() {
	    // Arrange
        createValidator(macro);
	    
		// Act
        macro.setValue("");
		
		// Assert
        assertEquals(false, getErrorMessage().isError());
	}
	
	@Test
    public void if_validation_string_valid_then_OK() {
        // Arrange
        createValidator(macro);

		// Act
        macro.setValue(VALID_VALUE);
		
		// Assert
        assertEquals(false, getErrorMessage().isError());
	}
	
	@Test
    public void if_validation_string_valid_then_null_warning_message() {
        // Arrange
        createValidator(macro);

		// Act
        macro.setValue(VALID_VALUE);
		
		// Assert
        assertEquals(null, getErrorMessage().getMessage());
	}
	
	@Test
	public void if_validation_string_invalid_then_not_OK() {
        // Arrange
        createValidator(macro);

        // Act
        macro.setValue(INVALID_VALUE);

        // Assert
        assertEquals(true, getErrorMessage().isError());
	}
	
	@Test
    public void if_validation_string_invalid_then_pattern_mismatch_message() {
        // Arrange
        createValidator(macro);

        // Act
        macro.setValue(INVALID_VALUE);
		
		// Assert
        assertEquals(errorPrefix + MacroValueValidator.PATTERN_MISMATCH_MESSAGE, getErrorMessage().getMessage());
	}
	
	@Test
    public void if_pattern_invalid_then_not_OK() {
        // Arrange
        createValidator(macroWithInvalidPattern);
		
        // Act
        macroWithInvalidPattern.setValue(INVALID_VALUE);
		
		// Assert
        assertEquals(true, getErrorMessage().isError());
	}
	
	@Test
    public void if_pattern_invalid_then_pattern_invalid_message() {
		// Arrange
        createValidator(macroWithInvalidPattern);
		
		// Act
        macroWithInvalidPattern.setValue(INVALID_VALUE);
		
		// Assert
        assertEquals(errorPrefix + MacroValueValidator.PATTERN_INVALID, getErrorMessage().getMessage());
	}
}

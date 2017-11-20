 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector.tests;

import static org.junit.Assert.*;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector.ComponentNameVerifier;

/**
 * Tests for the component name verifier.
 */
public class ComponentNameVerifierTests {
    
    private ComponentNameVerifier verifier;
    private VerifyEvent event;
    
    @Before
    public void setup(){
        verifier = new ComponentNameVerifier();
        
        // A verify event needs a source event with a non-null widget.
        Event source = new Event();
        source.widget = Mockito.mock(Widget.class);
        
        event = new VerifyEvent(source);        
    }
    
    @Test
    public void GIVEN_a_well_formed_component_name_WHEN_validating_THEN_valid(){
        // Arrange
        event.text = "Valid_component_name";
        // Act
        verifier.verifyText(event);
        // Assert
        assertTrue(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_containing_spaces_WHEN_validating_THEN_valid(){
        // Arrange
        event.text = "Valid component name";
        // Act
        verifier.verifyText(event);
        // Assert
        assertTrue(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_with_brackets_WHEN_validating_THEN_valid(){
        // Arrange
        event.text = "Valid_component_name (brackets)";
        // Act
        verifier.verifyText(event);
        // Assert
        assertTrue(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_with_dashes_WHEN_validating_THEN_valid(){
        // Arrange
        event.text = "Valid-component-name";
        // Act
        verifier.verifyText(event);
        // Assert
        assertTrue(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_with_an_exclamation_mark_WHEN_validating_THEN_invalid(){
        // Arrange
        event.text = "Invalid!";
        // Act
        verifier.verifyText(event);
        // Assert
        assertFalse(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_with_lots_of_special_characters_WHEN_validating_THEN_invalid(){
        // Arrange
        event.text = "£$%^&*()";
        // Act
        verifier.verifyText(event);
        // Assert
        assertFalse(event.doit);
    }
    
    @Test
    public void GIVEN_a_component_name_containing_numbers_WHEN_validating_THEN_valid(){
        // Arrange
        event.text = "componennt 12345";
        // Act
        verifier.verifyText(event);
        // Assert
        assertTrue(event.doit);
    }
}

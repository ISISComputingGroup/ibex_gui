
/**
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.validators.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

public class RunControlValidatorTest {

	private void checkPassedWithMessage(String low_limit, String high_limit, String expected) {
        // Act
        RunControlValidator limitsValid = new RunControlValidator();
        boolean isValid = limitsValid.isValid(low_limit, high_limit);
        
        // Assert
        assertTrue(isValid);
        assertTrue(limitsValid.getErrorMessage().equals(expected));
	}
	
	private void checkFailedWithMessage(String low_limit, String high_limit, String expected) {
        // Act
        RunControlValidator limitsValid = new RunControlValidator();
        boolean isValid = limitsValid.isValid(low_limit, high_limit);
        
        // Assert
        assertFalse(isValid);
        assertTrue(limitsValid.getErrorMessage().equals(expected));
	}
	
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     */
    @Test
    public void valid_limits() {
        checkPassedWithMessage("0.0", "10.0", RunControlValidator.NO_ERROR);
    }

    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void invalid_low_limit_conversion() {
        checkFailedWithMessage("a", "10.0", RunControlValidator.LOW_LIMIT_FLOAT);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void invalid_high_limit_conversion() {
        checkFailedWithMessage("15.0", "^", RunControlValidator.HIGH_LIMIT_FLOAT);
    }

    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void invalid_low_limit_empty() {       
        checkFailedWithMessage("", "1.0", RunControlValidator.LOW_LIMIT_EMPTY);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void invalid_high_limit_empty() {        
        checkFailedWithMessage("2.5", "", RunControlValidator.HIGH_LIMIT_EMPTY);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void invalid_high_limit_lower() {
        checkFailedWithMessage("2.5", "1.0", RunControlValidator.LOW_LIMIT_LESS);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    public void valid_limits_equal() {
        checkPassedWithMessage("5.0", "5.0", RunControlValidator.NO_ERROR);
    }
}

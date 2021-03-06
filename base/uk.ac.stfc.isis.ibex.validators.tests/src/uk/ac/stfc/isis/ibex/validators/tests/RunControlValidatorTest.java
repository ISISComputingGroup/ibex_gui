
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

@SuppressWarnings("checkstyle:methodname")
public class RunControlValidatorTest {

	private void checkPassedWithMessage(double lowLimit, double highLimit, String expected) {
        // Act
        RunControlValidator limitsValid = new RunControlValidator();
        boolean isValid = limitsValid.isValid(lowLimit, highLimit, true);
        
        // Assert
        assertTrue(isValid);
        assertTrue(limitsValid.getErrorMessage().equals(expected));
	}
	
	private void checkFailedWithMessage(double lowLimit, double highLimit, String expected) {
        // Act
        RunControlValidator limitsValid = new RunControlValidator();
        boolean isValid = limitsValid.isValid(lowLimit, highLimit, true);
        
        // Assert
        assertFalse(isValid);
        assertTrue(limitsValid.getErrorMessage().equals(expected));
	}
	
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void valid_limits() {
        checkPassedWithMessage(0., 10., RunControlValidator.NO_ERROR);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void invalid_high_limit_lower() {
        checkFailedWithMessage(2.5, 1.0, RunControlValidator.LOW_LIMIT_LESS);
    }
    
    /**
     * Test method for {@link uk.ac.stfc.isis.ibex.validators.RunControlValidator#isValid(java.lang.String, java.lang.String)}.
     * .
     */
    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void valid_limits_equal() {
        checkPassedWithMessage(5.0, 5.0, RunControlValidator.NO_ERROR);
    }
}

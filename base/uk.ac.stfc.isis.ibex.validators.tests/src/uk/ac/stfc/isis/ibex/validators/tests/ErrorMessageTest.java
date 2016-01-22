
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

package uk.ac.stfc.isis.ibex.validators.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

@SuppressWarnings("checkstyle:methodname")
public class ErrorMessageTest {

    @Test
    public void default_constructor_sets_error_to_false() {
        // Arrange
        // Act
        ErrorMessage msg = new ErrorMessage();
        // Assert
        assertEquals(false, msg.isError());
    }

    @Test
    public void non_default_constructor_sets_error_and_message() {
        // Arrange
        // Act
        ErrorMessage msg = new ErrorMessage(true, "Error!");
        // Assert
        assertEquals(true, msg.isError());
        assertEquals("Error!", msg.getMessage());
    }

    @Test
    public void setting_message_and_error_works() {
        // Arrange
        ErrorMessage msg = new ErrorMessage();
        // Act
        msg.setMessage("Error!");
        msg.setError(true);
        // Assert
        assertEquals(true, msg.isError());
        assertEquals("Error!", msg.getMessage());
    }
}

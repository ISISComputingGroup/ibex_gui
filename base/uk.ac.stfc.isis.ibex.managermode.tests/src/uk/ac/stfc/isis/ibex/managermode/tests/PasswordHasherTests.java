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
package uk.ac.stfc.isis.ibex.managermode.tests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.managermode.PasswordHasher;

/**
 *
 */
public class PasswordHasherTests {

    private static final String TEST_PASSWORD = "this is the test password";
    private static final String TEST_PASSWORD_HASH = "qwvi4z4BQkUZ9ghTxqI4LJvE8xxkFMtldhl5UWPsp4A=";

    private PasswordHasher hasher;

    @Before
    public void setUp() {
        hasher = new PasswordHasher(TEST_PASSWORD_HASH);
    }

    @Test
    public void GIVEN_the_correct_password_WHEN_the_password_is_checked_THEN_returns_true()
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (!hasher.isCorrectPassword(TEST_PASSWORD)) {
            Assert.fail("Password didn't hash to expected value");
        }
    }

    @Test
    public void GIVEN_a_null_password_WHEN_the_password_is_checked_THEN_returns_false()
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (hasher.isCorrectPassword((String) null)) {
            Assert.fail("Null password allowed login");
        }
    }

    @Test
    public void GIVEN_an_empty_password_WHEN_the_password_is_checked_THEN_returns_false()
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (hasher.isCorrectPassword("")) {
            Assert.fail("Empty password allowed login");
        }
    }

    @Test
    public void GIVEN_an_incorrect_password_WHEN_the_password_is_checked_THEN_returns_false()
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (hasher.isCorrectPassword("not the correct password")) {
            Assert.fail("Incorrect password allowed login");
        }
    }


}

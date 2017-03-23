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

import javax.security.auth.login.FailedLoginException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;

/**
 *
 */
public class ManagerModeModelTests {

    /*
     * Tests needing this are ignored by default. If you need to test this in a
     * development environment then comment out the @ignore annotation on the
     * tests and insert the production password here.
     * 
     * Make sure not to commit those changes to git!
     */
    private static final String CORRECT_PASSWORD = "Insert the correct password here";

    private ManagerModeModel model;

    private Writable<String> mockWritable = Mockito.mock(Writable.class);

    private ClosableObservable<Boolean> mockObservable = Mockito.mock(ClosableObservable.class);

    private ForwardingObservable<Boolean> forwardingObservable = new ForwardingObservable<>(mockObservable);

    @Before
    public void setUp() {
        Mockito.when(mockWritable.canWrite()).thenReturn(true);
        Mockito.when(mockObservable.getValue()).thenReturn(true);

        model = ManagerModeModel.getTestableInstance(mockWritable, forwardingObservable);
    }

    @Test
    public void given_a_blank_password_then_login_fails() {

        // Act
        try {
            model.login("");
            Assert.fail("Didn't throw an exception");
        } catch (FailedLoginException e) {
            // Assert: this is the only path that doesn't explicitly fail.
        } catch (Exception e) {
            Assert.fail("Threw an unexpected exception: " + e.toString());
        }
    }

    @Test
    public void given_a_null_password_then_login_fails() {

        // Act
        try {
            model.login((String) null);
            Assert.fail("Didn't throw an exception");
        } catch (FailedLoginException e) {
            // Assert: this is the only path that doesn't explicitly fail.
        } catch (Exception e) {
            Assert.fail("Threw an unexpected exception: " + e.toString());
        }
    }

    @Test
    public void given_an_incorrect_password_then_login_fails() {

        // Act
        try {
            model.login("This is definitely not the correct password");
            Assert.fail("Didn't throw an exception");
        } catch (FailedLoginException e) {
            // Assert: this is the only path that doesn't explicitly fail.
        } catch (Exception e) {
            Assert.fail("Threw an unexpected exception: " + e.toString());
        }
    }

    /*
     * Test is ignored by default. If you need to test this in a development
     * environment then comment out the @ignore annotation and insert the
     * production password in the constant at the top of the file.
     * 
     * Make sure not to commit those changes to git!
     */
    @Ignore
    @Test
    public void given_a_correct_password_then_login_doesnt_throw_an_exception() {
        // Act
        try {
            model.login(CORRECT_PASSWORD);
        } catch (FailedLoginException e) {
            Assert.fail("Login didn't succeed: " + e.toString());
        } catch (Exception e) {
            Assert.fail("Threw an unexpected exception: " + e.toString());
        }

        // Assert: passes if no exceptions were thrown.
    }

    /*
     * Test is ignored by default. If you need to test this in a development
     * environment then comment out the @ignore annotation and insert the
     * production password in the constant at the top of the file.
     * 
     * Make sure not to commit those changes to git!
     */
    @Ignore
    @Test
    public void given_a_correct_password_then_writable_is_updated_with_one() {
        // Act
        try {
            model.login(CORRECT_PASSWORD);
        } catch (FailedLoginException e) {
            Assert.fail("Login didn't succeed: " + e.toString());
        }

        Mockito.verify(mockWritable, Mockito.times(1)).write("1");
    }

    @Test
    public void given_a_logout_command_then_the_writable_is_updated_with_zero() {
        // Act
        model.logout();

        // Assert
        Mockito.verify(mockWritable, Mockito.times(1)).write("0");
    }

    @Test
    public void given_a_failed_login_then_the_writable_is_not_updated() {
        // Act
        try {
            model.login("This is definitely not the correct password");
        } catch (FailedLoginException e) {
            // Expected.
        }

        // Assert
        Mockito.verify(mockWritable, Mockito.times(0)).write(Matchers.anyString());
    }

}

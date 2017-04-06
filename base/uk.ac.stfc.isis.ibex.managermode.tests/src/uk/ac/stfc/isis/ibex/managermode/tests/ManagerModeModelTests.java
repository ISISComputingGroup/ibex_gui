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

import javax.security.auth.login.FailedLoginException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;
import uk.ac.stfc.isis.ibex.managermode.PasswordHasher;

/**
 *
 */
public class ManagerModeModelTests {

    private ManagerModeModel model;

    @SuppressWarnings("unchecked")
    private Writable<String> mockWritable = Mockito.mock(Writable.class);

    @SuppressWarnings("unchecked")
    private ClosableObservable<Boolean> mockObservable = Mockito.mock(ClosableObservable.class);

    private ForwardingObservable<Boolean> forwardingObservable = new ForwardingObservable<>(mockObservable);

    /**
     * Always returns true when given any String as a password.
     */
    private PasswordHasher correctPasswordHasherMock = Mockito.mock(PasswordHasher.class);

    /**
     * Always returns false when given any String as a password.
     */
    private PasswordHasher incorrectPasswordHasherMock = Mockito.mock(PasswordHasher.class);

    @Before
    public void setUp() throws InvalidKeySpecException, NoSuchAlgorithmException {
        Mockito.when(mockWritable.canWrite()).thenReturn(true);
        Mockito.when(mockObservable.getValue()).thenReturn(true);

        Mockito.when(correctPasswordHasherMock.isCorrectPassword(Matchers.anyString())).thenReturn(true);
        Mockito.when(incorrectPasswordHasherMock.isCorrectPassword(Matchers.anyString())).thenReturn(false);

    }

    @Test
    public void given_an_incorrect_password_then_login_fails() {

        // Arrange
        model = ManagerModeModel.getTestableInstance(incorrectPasswordHasherMock, mockWritable, forwardingObservable);

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

    @Test
    public void given_a_correct_password_then_login_doesnt_throw_an_exception() {

        // Arrange
        model = ManagerModeModel.getTestableInstance(correctPasswordHasherMock, mockWritable, forwardingObservable);

        // Act
        try {
            model.login("");
        } catch (FailedLoginException e) {
            Assert.fail("Login didn't succeed: " + e.toString());
        } catch (Exception e) {
            Assert.fail("Threw an unexpected exception: " + e.toString());
        }

        // Assert: passes if no exceptions were thrown.
    }

    @Test
    public void given_a_correct_password_then_writable_is_updated_with_one() {

        // Arrange
        model = ManagerModeModel.getTestableInstance(correctPasswordHasherMock, mockWritable, forwardingObservable);

        // Act
        try {
            model.login("");
        } catch (FailedLoginException e) {
            Assert.fail("Login didn't succeed: " + e.toString());
        }

        // Assert
        Mockito.verify(mockWritable, Mockito.times(1)).write("1");
    }

    @Test
    public void given_a_logout_command_then_the_writable_is_updated_with_zero() {

        // Arrange
        model = ManagerModeModel.getTestableInstance(correctPasswordHasherMock, mockWritable, forwardingObservable);

        // Act
        model.logout();

        // Assert
        Mockito.verify(mockWritable, Mockito.times(1)).write("0");
    }

    @Test
    public void given_a_failed_login_then_the_writable_is_not_updated() {

        // Arrange
        model = ManagerModeModel.getTestableInstance(incorrectPasswordHasherMock, mockWritable, forwardingObservable);

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

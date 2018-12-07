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
package uk.ac.stfc.isis.ibex.managermode;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.security.auth.login.FailedLoginException;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.InstrumentSwitchers;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Model for the manager mode.
 */
public final class ManagerModeModel extends ModelObject implements IManagerModeModel {

    private final PasswordHasher passwordHasher;

    /**
     * The password that was entered (as plain text).
     */
    private String password;

    private boolean passwordValid;

    private WritableFactory writeFactory;

    private Writable<String> managerPvWritable;

    private Boolean inManagerMode;

    private ManagerModeObservable managerModePv;

    /**
     * Constructor using the default password.
     */
    public ManagerModeModel() {
        setupPV();
        addObserver();
        passwordHasher = new PasswordHasher();
    }

    /**
     * Constructor only used for testing.
     */
    public ManagerModeModel(PasswordHasher hasher, Writable<String> writable,
            ForwardingObservable<Boolean> observable) {
        managerPvWritable = writable;
        managerModePv = new ManagerModeObservable(observable);
        passwordHasher = hasher;
    }

    /**
     * Sets the entered password.
     * 
     * @param password
     *            the password in plain text
     * @throws FailedLoginException
     *             if the password was incorrect
     */
    public void authenticate(String password) throws FailedLoginException {
        this.password = password;
        validate();
        try {
            updatePV();
        } catch (IOException e) {
            throw new FailedLoginException(e.getMessage());
        }
    }

    /**
     * Exits manager mode.
     */
    public void deauthenticate() {
        this.password = "";
        this.passwordValid = false;
        try {
            updatePV();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update PV (caused by: " + e.getMessage() + ")", e);
        }
    }

    /**
     * Validates the current password against the known password. Throws an
     * exception if the login was incorrect.
     * 
     * @throws FailedLoginException
     *             if the login failed
     */
    private void validate() throws FailedLoginException {

        try {
            passwordValid = passwordHasher.isCorrectPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new FailedLoginException("Login failed: internal error\n" + e.getMessage());
        }

        if (!passwordValid) {
            throw new FailedLoginException("Login failed: Password was incorrect.");
        }
    }

    private void setupPV() {
        writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        managerPvWritable =
                writeFactory.getSwitchableWritable(new StringChannel(), InstrumentUtils.addPrefix("CS:MANAGER"));

        managerModePv = new ManagerModeObservable();

    }

    private void addObserver() {
        new ManagerModeObserver(managerModePv.observable) {

            @Override
            protected void setManagerMode(Boolean value) {
                inManagerMode = value;
            }

            @Override
            protected void setUnknown() {
                inManagerMode = null;
            }

        };
    }

    private void updatePV() throws IOException {
        if (passwordValid) {
            managerPvWritable.write("1");
        } else {
            managerPvWritable.write("0");
        }
    }

    /**
     * Returns true if the instrument is in manager mode, false otherwise.
     * 
     * @return true if the instrument is in manager mode, false otherwise
     * @throws ManagerModePvNotConnectedException if the manager mode pv is not connected
     */
    public boolean isAuthenticated() throws IOException {
        if (inManagerMode == null) {
            try {
                // PV doesn't have time to connect before this is called the
                // first time, so wait for half a second.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //Do nothing.
            }
            
            if (inManagerMode == null) {
                throw new IOException("Manager mode PV not connected. Please try again in a few moments.");
            }
        }
        
        return inManagerMode.booleanValue();
    }

}

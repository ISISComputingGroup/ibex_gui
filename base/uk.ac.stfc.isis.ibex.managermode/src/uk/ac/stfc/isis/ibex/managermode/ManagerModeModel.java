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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
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
public final class ManagerModeModel extends ModelObject {

    private static ManagerModeModel instance;

    /**
     * Iterations of the PBKDF2WithHmacSHA512 hashing algorithm. This is an
     * arbitrary "slowdown" factor to harden the hash againt brute force
     * attacks.
     */
    private static final int ITERATIONS = 10000;

    /**
     * The key length used by the PBKDF2WithHmacSHA512 hashing algorithm.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * Salt used by the PBKDF2WithHmacSHA512 hashing algorithm. This should be
     * constant but random data, which helps against precomputed hash table
     * attacks.
     */
    private static final String SALT =
            "o0iLqI7Poq8KEWdYmaAS5ZsPRJSrgO27eusvDXZtWpTYnKiRW28Naf97c6KgHUHiHA7HdQ2jjPPEI7By";

    /**
     * The expected hash if the password is correct.
     * 
     * Changing any of iterations, key length or salt will cause this value to
     * change!
     */
    private static final String EXPECTED = "vhvn76Ca6TP+fVCgRqlCgvN9SCoHGdrNwxw/6WU/ks4=";

    /**
     * The password that was entered (as plain text).
     */
    private String password;

    private boolean passwordValid;

    private WritableFactory writeFactory;

    private Writable<String> managerPvWritable;

    private Boolean inManagerMode;

    private ManagerModeObservable managerModePv;

    private ManagerModeModel() {
        setupPV();
        addObserver();
    }

    private ManagerModeModel(Writable<String> writable, ForwardingObservable<Boolean> observable) {
        managerPvWritable = writable;
        managerModePv = new ManagerModeObservable(observable);
    }

    /**
     * Gets the singleton instance of this class.
     * 
     * @return the singleton instance of this class
     */
    public static ManagerModeModel getInstance() {
        if (instance == null) {
            instance = new ManagerModeModel();
        }
        return instance;
    }

    /**
     * Used in unit tests to make sure there is a consistent start state for all
     * tests.
     */
    public static ManagerModeModel getTestableInstance(Writable<String> writable,
            ForwardingObservable<Boolean> observable) {
        instance = new ManagerModeModel(writable, observable);
        return instance;
    }

    /**
     * Sets the entered password.
     * 
     * @param password
     *            the password in plain text
     * @throws FailedLoginException
     *             if the password was incorrect
     */
    public void login(String password) throws FailedLoginException {
        this.password = password;
        validate();
        sendToPV();
    }

    /**
     * Exits manager mode.
     */
    public void logout() {
        this.password = "";
        this.passwordValid = false;
        sendToPV();
    }

    /**
     * Validates the current password against the known password.
     * 
     * @return true if the password matches, false otherwise
     */
    private boolean validate() throws FailedLoginException {

        if (password == null) {
            throw new FailedLoginException("Login failed. Password was null.");
        }

        String hashedPassword = getBase64StringFromByteArray(getHashedPassword());
        if (hashedPassword.equals(EXPECTED)) {
            passwordValid = true;
        } else {
            passwordValid = false;
            throw new FailedLoginException("Login failed.");
        }
        return passwordValid;
    }

    /**
     * Hashes the password using the PBKDF2 algorithm.
     * 
     * @return the hashed password
     */
    private byte[] getHashedPassword() {

        char[] password = this.password.toCharArray();

        try {
            PBEKeySpec keySpecification = new PBEKeySpec(password, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
            SecretKey key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(keySpecification);

            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getGlobal().severe("PBKDF2WithHmacSHA512 algorithm not found!");
            throw new RuntimeException(e);
        }

    }

    /**
     * Converts an array of bytes to a base64 string. This is useful to get rid
     * of some of the "nasty" characters that come out of the hashing algorithm.
     * 
     * @param input
     *            an input byte array
     * @return an output string
     */
    private String getBase64StringFromByteArray(byte[] input) {
        return new String(Base64.getEncoder().encode(input));
    }

    private void setupPV() {
        writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH, InstrumentSwitchers.getDefault());
        managerPvWritable =
                writeFactory.getSwitchableWritable(new StringChannel(), InstrumentUtils.addPrefix("CS:MANAGER"));

        managerModePv = new ManagerModeObservable();

    }

    public ManagerModeObservable getManagerModeObservable() {
        return managerModePv;
    }

    private void addObserver() {
        new ManagerModeObserver(managerModePv.observable, managerModePv.self) {

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

    private void sendToPV() {
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
     * @throws IllegalStateException
     *             if the PV isn't connected
     */
    public boolean isInManagerMode() {
        if (inManagerMode == null) {
            throw new IllegalStateException("PV not connected. Please try again in a few moments.");
        }
        return inManagerMode.booleanValue();
    }

}

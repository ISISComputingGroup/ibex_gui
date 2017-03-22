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
package uk.ac.stfc.isis.ibex.ui.mainmenu.managermode;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 *
 */
public class ManagerModeModel extends ModelObject {

    private static ManagerModeModel instance;

    private ManagerModeModel() {
        setupPV();
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
    private Writable<String> titleSP;

    /**
     * Sets the entered password.
     * 
     * @param password
     *            the password in plain text
     */
    public void setPassword(String password) {
        this.password = password;
        validate();
        sendToPV();
    }

    /**
     * Validates the current password against the known password.
     * 
     * @return true if the password matches, false otherwise
     */
    private void validate() {
        String hashedPassword = getBase64StringFromByteArray(getHashedPassword());
        if (hashedPassword.equals(EXPECTED)) {
            passwordValid = true;
        } else {
            passwordValid = false;
        }
    }

    /**
     * True if the password is valid; false otherwise.
     * 
     * @return true if the password is valid; false otherwise
     */
    public boolean isPasswordValid() {
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
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpecification = new PBEKeySpec(password, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
            SecretKey key = keyFactory.generateSecret(keySpecification);

            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getGlobal().severe("PBKDF2WithHmacSHA512 algorithm not found!");
            return new byte[0];
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
        // TODO: Think carefully about how to handle instrument switching.
        writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);
        titleSP =
                writeFactory.getSwitchableWritable(new StringChannel(), InstrumentUtils.addPrefix("CS:MANAGER"));
    }

    private void sendToPV() {
        if (isPasswordValid()) {
            titleSP.write("1");
        } else {
            titleSP.write("0");
        }
    }

}

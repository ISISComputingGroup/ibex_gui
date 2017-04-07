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

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * This class compares a password against a known hash to determine whether they
 * match.
 */
public class PasswordHasher {

    /**
     * Using PBKDF2WithHmacSHA512 would be ideal, but it's not available in all
     * java 1.7 environments, so use PBKDF2WithHmacSHA1 instead.
     */
    private static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * Iterations of the hashing algorithm. This is an arbitrary "slowdown"
     * factor to harden the hash againt brute force attacks.
     */
    private static final int ITERATIONS = 10000;

    /**
     * The key length used by the hashing algorithm.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * Salt used by the hashing algorithm. This should be constant but random
     * data, which helps against precomputed hash table attacks.
     */
    private static final String SALT =
            "o0iLqI7Poq8KEWdYmaAS5ZsPRJSrgO27eusvDXZtWpTYnKiRW28Naf97c6KgHUHiHA7HdQ2jjPPEI7By";

    /**
     * The expected hash if the password is correct.
     * 
     * Changing any of algorithm, iterations, key length or salt will cause this
     * value to change!
     */
    private String expectedHash = "uVpfg6gqCDPvluvcyQGVm06aN/t435TbfX7fnEDU0+4=";

    /**
     * Creates a password hasher that expects the default password.
     */
    public PasswordHasher() {
    }

    /**
     * Creates a password hasher that expects a specific hash, used for unit tests.
     * 
     * @param expectedHash
     *            the expected hash
     */
    public PasswordHasher(String expectedHash) {
        this.expectedHash = expectedHash;
    }

    /**
     * Hashes the password using the PBKDF2 algorithm.
     * 
     * @return the hashed password
     * @throws NoSuchAlgorithmException
     *             if the algorithm is not available on this machine
     * @throws InvalidKeySpecException
     *             if the key specification was invalid
     */
    private byte[] getHashedPassword(String passwordString) throws InvalidKeySpecException, NoSuchAlgorithmException {

        if (passwordString == null) {
            passwordString = "";
        }

        char[] password = passwordString.toCharArray();

        PBEKeySpec keySpecification = new PBEKeySpec(password, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKey key = SecretKeyFactory.getInstance(HASHING_ALGORITHM).generateSecret(keySpecification);
        return key.getEncoded();

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

    private String hash(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return getBase64StringFromByteArray(getHashedPassword(password));
    }

    /**
     * Checks if a given password is correct.
     * 
     * @param password
     *            the password to check
     * @return true if the password is correct, false otherwise
     * @throws InvalidKeySpecException
     *             if the parameters of this class are invalid
     * @throws NoSuchAlgorithmException
     *             if there is no implementation of the requested algorithm
     *             available
     */
    public boolean isCorrectPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println(hash(password));
        return hash(password).equals(expectedHash);
    }
}

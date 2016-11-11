
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
package uk.ac.stfc.isis.ibex.instrument.internal;

import java.security.InvalidParameterException;

/**
 * The Class CRC8 which holds a CRC value.
 */
public class CRC8 {

    /** CRC value. */
    private byte crc;

    /** generator for the CRC. */
    private static final byte GENERATOR = 0x07;

    /** size of the crc. */
    private static final int CRC_SIZE = 8;

    /**
     * Instantiates a new CRC8.
     *
     * @param crc the CRC8 value
     */
    public CRC8(byte crc) {
        this.crc = crc;
    }

    /**
     * Create a CRC8 from a string.
     *
     * @param stringValue the string value
     * @return the CRC8
     */
    public static CRC8 fromString(String stringValue) {

        if (stringValue == null || stringValue.isEmpty()) {
            throw new InvalidParameterException("Invalid string for CRC8 (empty or null)");
        }

        byte crc = 0; /* start with 0 so first byte can be 'xored' in */

        for (byte currentByte : stringValue.getBytes()) {
            crc ^= currentByte; /* XOR-in the next input byte */

            for (int i = 0; i < CRC_SIZE; i++) {
                if ((crc >> (CRC_SIZE - 1)) != 0) {
                    crc = (byte) ((crc << 1) ^ GENERATOR);
                } else {
                    crc <<= 1;
                }
            }
        }

        return new CRC8(crc);
    }

    /**
     * Gets the CR c8.
     *
     * @return the CR c8
     */
    @Override
    public String toString() {
        return String.format("%02X", crc);
    }

}

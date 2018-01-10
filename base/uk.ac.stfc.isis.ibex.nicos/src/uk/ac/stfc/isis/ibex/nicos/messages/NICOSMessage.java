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
package uk.ac.stfc.isis.ibex.nicos.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;

/**
 * A message that can be serialised and sent to NICOS.
 * 
 * @param <T>
 *            The type of parameters to send.
 */
public abstract class NICOSMessage<T> {
    protected String command = "";
    protected List<T> parameters = new ArrayList<>();
    
    /**
     * Converts the message into a list of messages to send NICOS.
     * 
     * @return The list of messages to send to NICOS.
     * @throws ConversionException
     *             thrown when the conversion cannot take place.
     */
    public List<String> getMulti() throws ConversionException {
        @SuppressWarnings("rawtypes")
        JsonSerialisingConverter<List> serialiser = new JsonSerialisingConverter<>(List.class);
        return Arrays.asList(command, "", serialiser.convert(parameters));
    }

    /**
     * Parses the response from NICOS once this command has been sent.
     * 
     * @param response
     *            The string response from NICOS.
     * 
     * @return The response from NICOS.
     * @throws ConversionException
     *             Thrown when the response from NICOS is not as expected.
     */
    public abstract ReceiveMessage parseResponse(String response) throws ConversionException;
}

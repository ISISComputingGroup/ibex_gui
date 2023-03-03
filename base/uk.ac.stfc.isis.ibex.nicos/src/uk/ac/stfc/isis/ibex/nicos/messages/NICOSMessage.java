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
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQWrapper;

/**
 * A message that can be serialised and sent to NICOS.
 * 
 * @param <TSEND>
 *            The type of parameters to send.
 * @param <TRESP>
 * 			  The type of the response.
 */
public abstract class NICOSMessage<TSEND, TRESP> {
	
	/**
	 * The command to be sent as this message.
	 */
    protected String command = "";
    
    /**
     * The parameters to send alongside the command.
     */
    protected List<TSEND> parameters = new ArrayList<>();
    
    /**
     * The status of the response to this message.
     */
    protected String responseStatus;
    
    /**
     * The response content of this message.
     */
    protected String response;
    
    /**
     * Converts the message into a list of messages to send NICOS.
     * 
     * @return The list of messages to send to NICOS.
     * @throws ConversionException
     *             thrown when the conversion cannot take place.
     */
    public List<String> getMulti() throws ConversionException {
		JsonSerialisingConverter<List<TSEND>> serialiser = new JsonSerialisingConverter<List<TSEND>>(List.class);
        return Arrays.asList(command, "", serialiser.apply(parameters));
    }

    /**
     * Parses the response from NICOS once this command has been sent.
     * 
     * @param response
     *            The response from NICOS.
     * 
     * @return The response from NICOS.
     * @throws ConversionException
     *             Thrown when the response from NICOS is not as expected.
     */
    public abstract TRESP parseResponse(String response) throws ConversionException;
    
    /**
     * Receive the response from this message using the given ZMQWrapper.
     * 
     * @param zmq The wrapper to receive the message with.
     */
    public void receiveResponse(ZMQWrapper zmq) {
    	responseStatus = zmq.receiveString();
    	// NICOS protocol leaves the second package empty for future expansion
        // so read and throw away.
    	zmq.receiveString();
    	response = zmq.receiveString();
    }
    
    /**
     * Get the response filled out by calling receiveResponse.
     * 
     * @return The content of the response to this message.
     */
    public String getResponse() {
    	return response;
    }
    
    /**
     * Get the response status filled out by calling receiveResponse.
     * 
     * @return The status of the response to this message.
     */
    public String getResponseStatus() {
    	return responseStatus;
    }
}

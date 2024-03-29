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


import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQWrapper;

/**
 * Command that gets banner information from NICOS.
 */
public class GetBanner extends NICOSMessage<String, ReceiveBannerMessage> {
	

    /**
     * Create the get banner command.
     */
    public GetBanner() {
        this.command = "getbanner";
    }

    @Override
    public ReceiveBannerMessage parseResponse(String response) throws ConversionException {
        return new JsonDeserialisingConverter<>(ReceiveBannerMessage.class).apply(response);
    }
    
    /**
     * Receive the response to this GetBanner message using the given ZMQWrapper
     * and store that response and responseStatus in the messages fields.
     * 
     * @param zmq The wrapper to receive the string with.
     */
    public void receiveResponse(ZMQWrapper zmq) {
    	responseStatus = zmq.receiveString();
    	zmq.receiveString();
    	response = zmq.receiveString();
    }
    

}

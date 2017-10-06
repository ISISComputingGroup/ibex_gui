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
package uk.ac.stfc.isis.ibex.nicos;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLoginMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveStringMessage;

/**
 * The class parses the data coming out of ActiveMQ into a NicosMessage.
 */
public class NicosMessageParser {

    private static final Logger LOG = IsisLog.getLogger(NicosMessageParser.class);

    private static final JsonDeserialisingConverter<ReceiveStringMessage> STRING_PAYLOAD_CONVERTER =
            new JsonDeserialisingConverter<>(ReceiveStringMessage.class);
    private static final JsonDeserialisingConverter<ReceiveLoginMessage> LOGIN_PAYLOAD_CONVERTER =
            new JsonDeserialisingConverter<>(ReceiveLoginMessage.class);

//    /**
//     * Convert the ActiveMQ message to a Nicos message.
//     * 
//     * @param rawMessage
//     *            The raw message from ActiveMQ.
//     * @return A dummy message.
//     */
//    protected ReceiveMessage parseMessage(String rawMessage) {
//        ReceiveMessage nicosMessage;
//        String rawMessageText = rawMessage.getText();
//        try {
//            nicosMessage = convert(rawMessageText);
//            nicosMessage.setMessageId(rawMessage.getMessageID());            
//        } catch (ConversionException e) {
//            LOG.error("Error converting message from script server. Text was '" + rawMessageText + "'", e);
//            nicosMessage = new ReceiveErrorMessage("Error converting message from the script server.",
//                    rawMessage.getMessageID(), false);
//        } catch (NullPointerException e) {
//            LOG.error("Error converting message from script server, text was null.", e);
//            nicosMessage =
//                    new ReceiveStringMessage("Blank message sent from the script server.", rawMessage.getMessageID(),
//                            false);
//        }
//        
//        return nicosMessage;
//    }

    /**
     * Convert a message to a received message using one of the parsers
     * 
     * @param rawMessageText
     *            text to convert
     * @return message
     * @throws ConversionException
     *             if the message can not be converted
     */
    private ReceiveMessage convert(String rawMessageText) throws ConversionException {
        
        try {
            return STRING_PAYLOAD_CONVERTER.convert(rawMessageText);
        } catch (ConversionException e) {
            // carry on and try the next converter
        }
        return LOGIN_PAYLOAD_CONVERTER.convert(rawMessageText);
    }

}

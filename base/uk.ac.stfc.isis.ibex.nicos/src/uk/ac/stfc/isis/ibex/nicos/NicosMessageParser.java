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

import uk.ac.stfc.isis.ibex.activemq.message.MessageDetails;
import uk.ac.stfc.isis.ibex.activemq.message.MessageParser;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The class parses the data coming out of ActiveMQ into a NicosMessage.
 */
public class NicosMessageParser extends MessageParser<NicosMessage> {

    private static final Logger LOG = IsisLog.getLogger(NicosMessageParser.class);

    private static final JsonDeserialisingConverter<NicosMessage> CONVERTER =
            new JsonDeserialisingConverter<>(NicosMessage.class);

    /**
     * Convert the ActiveMQ message to a Nicos message.
     * 
     * @param rawMessage
     *            The raw message from ActiveMQ.
     * @return A dummy message.
     */
    @Override
    protected NicosMessage parseMessage(MessageDetails rawMessage) {
         

        NicosMessage nicosMessage;
        String rawMessageText = rawMessage.getText();
        try {
            nicosMessage = CONVERTER.convert(rawMessageText);
            nicosMessage.setMessageId(rawMessage.getMessageID());            
        } catch (ConversionException e) {
            LOG.error("Error converting message from script server. Text was '" + rawMessageText + "'", e);
            nicosMessage = new NicosMessage("Error converting message from the script server.",
                    rawMessage.getMessageID(), false);
        } catch (NullPointerException e) {
            LOG.error("Error converting message from script server, text was null.", e);
            nicosMessage =
                    new NicosMessage("Blank message sent from the script server.", rawMessage.getMessageID(), false);
        }
        
        return nicosMessage;
    }

}

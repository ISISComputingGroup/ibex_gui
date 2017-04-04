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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.activemq.message.MessageDetails;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveErrorMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveLoginMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveStringMessage;

public class NicosMessageParserTest {

    private NicosMessageParser parser;

    @Before
    public void setUp() {
        parser = new NicosMessageParser();
    }

    @Test
    public void GIVEN_job_submitted_message_WHEN_convert_THEN_get_message_with_string_payload() {
        String expected = "expected answer";
        String jobSubmitted = "{\"payload\": \"" + expected + "\", \"success\": true}";

        ReceiveMessage result = parser.parseMessage(new MessageDetails(jobSubmitted, "message id"));

        assertThat(((ReceiveStringMessage) result).getPayload(), is(expected));
    }

    @Test
    public void GIVEN_log_in_WHEN_convert_THEN_get_message_with_user_level() {
        int expected = 3;
        
        String login = "{\"payload\": {\"user_level\": " + expected + "}, \"success\": True}";

        ReceiveMessage result = parser.parseMessage(new MessageDetails(login, "message id"));

        assertThat(((ReceiveLoginMessage) result).getUserLevel(), is(expected));
    }

    @Test
    public void GIVEN_inital_message_WHEN_convert_THEN_error() {

        String login =
                "{\"payload\": {\"user_level\": {\"user_level2\": 3}}, \"success\": True}";

        ReceiveMessage result = parser.parseMessage(new MessageDetails(login, "message id"));

        assertThat(((ReceiveErrorMessage) result).isSuccess(), is(false));
        assertThat(((ReceiveErrorMessage) result).getErrorMessage(),
                containsString("Error converting message from the script server."));
    }

    @Test
    public void GIVEN_empty_message_WHEN_convert_THEN_error() {
        String login = "";

        ReceiveMessage result = parser.parseMessage(new MessageDetails(login, "message id"));

        assertThat(result.getMessage(), containsString("Blank"));
    }
}

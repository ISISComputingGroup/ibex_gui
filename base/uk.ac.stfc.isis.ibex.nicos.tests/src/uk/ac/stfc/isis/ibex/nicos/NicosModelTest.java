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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import uk.ac.stfc.isis.ibex.activemq.SendMessageDetails;
import uk.ac.stfc.isis.ibex.activemq.SendReceiveSession;
import uk.ac.stfc.isis.ibex.activemq.message.MessageDetails;

public class NicosModelTest {

    /**
     * 
     */
    private static final String SUBMIT_FAILLED_ERROR_MESSGAGE = "a script is running";
    private static final String SCRIPT = "#valid python script";
    private static final String JOB_SUBMITTED = "{\"payload\": 2, \"success\": true}";
    private static final String JOB_SUBMIT_FAILED =
            "{\"payload\": \"" + SUBMIT_FAILLED_ERROR_MESSGAGE + "\", \"success\": false}";

    private SendReceiveSession sendReceiveSession = mock(SendReceiveSession.class);
    private NicosModel model;
    private NicosMessageParser messageParser;
    private MessageConsumer consumer = mock(MessageConsumer.class);

    /**
     * @return the stub after the send receive session has logged in
     */
    private OngoingStubbing<SendMessageDetails> loginedInSessionSendMessage() {

        return when(sendReceiveSession.sendMessage(any()));

    }

    @Before
    public void setUp() {
        model = new NicosModel(sendReceiveSession);
        ArgumentCaptor<NicosMessageParser> captor = ArgumentCaptor.forClass(NicosMessageParser.class);
        verify(sendReceiveSession).addMessageParser(captor.capture());
        messageParser = captor.getValue();
        messageParser.setActiveMQConsumer(consumer);
    }

    @Test
    public void GIVEN_no_connection_WHEN_get_status_THEN_status_is_unconnected() {

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.NONE));
    }

    @Test
    public void GIVEN_connection_drop_WHEN_send_script_THEN_status_is_send_failure() {
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendFail("fail", ""));
        model.sendScript(SCRIPT);

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.SEND_ERROR));
    }

    @Test
    public void GIVEN_connection_WHEN_send_script_THEN_status_is_sending() {
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess("messageId"));
        model.sendScript(SCRIPT);

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.SENDING));
    }

    @Test
    public void GIVEN_connection_WHEN_send_script_THEN_message_sender_recieves_script() {
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess("messageId"));

        model.sendScript(SCRIPT);

        verify(sendReceiveSession).sendMessage(contains(SCRIPT));
    }

    @Test
    public void GIVEN_script_sent_WHEN_recieve_acknoledgement_THEN_script_sctatus_is_sent() throws JMSException {
        String messageId = "messageId";
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        model.sendScript(SCRIPT);
        modelRecieveNewMessage(JOB_SUBMITTED, messageId);

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.SENT));

    }

    @Test
    public void
            GIVEN_script_sent_WHEN_not_recieved_acknoledgement_but_different_uncorrelated_message_THEN_script_sctatus_is_sending()
                    throws JMSException {
        String messageId = "messageId";
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        model.sendScript(SCRIPT);
        modelRecieveNewMessage(JOB_SUBMITTED, "not messageId");

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.SENDING));

    }

    @Test
    public void GIVEN_script_sent_WHEN_recieved_failure_acknoledgement_THEN_script_sctatus_is_fail()
            throws JMSException {
        String messageId = "messageId";
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        model.sendScript(SCRIPT);
        modelRecieveNewMessage(JOB_SUBMIT_FAILED, messageId);

        ScriptSendStatus result = model.getScriptSendStatus();

        assertThat("Script status", result, is(ScriptSendStatus.SEND_ERROR));

    }

    @Test
    public void GIVEN_script_sent_WHEN_recieved_failure_acknoledgement_THEN_error_message_is_updated()
            throws JMSException {
        String messageId = "messageId";
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        model.sendScript(SCRIPT);
        modelRecieveNewMessage(JOB_SUBMIT_FAILED, messageId);

        String result = model.getScriptSendErrorMessage();

        assertThat("Error message", result, is(SUBMIT_FAILLED_ERROR_MESSGAGE));

    }

    /**
     * @param jobSubmitFailed
     * @return
     */
    private void modelRecieveNewMessage(String rawMessage, String messageId) {
        NicosMessage nicosMessage = messageParser.parseMessage(new MessageDetails(rawMessage, messageId));
        model.newMessage(nicosMessage);
    }

    @Test
    public void GIVEN_connection_WHEN_send_script_THEN_message_sender_recieves_script_as_queue_command() {
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess("messageId"));

        model.sendScript(SCRIPT);

        verify(sendReceiveSession).sendMessage(contains(NicosModel.QUEUE_SCRIPT_COMMAND_TEMPLATE.substring(0, 20)));
    }

    @Test
    public void GIVEN_connection_WHEN_send_script_that_needs_escaping_THEN_message_contains_escaped_script() {
        loginedInSessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess("messageId"));
        String original = "backslash \\ formfeed \f newline \n cr \r tab \t quotes \" backspace \b";
        String escaped = "backslash \\\\ formfeed \\f newline \\n cr \\r tab \\t quotes \\\" backspace \\b";

        model.sendScript(original);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(sendReceiveSession, times(2)).sendMessage(argument.capture());
        assertThat(argument.getValue(), containsString(escaped));
    }
}

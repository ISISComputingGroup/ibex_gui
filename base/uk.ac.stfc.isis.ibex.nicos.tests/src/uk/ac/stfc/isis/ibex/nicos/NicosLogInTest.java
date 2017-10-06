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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import uk.ac.stfc.isis.ibex.activemq.SendReceiveSession;
import uk.ac.stfc.isis.ibex.activemq.message.MessageDetails;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SendMessageDetails;

public class NicosLogInTest {

    /**
     * 
     */
    private static final String LOGIN_FAIL_ERROR_MESSAGE = "credentials not accepted";
    private static final String LOGIN_SUCCESS = "{\"payload\": {\"user_level\": 20}, \"success\": True}";
    private static final String LOGIN_FAIL = "{\"payload\": \"" + LOGIN_FAIL_ERROR_MESSAGE + "\", \"success\": False}";

    private SendReceiveSession sendReceiveSession = mock(SendReceiveSession.class);
    private NicosModel model;
    private NicosMessageParser messageParser;
    private PropertyChangeListener connectionPropertChange;
    private PropertyChangeListener connectionErrorPropertChange;

    /**
     * @return the stub after the send receive session has logged in
     */
    private OngoingStubbing<SendMessageDetails> sessionSendMessage() {

        return when(sendReceiveSession.sendMessage(anyString()));

    }

    @Before
    public void setUp() {
        sessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess("messageId"));

        model = new NicosModel(sendReceiveSession);
        ArgumentCaptor<NicosMessageParser> captor = ArgumentCaptor.forClass(NicosMessageParser.class);
        verify(sendReceiveSession).addMessageParser(captor.capture());
        messageParser = captor.getValue();
        
        ArgumentCaptor<PropertyChangeListener> propertyChangeCaptor = ArgumentCaptor.forClass(PropertyChangeListener.class);
        verify(sendReceiveSession).addPropertyChangeListener(eq("connection"),
                propertyChangeCaptor.capture());
        connectionPropertChange = propertyChangeCaptor.getValue();

        verify(sendReceiveSession).addPropertyChangeListener(eq("connectionError"), propertyChangeCaptor.capture());
        connectionErrorPropertChange = propertyChangeCaptor.getValue();
    }

    @Test
    public void GIVEN_no_connection_WHEN_get_connection_status_THEN_status_is_unconnected() {

        ConnectionStatus result = model.getConnectionStatus();

        assertThat("Connection status", result, is(ConnectionStatus.DISCONNECTED));
    }

    @Test
    public void GIVEN_no_connection_WHEN_connection_status_changes_THEN_status_is_connecting() {
        
        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.FALSE, Boolean.TRUE));

        ConnectionStatus result = model.getConnectionStatus();

        assertThat("Connection status", result, is(ConnectionStatus.CONNECTING));
    }

    @Test
    public void GIVEN_no_connection_error_WHEN_connection_error_from_session_THEN_error_is_set() {

        String expectedError = "error expected";
        connectionErrorPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connectionError", "", expectedError));

        String result = model.getConnectionErrorMessage();

        assertThat("Connection error", result, is(expectedError));
    }

    @Test
    public void GIVEN_connecting_WHEN_disconnect_THEN_status_is_disconnected() {
        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.FALSE, Boolean.TRUE));

        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.TRUE, Boolean.FALSE));
        ConnectionStatus result = model.getConnectionStatus();

        assertThat("Connection status", result, is(ConnectionStatus.DISCONNECTED));
    }

    @Test
    public void GIVEN_no_connection_WHEN_get_connect_and_login_message_return_success_THEN_status_is_connected() {
        String messageId = "message id";
        sessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.FALSE, Boolean.TRUE));
        ReceiveMessage nicosMessage = messageParser.parseMessage(new MessageDetails(LOGIN_SUCCESS, messageId));
        model.newMessage(nicosMessage);

        ConnectionStatus result = model.getConnectionStatus();
        String message = model.getConnectionErrorMessage();

        assertThat("Connection status", result, is(ConnectionStatus.CONNECTED));
        assertThat("Connection error message", message, is(""));
    }

    @Test
    public void GIVEN_connection_WHEN_can_not_send_script_THEN_status_is_error() {
        String messageId = "message id";
        String expectedError = "error message";
        sessionSendMessage().thenReturn(SendMessageDetails.createSendFail(expectedError, messageId));
        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.FALSE, Boolean.TRUE));

        ConnectionStatus result = model.getConnectionStatus();
        String message = model.getConnectionErrorMessage();

        assertThat("Connection status", result, is(ConnectionStatus.FAILED));
        assertThat("Connection error message", message, containsString(expectedError));
    }

    @Test
    public void GIVEN_no_connection_WHEN_get_connect_and_login_message_returns_failure_THEN_status_is_fail() {
        String messageId = "message id";
        sessionSendMessage().thenReturn(SendMessageDetails.createSendSuccess(messageId));
        connectionPropertChange
                .propertyChange(new PropertyChangeEvent(this, "connection", Boolean.FALSE, Boolean.TRUE));
        ReceiveMessage nicosMessage = messageParser.parseMessage(new MessageDetails(LOGIN_FAIL, messageId));
        model.newMessage(nicosMessage);

        ConnectionStatus result = model.getConnectionStatus();
        String message = model.getConnectionErrorMessage();

        assertThat("Connection status", result, is(ConnectionStatus.FAILED));
        assertThat("Connection error message", message, containsString(LOGIN_FAIL_ERROR_MESSAGE));
    }

}

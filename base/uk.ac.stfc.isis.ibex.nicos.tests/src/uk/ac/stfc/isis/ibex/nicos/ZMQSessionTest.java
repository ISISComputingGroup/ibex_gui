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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.zeromq.ZMQException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQWrapper;
import uk.ac.stfc.isis.ibex.nicos.messages.NICOSMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.ReceiveMessage;
import uk.ac.stfc.isis.ibex.nicos.messages.SentMessageDetails;

public class ZMQSessionTest {

    private ZMQWrapper zmq = mock(ZMQWrapper.class);
    private ZMQSession session;

    private NICOSMessage mockMessage = mock(NICOSMessage.class);

    @Before
    public void setUp() {
        session = new ZMQSession(zmq);
    }

    @Test
    public void WHEN_connect_called_THEN_zmq_connect_called_with_constructed_uri() {
        String hostName = "MY_HOST";
        InstrumentInfo instrument = new InstrumentInfo("", "", hostName);
        session.connect(instrument);

        ArgumentCaptor<String> uri = ArgumentCaptor.forClass(String.class);
        verify(zmq, times(1)).connect(uri.capture());

        assertEquals("tcp://" + hostName + ":1301", uri.getValue());
    }

    private SentMessageDetails sendBlankMessage(int numberOfParts) {
        ArrayList<String> parts = new ArrayList<>();
        for (int i=0; i<numberOfParts; i++) {
            parts.add("");
        }
        try {
            when(mockMessage.getMulti()).thenReturn(parts);
        } catch (ConversionException e) {
        }

        return session.sendMessage(mockMessage);
    }

    @Test
    public void GIVEN_NICOS_message_with_three_parts_WHEN_message_sent_THEN_three_parts_sent() {
        // Note: most messages NICOS expects will be 3 parts
        sendBlankMessage(3);

        verify(zmq, times(3)).send(anyString(), anyBoolean());
    }

    @Test
    public void GIVEN_NICOS_message_with_zero_parts_WHEN_message_sent_THEN_nothing_sent() {
        sendBlankMessage(0);

        verify(zmq, never()).send(anyString(), anyBoolean());
    }

    @Test
    public void GIVEN_NICOS_message_with_one_part_WHEN_message_sent_THEN_one_part_sent() {
        sendBlankMessage(1);

        verify(zmq, times(1)).send(anyString(), anyBoolean());
    }

    @Test
    public void GIVEN_NICOS_message_with_one_part_WHEN_message_sent_THEN_more_flag_is_false() {
        sendBlankMessage(1);

        verify(zmq, times(1)).send(anyString(), eq(false));
    }

    @Test
    public void GIVEN_NICOS_message_with_two_parts_WHEN_message_sent_THEN_first_more_flag_is_true_and_second_false() {
        sendBlankMessage(2);

        ArgumentCaptor<Boolean> more = ArgumentCaptor.forClass(Boolean.class);
        verify(zmq, times(2)).send(anyString(), more.capture());

        assertEquals(true, more.getAllValues().get(0));
        assertEquals(false, more.getAllValues().get(1));
    }

    @Test
    public void GIVEN_message_failing_to_send_WHEN_message_sent_THEN_fail_message_returned() {
        String failedMessage = "FAIL";
        doThrow(new ZMQException(failedMessage, 0)).when(zmq).send(anyString(), anyBoolean());
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(failedMessage, resp.getFailureReason());
    }

    @Test
    public void GIVEN_message_failing_to_convert_WHEN_message_sent_THEN_failed_to_convert_message_returned()
            throws ConversionException {
        doThrow(new ConversionException("")).when(mockMessage).getMulti();
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(ZMQSession.FAILED_TO_CONVERT, resp.getFailureReason());
    }

    @Test
    public void WHEN_message_sent_THEN_three_messages_expected_from_server() {
        sendBlankMessage(2);

        verify(zmq, times(3)).receiveString();
    }

    @Test
    public void GIVEN_null_responses_from_server_WHEN_message_sent_THEN_no_data_received_message() {
        when(zmq.receiveString()).thenReturn(null);
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(ZMQSession.NO_DATA_RECEIVED, resp.getFailureReason());
    }

    @Test
    public void GIVEN_empty_responses_from_server_WHEN_message_sent_THEN_no_data_received_message() {
        when(zmq.receiveString()).thenReturn("");
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(ZMQSession.NO_DATA_RECEIVED, resp.getFailureReason());
    }

    @Test
    public void GIVEN_invalid_responses_from_server_WHEN_message_sent_THEN_message_not_parsed()
            throws ConversionException {
        when(zmq.receiveString()).thenReturn("");
        sendBlankMessage(2);

        verify(mockMessage, never()).parseResponse(anyString());
    }

    @Test
    public void GIVEN_ok_status_response_from_server_WHEN_message_sent_THEN_response_parsed()
            throws ConversionException {
        when(zmq.receiveString()).thenReturn("ok");
        sendBlankMessage(2);

        verify(mockMessage).parseResponse(anyString());
    }

    @Test
    public void GIVEN_ok_status_message_from_server_WHEN_message_sent_THEN_third_response_is_parsed()
            throws ConversionException {
        String response = "TEST";
        when(zmq.receiveString()).thenReturn("ok", "", response);
        sendBlankMessage(2);

        verify(mockMessage).parseResponse(response);
    }

    @Test
    public void GIVEN_parsable_message_from_server_WHEN_message_sent_THEN_successful_message_returned()
            throws ConversionException {
        ReceiveMessage recieved = mock(ReceiveMessage.class);
        when(zmq.receiveString()).thenReturn("ok");
        when(mockMessage.parseResponse(anyString())).thenReturn(recieved);
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(true, resp.isSent());
        assertEquals(recieved, resp.getResponse());
    }

    @Test
    public void GIVEN_unparsable_message_from_server_WHEN_message_sent_THEN_unexpected_response_message_returned()
            throws ConversionException {
        when(zmq.receiveString()).thenReturn("ok");
        doThrow(new ConversionException("")).when(mockMessage).parseResponse(anyString());
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(ZMQSession.UNEXPECTED_RESPONSE, resp.getFailureReason());
    }

    @Test
    public void GIVEN_not_ok_status_from_server_WHEN_message_sent_THEN_server_response_returned() {
        String serverResponse = "BAD RESPONSE";
        when(zmq.receiveString()).thenReturn("not_ok").thenReturn("").thenReturn(serverResponse);
        SentMessageDetails resp = sendBlankMessage(2);

        assertEquals(false, resp.isSent());
        assertEquals(serverResponse, resp.getFailureReason());
    }
}

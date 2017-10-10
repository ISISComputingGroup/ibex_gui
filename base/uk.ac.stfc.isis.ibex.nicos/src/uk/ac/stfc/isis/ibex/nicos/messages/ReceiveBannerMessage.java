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

package uk.ac.stfc.isis.ibex.nicos.messages;

/**
 * A Login Message that has been received from NICOS. There are two associated
 * with the login command, the first is the initial connection and details nicos
 * the second is the user connection.
 * 
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
@SuppressWarnings({ "checkstyle:membername", "unused" })
public class ReceiveBannerMessage implements ReceiveMessage {
    private String custom_path;
    private String nicos_root;
    private String rsakey;
    private String daemon_version;
    private String pw_hashing;
    private String serializer;
    private Integer protocol_version;
    private String custom_version;

    private static final String VALID_SERIALISER = "json";
    private static final Integer VALID_VERSION = 15;

    /**
     * @return True if the server is using the correct serializer.
     */
    public boolean serializerValid() {
        return serializer.equals(VALID_SERIALISER);
    }
    
    /**
     * @return True if the server is using the correct protocol.
     */
    public boolean protocolValid() {
        return protocol_version == VALID_VERSION;
    }

}

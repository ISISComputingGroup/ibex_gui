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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;

/**
 * Class to log in to Nicos.
 */
public class Login extends NICOSMessage<Map<String, String>, ReceiveLoginMessage> {
    /**
     * Creates the login command for NICOS.
     */
    public Login() {
        command = "authenticate";
        Map<String, String> params = new HashMap<>();
        params.put("login", "ibex");
        params.put("passwd", "dECJ_2Fa");
        parameters = Arrays.asList(params);
    }

    @Override
    public ReceiveLoginMessage parseResponse(String response) throws ConversionException {
        return new JsonDeserialisingConverter<>(ReceiveLoginMessage.class).apply(response);
    }
        
}

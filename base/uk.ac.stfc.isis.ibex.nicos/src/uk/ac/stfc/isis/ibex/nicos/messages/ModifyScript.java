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

import java.util.Arrays;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;

/**
 * Serialisable class to modif a script in NICOS.
 */
public class ModifyScript extends NICOSMessage<String> {
    
    /**
     * Constructor.
     * 
     * @param reqid ID of script
     * @param newcode the new code of the script
     * @param reason the reason for script modification
     */
    public ModifyScript(String reqid, String newcode, String reason) {
    	this.command = "update";
    	System.out.println(String.format("reqid: %s\n\n newcode %s\n\n reason: %s", reqid, newcode, reason));
        this.parameters = Arrays.asList(newcode, reason, reqid);
    }

    @Override
    public ReceiveMessage parseResponse(String response) throws ConversionException {
        JsonDeserialisingConverter<String> deserial = new JsonDeserialisingConverter<>(String.class);
        return new ReceiveStringMessage(deserial.convert(response));
    }
        
}

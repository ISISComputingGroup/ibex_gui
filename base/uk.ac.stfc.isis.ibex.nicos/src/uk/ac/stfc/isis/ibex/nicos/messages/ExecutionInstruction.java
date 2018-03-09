 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2018 Science & Technology Facilities Council.
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

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonDeserialisingConverter;
import uk.ac.stfc.isis.ibex.nicos.BreakLevel;
import uk.ac.stfc.isis.ibex.nicos.ExecutionInstructionType;

/**
 * Sends an instruction affecting the current script execution in the NICOS
 * server.
 */
public class ExecutionInstruction extends NICOSMessage<Integer> {


    /**
     * The constructor.
     * 
     * @param type
     *            The type of instruction.
     * @param level
     *            The break level
     */
    public ExecutionInstruction(ExecutionInstructionType type, BreakLevel level) {
        this.command = type.getCommand();
        if (level != null) {
            this.parameters = Arrays.asList(level.getNumVal());
        }
    }

    /**
     * @param response
     * @return
     * @throws ConversionException
     */
    @Override
    public ReceiveMessage parseResponse(String response) throws ConversionException {
        return new JsonDeserialisingConverter<>(ReceiveScriptStatus.class).convert(response);
    }

}

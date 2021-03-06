
/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.core.databinding.conversion.Converter;

import uk.ac.stfc.isis.ibex.configserver.displaying.RuncontrolState;

/**
 * This is a converter for converting the value for the current run-control
 * state into the appropriate symbol for the run-control status indicator.
 * 
 * Used for data-binding.
 * 
 */
public class RunControlSymbolConverter extends Converter<RuncontrolState, String> {
    
    /**
     * Constructor.
     */
    public RunControlSymbolConverter() {
        super(RuncontrolState.class, String.class);
    }

    @Override
    public String convert(RuncontrolState state) {
        switch (state) {
	        case ENABLED_IN_RANGE:
	        	return "\u2713";  // Checkmark
	        case ENABLED_OUT_RANGE:
	        	return "X";
	        case DISCONNECTED:
	        	return "!";
	        default:
	        	return "";
        }
    }

}

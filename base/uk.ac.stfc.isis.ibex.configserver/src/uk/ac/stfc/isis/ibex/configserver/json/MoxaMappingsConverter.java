
/*
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

package uk.ac.stfc.isis.ibex.configserver.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

/**
 * Converts a JSON representation of a PV into a java object representation.
 */
@SuppressWarnings({ "checkstyle:magicnumber", "rawtypes", "unchecked" })
public class MoxaMappingsConverter implements Function<Map, HashMap<String, ArrayList<ArrayList<String>>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, ArrayList<ArrayList<String>>> apply(Map value) throws ConversionException {
		HashMap<String, ArrayList<ArrayList<String>>> namesToPorts = new HashMap<String, ArrayList<ArrayList<String>>>(value);
		return namesToPorts;
	}
}

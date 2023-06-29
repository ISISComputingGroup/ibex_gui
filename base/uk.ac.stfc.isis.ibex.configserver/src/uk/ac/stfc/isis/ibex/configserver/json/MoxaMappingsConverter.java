
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.MoxaMappings;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

/**
 * Converts a JSON representation of a PV into a java object representation.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MoxaMappingsConverter implements Function<String[][], MoxaMappings> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MoxaMappings apply(String[][] value) throws ConversionException {
		
		 HashMap<String, String> namesToIps = new HashMap<String, String>(); 
		 HashMap<String, HashMap<String, String>> namesToPorts = new HashMap<String, HashMap<String,String>>();
		return new MoxaMappings(namesToIps, namesToPorts);
//		return Arrays.stream(value)
//				.map(info -> new MoxaMappings(info[0], info[1], info[2], info[3]))
//				.collect(Collectors.toCollection(ArrayList::new));
	}
}

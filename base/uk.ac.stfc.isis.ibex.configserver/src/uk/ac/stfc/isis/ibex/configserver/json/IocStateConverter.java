
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

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * Converts a JSON representation of the state of an IOC into a java object representation.
 */
public class IocStateConverter extends Converter<Map<String, IocParameters>, Collection<IocState>> {
	/**
	 * Converts a JSON representation of the state of an IOC into a java object representation.
	 * 
	 * @param value the value to convert
	 * 
	 * @return a collection of converted values
	 */
	@Override
	public Collection<IocState> convert(Map<String, IocParameters> value) throws ConversionException {
		return value.entrySet().stream()
				.map(entry -> new IocState(entry.getKey(), entry.getValue().isRunning(), entry.getValue().getDescription()))
				.collect(Collectors.toSet());
	}
}

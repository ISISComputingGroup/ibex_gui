
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
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
/**
 * Converts a JSON representation of an editable IOC into a java object representation.
 */
public class EditableIocsConverter implements Function<Map<String, IocParameters>, Collection<EditableIoc>> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<EditableIoc> apply(Map<String, IocParameters> value) throws ConversionException {
		return value.entrySet().stream()
				.map(entry -> iocFromParameters(entry.getKey(), entry.getValue()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Construct an EditableIoc object based on IocParameters.
	 * @param name - The name of the IOC.
	 * @param parameters - The parameters.
	 * @return - The new IOC
	 */
	private static EditableIoc iocFromParameters(String name, IocParameters parameters) {
		EditableIoc ioc = new EditableIoc(name);
		setIfNotNull(ioc::setAvailableMacros, parameters.getMacros());
		setIfNotNull(ioc::setAvailablePVs, parameters.getPVs());
		setIfNotNull(ioc::setAvailablePVSets, parameters.getPVSets());
		setIfNotNull(ioc::setDescription, parameters.getDescription());
		setIfNotNull(ioc::setRemotePvPrefix, parameters.getRemotePvPrefix());
		return ioc;	
	}
	
	/**
	 * Calls the method toBeSet with the provided value, if the value is not null.
	 * @param <T> - The type of object to use.
	 * @param toBeSet - The method to call if the value was non-null.
	 * @param value - The value to set.
	 */
	private static <T> void setIfNotNull(Consumer<T> toBeSet, T value) {
		if (value != null) {
			toBeSet.accept(value);
		}
	}
}

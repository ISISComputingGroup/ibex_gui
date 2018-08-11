
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
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class EditableIocsConverter implements Converter<Map<String, IocParameters>, Collection<EditableIoc>> {
	
	@Override
	public Collection<EditableIoc> apply(Map<String, IocParameters> value) throws ConversionException {
		return Lists.newArrayList(Iterables.transform(value.entrySet(), new Function<Map.Entry<String, IocParameters>, EditableIoc>() {

			@Override
			public EditableIoc apply(Entry<String, IocParameters> entry) {
				String name = entry.getKey();
				IocParameters parameters = entry.getValue();
				
				EditableIoc ioc = new EditableIoc(name);
				if (parameters.getMacros() != null) {
					ioc.setAvailableMacros(parameters.getMacros());
				}
				
				if (parameters.getPVs() != null) {
					ioc.setAvailablePVs(parameters.getPVs());
				}
				
				if (parameters.getPVSets() != null) {
					ioc.setAvailablePVSets(parameters.getPVSets());
				}
				
                if (parameters.getDescription() != null) {
                    ioc.setDescription(parameters.getDescription());
                }

				return ioc;				
			}
		}));
	}
}

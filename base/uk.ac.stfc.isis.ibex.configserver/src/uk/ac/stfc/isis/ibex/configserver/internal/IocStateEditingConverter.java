
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class IocStateEditingConverter extends Converter<Collection<IocState>, Collection<EditableIocState>> {
	
	private final ConfigServer server;

	public IocStateEditingConverter(ConfigServer server) {
		this.server = server;
	}
	
	@Override
	public Collection<EditableIocState> convert(Collection<IocState> value)
			throws ConversionException {
		return Lists.newArrayList(Iterables.transform(value, new Function<IocState, EditableIocState>() {
			@Override
			public EditableIocState apply(IocState state) {
				return new EditableIocState(
						state, 
						server.iocDescription(state.getName()), 
						server.iocDescriptionSetter(state.getName()));
			}
		}));
	}

}

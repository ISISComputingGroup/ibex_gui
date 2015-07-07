
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
import java.util.Locale;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.FilterPredicate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class IocNameFilterPredicate implements FilterPredicate<EditableIoc> {

	@Override
	public boolean apply(Collection<EditableIoc> toFilter, EditableIoc item) {
		final String iocName = normalise(item.getName());
		return Iterables.any(normaliseEach(toFilter), new Predicate<String>() {
			@Override
			public boolean apply(String forbidden) {
				return iocName.startsWith(forbidden);
			}
			
		});
	}

	private Iterable<String> normaliseEach(Collection<EditableIoc> toFilter) {
		return Iterables.transform(toFilter, new Function<EditableIoc, String>() {
			@Override
			public String apply(EditableIoc ioc) {
				return normalise(ioc.getName());
		}});
	}

	private String normalise(String text) {
		return text.toLowerCase(Locale.UK);
	}
}

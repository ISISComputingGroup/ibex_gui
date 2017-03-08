
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
import java.util.Collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

/**
 * Removes iocs that whose states should not be changed by the user.
 */
public class FilteredIocs extends 
        TransformingObservable<Pair<Collection<IocState>, Collection<String>>, Collection<IocState>> {
	
    public FilteredIocs(ForwardingObservable<Collection<IocState>> iocs,
			ForwardingObservable<Collection<String>> iocsToFilter) {
		setSource(new ObservablePair<>(iocs, iocsToFilter));
	}

	@Override
    protected Collection<IocState> transform(Pair<Collection<IocState>, Collection<String>> value) {
        System.out.println("Converting: pair " + value.first.size() + "  " + value.second.size());
		if (value.first == null || value.second == null) {
			return Collections.emptyList();
		}
		
		return Lists.newArrayList(Iterables.filter(value.first, filterByName(value.second)));
	}

	private static Predicate<IocState> filterByName(final Collection<String> namesToFilter) {
		return new Predicate<IocState>() {
			@Override
			public boolean apply(final IocState ioc) {
				return !Iterables.any(namesToFilter, nameMatches(ioc));
			}
		};
	}

	private static Predicate<String> nameMatches(final IocState ioc) {
		return new Predicate<String>() {
			@Override
			public boolean apply(String name) {
				return ioc.getName().startsWith(name);
			}
		};
	}	
}

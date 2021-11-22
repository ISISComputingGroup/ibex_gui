
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

package uk.ac.stfc.isis.ibex.epics.observing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * The Class FilteredCollectionObservable is an observable on another observable
 * but with the objects matching the filter removed.
 *
 * @param <T>
 *            the generic type to filter
 */
public class FilteredCollectionObservable<T extends INamed> extends
        TransformingObservable<Pair<Collection<T>, Collection<String>>, Collection<T>> {
	
    /**
     * Instantiates a new filtered IOCs forwarding observer.
     *
     * @param iocs
     *            the iocs
     * @param iocsToFilter
     *            the iocs to filter
     */
    public FilteredCollectionObservable(ForwardingObservable<Collection<T>> iocs,
			ForwardingObservable<Collection<String>> iocsToFilter) {
    	setSource(new ObservablePair<>(iocs, iocsToFilter));
	}

	@Override
    protected Collection<T> transform(Pair<Collection<T>, Collection<String>> value) {
		if (value.first == null || value.second == null) {
			return Collections.emptyList();
		}
		
		return Lists.newArrayList(Iterables.filter(value.first, filterByName(value.second)));
	}

    private static <T1 extends INamed> Predicate<T1> filterByName(final Collection<String> namesToFilter) {
        return ioc -> !namesToFilter.stream().anyMatch(nameMatches(ioc));
	}
    
    private static Predicate<String> nameMatches(final INamed ioc) {
        return name -> ioc.getName().startsWith(name);
    }

    /**
     * Create an observable on a collection of objects which are filtered by
     * their name using an observable on a list of strings.
     * 
     * @param <T>
     *            type of object to filter in the collection
     * @param observableToFilter
     *            the observable to filter
     * @param filterOut
     *            observable on a collection of string used to filter the list
     * @return forward observable without the filtered objects
     */
    public static <T extends INamed> ForwardingObservable<Collection<T>> createFilteredByNameObservable(
            ForwardingObservable<Collection<T>> observableToFilter,
            ForwardingObservable<Collection<String>> filterOut) {

        // Use the above converter in a converting observable

        ConvertingObservable<Collection<T>, Collection<T>> convertingObservable =
                new ConvertingObservable<Collection<T>, Collection<T>>(observableToFilter, ArrayList<T>::new);

        ForwardingObservable<Collection<T>> iocs = new ForwardingObservable<>(convertingObservable);

        FilteredCollectionObservable<T> filteredIocs = new FilteredCollectionObservable<>(iocs, filterOut);

        return new ForwardingObservable<>(filteredIocs);
    }

}

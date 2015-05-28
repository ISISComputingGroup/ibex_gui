package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;
import java.util.Collections;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/*
 * Removes iocs that whose states should not be changed by the user.
 */
public class FilteredIocs extends 
	TransformingObservable<Pair<Collection<EditableIocState>, Collection<String>>, Collection<EditableIocState>> {
	
	public FilteredIocs(InitialiseOnSubscribeObservable<Collection<EditableIocState>> iocs,
			InitialiseOnSubscribeObservable<Collection<String>> iocsToFilter) {
		setSource(new ObservablePair<>(iocs, iocsToFilter));
	}

	@Override
	protected Collection<EditableIocState> transform(Pair<Collection<EditableIocState>, Collection<String>> value) {
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

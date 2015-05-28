package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;
import java.util.Locale;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.FilterPredicate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class IocNameFilterPredicate implements FilterPredicate<EditableIoc>{

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
		return Iterables.transform(toFilter, new Function<EditableIoc, String>(){
			@Override
			public String apply(EditableIoc ioc) {
				return normalise(ioc.getName());
		}});
	}

	private String normalise(String text) {
		return text.toLowerCase(Locale.UK);
	}
}

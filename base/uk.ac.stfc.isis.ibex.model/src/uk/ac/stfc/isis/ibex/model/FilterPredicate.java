package uk.ac.stfc.isis.ibex.model;

import java.util.Collection;

public interface FilterPredicate<T> {
	boolean apply(Collection<T> toFilter, T item);
}

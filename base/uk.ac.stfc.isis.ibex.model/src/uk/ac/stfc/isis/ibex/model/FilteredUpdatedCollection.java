package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FilteredUpdatedCollection<T> extends UpdatedValue<Collection<T>> {

	private final UpdatedValue<Collection<T>> values;
	private final UpdatedValue<Collection<T>> toFilter;
	private FilterPredicate<T> predicate;
	
	private final Runnable update = new Runnable() {
		@Override
		public void run() {
			update();
		}
	};
	
	private final PropertyChangeListener doUpdate = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			update.run();
		}
	};
	
	public FilteredUpdatedCollection(
			UpdatedValue<Collection<T>> values, 
			UpdatedValue<Collection<T>> toFilter,
			FilterPredicate<T> predicate) {
		this(values, toFilter);
		
		this.predicate = predicate;
	}
	
	public FilteredUpdatedCollection(UpdatedValue<Collection<T>> values, UpdatedValue<Collection<T>> toFilter) {
		this.values = values;
		this.toFilter = toFilter;
		UpdatedValues.awaitValues(values, toFilter).onAllSet(update);
	}
	
	private synchronized void update() {
		Set<T> filtered = new HashSet<>(values.getValue());
		Collection<T> itemsToFilter = toFilter.getValue();
		if (predicate == null) {
			filtered.removeAll(itemsToFilter);
		} else {
			for (T item : new ArrayList<T>(filtered)) {
				if (predicate.apply(itemsToFilter, item)) {
					filtered.remove(item);
				}
			}
		}
		
		
		if (!isSet()) {
			values.addPropertyChangeListener(doUpdate);
			toFilter.addPropertyChangeListener(doUpdate);
		}
		
		setValue(filtered);
	}
}


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

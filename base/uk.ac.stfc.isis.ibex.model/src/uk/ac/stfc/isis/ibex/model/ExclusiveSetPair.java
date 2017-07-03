
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Stores unique items in one of two sets. All items are initially added to the
 * first set.
 *
 * @param <T>
 *            the type of items to store
 */
public class ExclusiveSetPair<T> {

	private Set<T> selected = new HashSet<>();
	private Set<T> unselected = new LinkedHashSet<>();

    /**
     * Creates an exclusive pair based on a collection of selected items and a
     * collection of unselected items.
     * 
     * @param unselectedItems
     *            The unselected items for the pair.
     * @param selectedItems
     *            The selected items for the pair.
     */
	public ExclusiveSetPair(Collection<T> unselectedItems, Collection<T> selectedItems) {
		unselected.addAll(unselectedItems);
		unselected.addAll(selectedItems);
		move(selectedItems);
	}
	
    /**
     * Get the set of unselected items.
     * 
     * @return The unselected items.
     */
	public Set<T> unselected() {
		return Collections.unmodifiableSet(unselected);
	}

    /**
     * Gets the set of selected items.
     * 
     * @return The selected items.
     */
	public Set<T> selected() {
		return Collections.unmodifiableSet(selected);
	}

    /**
     * Gets the set that contains both selected and unselected items.
     * 
     * @return The set of all items.
     */
	public Set<T> all() {
		Set<T> all = new LinkedHashSet<>();
		all.addAll(selected);
		all.addAll(unselected);
		
		return Collections.unmodifiableSet(all);
	}
	
    /**
     * Moves a given item between sets.
     * 
     * @param item
     *            The item to move between sets.
     */
	public void move(T item) {			
		if (selected.contains(item)) {
			move(item, selected, unselected);
			return;
		}
		
		if (unselected.contains(item)) {
			move(item, unselected, selected);
			return;
		}
		
		return;
	}
	
    /**
     * Moves a collection of items between sets.
     * 
     * @param items
     *            The items to move between sets.
     */
	public void move(Collection<T> items) {
		for (T item : items) {
			move(item);
		}
	}
	
    /**
     * Get whether the item is contained in either set.
     * 
     * @param item
     *            The item to check for.
     * @return True if the item is contained in one of the sets.
     */
	public boolean contains(T item) {
		return selected.contains(item) || unselected.contains(item);
	}
	
    /**
     * Removes an item from the sets.
     * 
     * @param item
     *            The item to remove.
     * @return True if the item was removed.
     */
	public boolean remove(T item) {
		return selected.remove(item) || unselected.remove(item);
	}
	
	private void removeAll(Set<T> items) {
		for (T item : items) {
			remove(item);
		}
	}
	
    /**
     * Adds an item to the unselected set.
     * 
     * @param item
     *            The item to add.
     */
	public void addUnselected(T item) {
		if (contains(item)) {
			return;
		}
		
		unselected.add(item);
	}

    /**
     * Adds an item to the selected set.
     * 
     * @param item
     *            The item to add.
     */
	public void addSelected(T item) {
		if (contains(item)) {
			return;
		}
		
		selected.add(item);
	}

    /**
     * Adds the items to the unselected set if they are not already present in
     * either set.
     * 
     * @param items the items to add
     */
	public void addAllAsUnselected(Collection<T> items) {
		for (T item : items) {
			addUnselected(item);
		}
	}

    /**
     * Adds the items to the selected set if they are not already present in
     * either set.
     * 
     * @param items the items to add
     */
	public void addAllAsSelected(Collection<T> items) {
		for (T item : items) {
			addSelected(item);
		}
	}

    /**
     * Adds any items that are not already present to the unselected set and
     * removes any items that are not part of the new collection.
     * 
     * @param items the items
     */
	public void synchronise(Collection<T> items) {	
		addAllAsUnselected(items);		
		removeAll(missing(items));
	}

	private Set<T> missing(Collection<T> items) {
		Set<T> missing = new LinkedHashSet<>(all());
		missing.removeAll(items);
		
		return missing;
	}
	
	private static <T> void move(T item, Set<T> from, Set<T> to) {
		from.remove(item);
		to.add(item);
	}
}

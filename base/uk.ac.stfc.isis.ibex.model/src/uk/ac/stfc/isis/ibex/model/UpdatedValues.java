
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
import java.util.HashMap;
import java.util.Map;

/**
 * Performs an action once all updated values have a value.
 *
 */
public final class UpdatedValues {

	private final Map<UpdatedValue<?>, Boolean> status = new HashMap<>();
	private SettableUpdatedValue<Boolean> allSet = new SettableUpdatedValue<>(false);
	
	private UpdatedValues(UpdatedValue<?> first, UpdatedValue<?>... remaining) {		
		add(first);
		for (UpdatedValue<?> other : remaining) {
			add(other);
		}
		
		update();
	}

	public static UpdatedValues awaitValues(UpdatedValue<?> first, UpdatedValue<?>... remaining) {
		return new UpdatedValues(first, remaining);
	}
	
	public boolean allSet() {
		return allSet.getValue();
	}

	public void onAllSet(Runnable action) {
		if (allSet.getValue()) {
			action.run();
			return;
		}
		
		allSet.addPropertyChangeListener(perform(action));
	}
	
	private PropertyChangeListener perform(final Runnable action) {
		return new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (allSet.getValue()) {
					action.run();
				}
			}
		};
	}
	
	private synchronized void update() {		
		allSet.setValue(areAllSet());
	}
	
	private void add(final UpdatedValue<?> value) {
		updateStatus(value);
		if (!status.get(value)) {
			PropertyChangeListener onValueSet = getListener(value);
			value.addPropertyChangeListener(onValueSet);
		}
	}

	private PropertyChangeListener getListener(final UpdatedValue<?> value) {
		PropertyChangeListener onValueSet = new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				status.put(value, true);
				value.removePropertyChangeListener(this);
				update();
			}
		};
		
		return onValueSet;
	}

	private boolean areAllSet() {
		for (Boolean wasSet : status.values()) {
			if (!wasSet) {
				return false;
			}			
		}
		
		return true;
	}
	
	private void updateStatus(final UpdatedValue<?> value) {
		status.put(value, value.isSet());
	}
}

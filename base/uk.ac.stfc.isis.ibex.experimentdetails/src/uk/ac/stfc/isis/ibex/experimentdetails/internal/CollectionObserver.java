
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

package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;

/**
 * An observer that observes a collection. 
 * It updates with an empty collection when the observable goes into error or disconnects.
 *
 * @param <T> The type of the collection.
 */
public abstract class CollectionObserver<T> extends BaseObserver<Collection<T>> {

	@Override
	public void onValue(Collection<T> value) {
		updateCollection(value);
	}

	@Override
	public void onError(Exception e) {
		clear();
	}

	@Override
	public void onConnectionStatus(boolean isConnected) {
		if (!isConnected) {
			clear();
		}			
	}
	
	private void clear() {
		updateCollection(new ArrayList<T>());
	}
	
	protected abstract void updateCollection(Collection<T> values);
}
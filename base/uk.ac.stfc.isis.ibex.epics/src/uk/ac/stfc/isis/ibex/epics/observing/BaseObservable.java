
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

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * The abstract base class for observables
 *
 */
public abstract class BaseObservable<T> implements Observable<T> {
		
	private final Collection<Observer<T>> observers = new CopyOnWriteArrayList<>();
	
	@Override
	public Subscription addObserver(Observer<T> observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
		
		return new Unsubscriber<Observer<T>>(observers, observer);
	}
		
	protected void setValue(T value) {
		if (value == null) {
			return;
		}
		
		for (Observer<T> observer : observers) {
			observer.onValue(value);
		}
	}

	protected void setError(Exception e) {
		for (Observer<T> observer : observers) {
			observer.onError(e);
		}
	}
	
	protected void setConnectionStatus(boolean isConnected) {
		for (Observer<T> observer : observers) {
			observer.onConnectionStatus(isConnected);
		}
	}
}

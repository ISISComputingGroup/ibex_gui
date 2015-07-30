
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

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

/**
 * Links together two related observables.
 *
 */
public class ObservablePair<T1, T2> 
				extends BaseCachingObservable<Pair<T1, T2>> 
				implements ClosableCachingObservable<Pair<T1, T2>> {

	private abstract class PairObserver<T> extends BaseObserver<T> {
		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			setConnectionStatus(isConnected);
		}	
	}
	
	private final InitialisableObserver<T1> firstObserver = new PairObserver<T1>() {
		@Override
		public void onValue(T1 value) {
			setValue(new Pair<>(value, secondOrNull()));
		}
	};
	
	private final InitialisableObserver<T2> secondObserver = new PairObserver<T2>() {
		@Override
		public void onValue(T2 value) {
			setValue(new Pair<>(firstOrNull(), value));
		}
	};

	private final Subscription firstSubscription;
	private final Subscription secondSubscription;
	
	public ObservablePair(InitialiseOnSubscribeObservable<T1> firstSource, InitialiseOnSubscribeObservable<T2> secondSource) {
		firstSubscription = firstSource.addObserver(firstObserver);
		secondSubscription = secondSource.addObserver(secondObserver);
	}
	
	@Override
	public void close() {
		firstSubscription.removeObserver();
		secondSubscription.removeObserver();
	}

	private T1 firstOrNull() {
		Pair<T1, T2> pair = getValue();
		return pair != null ? pair.first : null;
	}
	
	private T2 secondOrNull() {
		Pair<T1, T2> pair = getValue();
		return pair != null ? pair.second : null;
	}
}


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


/**
 * The abstract base class for classes that transform the observed value into something else.
 * For example: changing an enum into a string
 *
 */
public abstract class TransformingObservable<T1, T2> extends BaseCachingObservable<T2> implements ClosableCachingObservable<T2> {

	private ClosableCachingObservable<T1> source;
	private Subscription sourceSubscription;

	private final BaseObserver<T1> sourceObserver = new BaseObserver<T1>() {
		@Override
		public void onValue(T1 value) {
			setValue(transform(value));
		}

		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			setConnectionChanged(isConnected);
		}
	};

	public final void setSource(ClosableCachingObservable<T1> source) {
		cancelSubscription();
		this.source = source;
		sourceObserver.update(source.value(), source.lastError(), source.isConnected());
		sourceSubscription = source.subscribe(sourceObserver);
	}
	
	protected abstract T2 transform(T1 value);

	@Override
	public void close() {
		cancelSubscription();
		source.close();
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) {
			sourceSubscription.cancel();
		}
	}	
}

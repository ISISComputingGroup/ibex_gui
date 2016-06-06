
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
 * An observable whose source is another observable.
 *
 */
public class ForwardingObservable<T> extends ClosableObservable<T> {
		
	private final BaseObserver<T> sourceObserver = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			setConnectionStatus(isConnected);
		}

        @Override
        public void onAlarm(String alarm) {
            setAlarm(alarm);
        }
	};
	
	private Subscription sourceSubscription;

    public ForwardingObservable(Observable<T> source) {
        setSource(source);
    }

    protected synchronized void setSource(Observable<T> newSource) {
		cancelSubscription();	
        sourceObserver.onConnectionStatus(false);
		
        sourceObserver.onConnectionStatus(newSource.isConnected());
		
		Exception error = newSource.lastError();
		if (error != null) {
			sourceObserver.onError(error);
		}
		
		T value = newSource.getValue();
		if (value != null) {
			sourceObserver.onValue(value);
		}
		
		sourceSubscription = newSource.addObserver(sourceObserver);
	}
	
	@Override
	public void close() {
		cancelSubscription();
        sourceObserver.onConnectionStatus(false);
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) {
			sourceSubscription.removeObserver();
		}
	}

    @Override
    public Subscription addObserver(Observer<T> observer) {
        observer.update(getValue(), lastError(), isConnected());
        return super.addObserver(observer);
    }
}

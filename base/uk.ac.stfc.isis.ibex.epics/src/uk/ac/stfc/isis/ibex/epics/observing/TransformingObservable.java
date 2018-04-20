
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * The abstract base class for classes that transform the observed value into something else.
 * For example: changing an enum into a string
 *
 * @param <T1> The type of the first value being observed.
 * @param <T2> The required type to transform to.
 */
public abstract class TransformingObservable<T1, T2> extends ClosableObservable<T2> {

    private ClosableObservable<T1> source;
	private Subscription sourceSubscription;

	private final BaseObserver<T1> sourceObserver = new BaseObserver<T1>() {
		@Override
		public void onValue(T1 value) {
			T2 transform = transform(value);
            setValue(transform);
		}

		@Override
		public void onError(Exception e) {
			setError(e);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			setConnectionStatus(isConnected);
		}

	};

    public TransformingObservable(ClosableObservable<T1> source) {
    	setSource(source);
	}
    
    protected void setSource(ClosableObservable<T1> source) {
		cancelSubscription();
		closeSource();
		this.source = source;     

        sourceObserver.onConnectionStatus(false);
        sourceObserver.onConnectionStatus(source.isConnected());

        T1 value = source.getValue();
        if (value != null) {
            sourceObserver.onValue(value);
        }

        Exception error = source.currentError();
        if (error != null) {
            sourceObserver.onError(error);
        }
		sourceSubscription = source.addObserver(sourceObserver);
    }
	
	protected abstract T2 transform(T1 value);

	@Override
	public void close() {
		cancelSubscription();
		closeSource();
        super.close();
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) sourceSubscription.removeObserver();
	}
	
	private void closeSource() {
		if (source != null) {
	        sourceObserver.onConnectionStatus(false);
			source.close();
		}
	}
}

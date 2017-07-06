
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
 * An observable whose source is another observable.
 *
 * @param <T>
 *            The type of the value that you are observing.
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
	};
	
    private ClosableObservable<T> source;

	private Subscription sourceSubscription;

    /**
     * Sets up a forwarding observable with a closable source.
     * 
     * @param source
     *            the source observable
     */
    public ForwardingObservable(ClosableObservable<T> source) {
        setSource(source);
    }

    /**
     * Sets the source observable.
     * 
     * @param newSource
     *            the new source observable
     */
    protected synchronized void setSource(ClosableObservable<T> newSource) {
        cancelSubscription();

        sourceObserver.onConnectionStatus(false);

        sourceObserver.onConnectionStatus(newSource.isConnected());

        T value = newSource.getValue();
        if (value != null) {
            sourceObserver.onValue(value);
        }

        Exception error = newSource.currentError();
        if (error != null) {
            sourceObserver.onError(error);
        }

        sourceSubscription = newSource.addObserver(sourceObserver);
    }
	
	@Override
	public void close() {
		cancelSubscription();
        sourceObserver.onConnectionStatus(false);
        if (source != null) {
            source.close();
        }
        super.close();
	}
	
	private void cancelSubscription() {
		if (sourceSubscription != null) {
			sourceSubscription.removeObserver();
		}
        if (source != null) {
            source.close();
        }
	}

    @Override
    public Subscription addObserver(Observer<T> observer) {
        observer.update(getValue(), currentError(), isConnected());
        return super.addObserver(observer);
    }
}

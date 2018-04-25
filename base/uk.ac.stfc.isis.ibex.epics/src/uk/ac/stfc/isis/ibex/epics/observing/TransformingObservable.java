
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
 * An object that observes a base observable of a particular type and supplies a
 * transformed version of the base observable's value on to other objects
 * 
 * For example: changing an enum into a string
 *
 * @param <T1> The type of the first value being observed.
 * @param <T2> The required type to transform to.
 */
public abstract class TransformingObservable<T1, T2> extends ClosableObservable<T2> {

	/**
	 * The observable that provides the raw value to be converted.
	 */
    protected ClosableObservable<T1> source;
    
    /**
     * Keeps track of the connection between the source observer and the source so it
     * can be cancelled later on if desired.
     */
	private Subscription sourceSubscription;

	/**
	 * Observes the source and triggers the properties of this object to be updated when
	 * they change.
	 */
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
		public void onConnectionStatus(boolean isConnected) {
			setConnectionStatus(isConnected);
		}

	};
    
	/**
	 * Set the data source for this observable to get its data from.
	 * 
	 * @param source The source that supplies the raw data that this object transforms
	 */
    protected synchronized void setSource(ClosableObservable<T1> source) {
		cancelSubscription();
		
		closeSource();
		this.source = source;     

        sourceObserver.onConnectionStatus(false);
        sourceObserver.onConnectionStatus(source.isConnected());

        T1 value = source.getValue();
        if (value != null)
        	sourceObserver.onValue(value);

        Exception error = source.currentError();
        if (error != null)
        	sourceObserver.onError(error);
        
		sourceSubscription = source.addObserver(sourceObserver);
    }
	
    /**
     * Transforms the value from the input value from the source observable to the new
     * value supplied by this object.
     * 
     * @param value The new value of the source
     * @return The transformed value supplied by this object
     */
	protected abstract T2 transform(T1 value);

	@Override
	public void close() {
		cancelSubscription();
		closeSource();
        super.close();
	}
	
	/**
	 * Break the connection between this objects source observer and the source
	 * it is currently pointing at.
	 */
	private void cancelSubscription() {
		if (sourceSubscription != null) sourceSubscription.removeObserver();
	}
	
	/**
	 * Close the attached data source. Useful for triggering a whole stack of observables to close
	 * from the top down.
	 */
	private void closeSource() {
		if (source != null) {
	        sourceObserver.onConnectionStatus(false);
			source.close();
		}
	}
	
	/**
	 * Return a human readable value to assist with identifying issues in the observable stack.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " observing source: " + source.toString();
	}
}

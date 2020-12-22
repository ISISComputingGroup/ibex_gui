
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2020 Science & Technology Facilities Council.
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
 * Concatenates two String observables into a single one. 
 * Only goes into error if the first source is in error.
 */
public class ConcatenatingObservable extends ClosableObservable<String> {

    private String firstValue = "";
    private String secondValue = "";
	
    private final Observer<String> firstObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			setValue(concatenate(value, secondValue));
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
	
    private final Observer<String> secondObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
		    setValue(concatenate(firstValue, value));
		}
	};

	private final Subscription firstSubscription;
	private final Subscription secondSubscription;
	
	/**
	 * Concatenates two String observables into a single one.
	 * Only goes into error if the first source is in error.
	 * 
	 * @param firstSource The source for the first string.
	 * @param secondSource The source for the second string.
	 */
	public ConcatenatingObservable(ForwardingObservable<String> firstSource, ForwardingObservable<String> secondSource) {
		firstSubscription = firstSource.subscribe(firstObserver);
		secondSubscription = secondSource.subscribe(secondObserver);
		this.firstValue = firstSource.getValue();
		this.secondValue = secondSource.getValue();
	}
	
	@Override
	public void close() {
		firstSubscription.cancelSubscription();
		secondSubscription.cancelSubscription();
	}
	
	private Boolean isNullOrEmpty(String value) {
	    return (value == null || value.isEmpty());
	}
	
	private String concatenate(String firstValue, String secondValue) {
	    this.firstValue = firstValue;
	    this.secondValue = secondValue;
	    if (isNullOrEmpty(firstValue) && isNullOrEmpty(secondValue)) {
	        return "";
	    } else if (isNullOrEmpty(firstValue)) {
	        return secondValue;
	    } else if (isNullOrEmpty(secondValue)) {
            return firstValue;
        }
	    return firstValue + " " + secondValue;
	}
}

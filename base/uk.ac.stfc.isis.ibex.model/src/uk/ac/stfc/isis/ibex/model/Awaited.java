
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Class to await on a value being valid before reading it.
 *
 * @param <T>
 *            The type of the value to be waited on
 */
public class Awaited<T> extends UpdatedValue<T> {
	
	private final UpdatedValue<T> value;
	private final CountDownLatch latch = new CountDownLatch(1);

	private final PropertyChangeListener set = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setValue(value.getValue());
			latch.countDown();
		}
	};
	
    /**
     * Creates an object to wait on the specified value.
     * 
     * @param value
     *            The value to wait upon.
     */
	public Awaited(final UpdatedValue<T> value) {
		this.value = value;
		if (value.isSet()) {
			setValue(value.getValue());
		} else {
			value.addPropertyChangeListener(set);
		}
	}
	
    /**
     * Gets the awaited value.
     * 
     * @param <T>
     *            the type of the value
     * @param value
     *            the value
     * @param secondsToWait
     *            the number of seconds to wait
     * @return the awaited value
     */
	public static <T> boolean returnedValue(UpdatedValue<T> value, int secondsToWait) {
		return new Awaited<>(value).until(secondsToWait);
	}
	
    /**
     * Waits for a value to be set.
     * 
     * @param secondsToWait
     *            how many seconds to wait for
     * @return true if the value was set
     */
	public boolean until(int secondsToWait) {
		startCountdown(secondsToWait);
		return isSet();
	}
	
	@Override
	public boolean isSet() {
		return value.isSet();
	}

	private void startCountdown(int secondsToWait) {
		try {
			latch.await(secondsToWait, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			value.removePropertyChangeListener(set);
		}
	}
}

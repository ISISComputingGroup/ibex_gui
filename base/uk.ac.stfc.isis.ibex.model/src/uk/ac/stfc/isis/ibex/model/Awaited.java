
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
	
	public Awaited(final UpdatedValue<T> value) {
		this.value = value;
		if (value.isSet()) {
			setValue(value.getValue());
		} else {
			value.addPropertyChangeListener(set);
		}
	}
	
	public static <T> boolean returnedValue(UpdatedValue<T> value, int secondsToWait) {
		return new Awaited<>(value).until(secondsToWait);
	}
	
	/*
	 * Return true if the value was set; otherwise false.
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

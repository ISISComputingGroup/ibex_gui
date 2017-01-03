
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
 * Base for an observer.
 * 
 * @param <T> the generic type that the observer is observing
 */
public abstract class BaseObserver<T> implements Observer<T> {

	@Override
	public void update(T value, Exception error, boolean isConnected) {		
		onConnectionStatus(isConnected);

		if (error != null) {
			onError(error);
			return;
		}

		if (isConnected && value != null) {
			onValue(value);			
		}	
	}

    @Override
    public void onValue(T value) {
        /** Default to no code as some children may not use */
    }

    @Override
    public void onError(Exception e) {
        /** Default to no code as some children may not use */
    }

    @Override
    public void onConnectionStatus(boolean isConnected) {
        /** Default to no code as some children may not use */
    }
}


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
 * The observer interface. This allows an object to observe another object
 * (usually an observable). The observed object will call the events when a
 * value associated with the even changes, e.g. when the connection status is
 * set on the observed object it will call onConnectionStatus.
 *
 */
public interface Observer<T> {
	
    /**
     * Called when the value is set on the observable.
     *
     * @param value the value
     */
    void onValue(T value);
    
    /**
     * Called when the error is set on the observable.
     *
     * @param e the e
     */
    void onError(Exception e);
    
    /**
     * Called when the connection status is set on the observable.
     *
     * @param isConnected the is connected
     */
    void onConnectionStatus(boolean isConnected);

    /**
     * Update the observer to this state, by calling the onEvent handlers. Often
     * called when initialising the observer.
     *
     * @param value the value to set, if there is no error and observer is
     *            connected
     * @param error the error; null if there is no current error
     * @param isConnected True to set the state to connected; False otherwise
     */
    void update(T value, Exception error, boolean isConnected);
}


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
 * The root interface in the Observable hierarchy. Defines that implementors can
 * be subscribed to and should keep a cached copy of its values.
 * 
 * @param <T> The type of the value being observed.
 */
public interface Observable<T> {

    /**
     * Adds an observer that should be notified of changes.
     *
     * @param observer the observer
     * @return the subscription; allowing the observer to stop observing
     */
    Subscription addObserver(Observer<T> observer);

    /**
     * Gets the value of the observer. Often has a special value if the
     * observable has an error or is disconnected.
     *
     * @return the value
     */
    T getValue();

    /**
     * Checks if is connected.
     *
     * @return true, if is connected
     */
    boolean isConnected();

    /**
     * The error which is currently experienced by the observable. The value
     * should not be relied on in the error is set (it may be stale).
     *
     * @return the exception last thrown; null no error
     */
    Exception currentError();
}

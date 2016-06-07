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

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * This class is the abstract skeletal implementation of the implemented
 * interface. It contains a default implementation for the all the required
 * methods.
 *
 * @param <T> The type of the value being observed.
 */
public abstract class ClosableObservable<T> implements Observable<T>, Closable {

    private final Collection<Observer<T>> observers = new CopyOnWriteArrayList<>();
    private T value;
    private boolean isConnected;
    private Exception lastError;

    @Override
    public Subscription addObserver(Observer<T> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }

        return new Unsubscriber<Observer<T>>(observers, observer);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public Exception lastError() {
        return lastError;
    }

    @Override
    public void close() {
        // Do nothing by default
    }

    protected void setValue(T value) {
        if (value == null) {
            return;
        }

        this.value = value;

        for (Observer<T> observer : observers) {
            observer.onValue(value);
        }
    }

    protected void setError(Exception e) {
        lastError = e;

        for (Observer<T> observer : observers) {
            observer.onError(e);
        }
    }

    protected void setConnectionStatus(boolean isConnected) {
        this.isConnected = isConnected;

        for (Observer<T> observer : observers) {
            observer.onConnectionStatus(isConnected);
        }
    }

}

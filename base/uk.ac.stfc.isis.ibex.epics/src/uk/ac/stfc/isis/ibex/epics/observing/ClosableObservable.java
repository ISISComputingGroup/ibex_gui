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

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This class is the abstract skeletal implementation of the implemented
 * interface. It contains a default implementation for the all the required
 * methods.
 *
 * @param <T> The type of the value being observed.
 */
public abstract class ClosableObservable<T> implements Observable<T>, Closable {

	/**
	 *  Use a set so that duplicate observers cannot be added
	 */
    private final Set<Observer<T>> observers = new CopyOnWriteArraySet<>();

    /**
     *  Optional wrapper around the value of this class.
     */
    private Optional<T> value = Optional.empty();

    /**
     *  Whether the observer is connected to it's underlying data source.
     */
    private boolean isConnected;

    /**
     *  Optional exception that occured while connecting to the data source.
     */
    private Optional<Exception> currentError = Optional.empty();

    private static final Logger LOG = IsisLog.getLogger(ClosableObservable.class);

    /**
     * Adds an observer to this observable
     */
    @Override
    public Subscription subscribe(Observer<T> observer) {
        observers.add(observer);

        // When a new observer is added, update it with the existing observable data
        logErrorsAndContinue(() -> observer.update(getValue(), currentError(), isConnected()));
        return new Unsubscriber<>(this, observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void unsubscribe(Observer<T> observer) {
        observers.remove(observer);
    }

    /**
     * Returns the current value of this observable.
     */
    @Override
    public T getValue() {
        return value.orElse(null);
    }

    /**
     * Returns whether this observable is connected.
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Returns the current error status of this observable.
     */
    @Override
    public Exception currentError() {
        return currentError.orElse(null);
    }

    /**
     * Closes this observable. This removes any observers which are currently
     * registered to this observable, such that they will no longer receive updates.
     *
     * As a side effect, this will make the observers eligible for garbage collection
     * (unless there is a reference to them elsewhere in the code).
     */
    @Override
    public void close() {
    	for (Observer<T> observer : observers) {
    		// Give one final update that the observer is no longer connected.
            logErrorsAndContinue(() -> observer.onConnectionStatus(false));
        }
        observers.clear();
    }

    /**
     * Sets the value. Also blank any error
     *
     * @param value the new value
     */
    protected void setValue(T value) {
    	this.value = Optional.ofNullable(value);
    	this.value.ifPresent(val -> currentError = Optional.empty());

    	for (Observer<T> observer : observers) {
    		logErrorsAndContinue(() -> this.value.ifPresent(observer::onValue));
    	}
    }

    /**
     * Sets the current error experienced by this observable. Error is blanked
     * if a new value is set
     *
     * @param e the new error
     */
    protected void setError(Exception e) {
        currentError = Optional.ofNullable(e);

        for (Observer<T> observer : observers) {
            logErrorsAndContinue(() -> observer.onError(e));
        }
    }

    /**
     * Sets the connection status.
     *
     * @param isConnected the new connection status
     */
    protected void setConnectionStatus(boolean isConnected) {
        this.isConnected = isConnected;

        for (Observer<T> observer : observers) {
            logErrorsAndContinue(() -> observer.onConnectionStatus(isConnected));
        }
    }

    /**
     * Runs a runnable, and if it throws errors then log the error and carry on.
     * @param runnable the runnable to run.
     */
    private static void logErrorsAndContinue(Runnable runnable) {
    	try {
    		runnable.run();
    	} catch (RuntimeException e) {
    		LoggerUtils.logErrorWithStackTrace(LOG, "Exception while updating observer: " + e.getMessage(), e);
    	}
    }

}

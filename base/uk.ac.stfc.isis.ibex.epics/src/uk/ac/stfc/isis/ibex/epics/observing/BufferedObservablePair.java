
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * Links together two related, interdependent observables into a single
 * observable and buffers values so that observers are not notified until both
 * observables have a non-null value.
 *
 * @param <T1> The type of the first value being observed.
 * @param <T2> The type of the second value being observed.
 */
public class BufferedObservablePair<T1, T2> extends ObservablePair<T1, T2> {

    private Pair<T1, T2> buffer;
    
    /**
     * Calls superclass constructor.
     * 
     * @param firstSource the first observable
     * @param secondSource the second observable
     */
    public BufferedObservablePair(ForwardingObservable<T1> firstSource, ForwardingObservable<T2> secondSource) {
        super(firstSource, secondSource);
    }

    /**
     * Updates buffer with the newly received value pair. Properly sets the
     * value and notifies observers once both values in the buffer pair are
     * non-null.
     * 
     * @param value the new value pair
     */
    @Override
    protected void setValue(Pair<T1, T2> value) {
        if (buffer == null) {
            buffer = new Pair<T1, T2>(null, null);
        }
        this.buffer = collateWithBuffer(value);

        if (checkNotNull()) {
            super.setValue(buffer);
        }
    }

    /**
     * Collates the newly received value pair with values already present in the
     * buffer.
     * 
     * @param pair the new value pair
     * @return the new value pair with missing values filled in from the buffer.
     */
    private Pair<T1, T2> collateWithBuffer(Pair<T1, T2> pair) {
        T1 first = collateFirst(pair.first);
        T2 second = collateSecond(pair.second);
        return new Pair<T1, T2>(first, second);
    }

    /**
     * Sets the most recent available value as the first value in the pair in
     * the order: new value received from observable, value from buffer, null.
     * 
     * @param value the new value
     * @return the most recent available value
     */
    private T1 collateFirst(T1 value) {
        T1 first = buffer.first;
        if (value != null) {
            first = value;
        }
        return first;
    }

    /**
     * Sets the most recent available value as the second value in the pair in
     * the order: new value received from observable, value from buffer, null.
     * 
     * @param value the new value
     * @return the most recent available value
     */
    private T2 collateSecond(T2 value) {
        T2 second = buffer.second;
        if (value != null) {
            second = value;
        }
        return second;
    }

    /**
     * Checks whether both values in the pair are set.
     * 
     * @return true if both values in the pair are set, false if at least one is
     *         still null.
     */
    private Boolean checkNotNull() {
        return (buffer.first != null && buffer.second != null);
    }
}

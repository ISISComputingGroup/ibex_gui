
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

package uk.ac.stfc.isis.ibex.instrument;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * Provides a number of utility methods for working with observables and
 * writables.
 */
public final class InstrumentUtils {

    private InstrumentUtils() {
    }

    /**
     * Provides an observable that converts data from another observable.
     * 
     * @param <S>
     *            The type to convert from.
     * @param <T>
     *            The type to convert to.
     * @param observable
     *            the original observable
     * @param converter
     *            the converter
     * @return the new observable
     */
    public static <S, T> ForwardingObservable<T> convert(ClosableObservable<S> observable,
            Converter<S, T> converter) {
        return new ForwardingObservable<>(new ConvertingObservable<>(observable, converter));
	}

    /**
     * Provides a writable that converts data and sends to another writable.
     * 
     * @param <S>
     *            The type of data to convert to
     * @param <T>
     *            The type of data to convert from
     * @param destination
     *            The writable to send the data to.
     * @param converter
     *            The converter
     * @return The new writable
     */
    public static <S, T> Writable<T> convert(Writable<S> destination, Converter<T, S> converter) {
        return new ForwardingWritable<>(destination, converter);
    }

    /**
     * Adds the current instrument prefix to a pv address.
     * 
     * @param address
     *            The address to add the prefix to.
     * @return The address with the prefix added.
     */
    public static String addPrefix(String address) {
        StringBuilder sb = new StringBuilder();
        sb.append(Instrument.getInstance().getPvPrefix());
        sb.append(address);
        return sb.toString();
    }
}

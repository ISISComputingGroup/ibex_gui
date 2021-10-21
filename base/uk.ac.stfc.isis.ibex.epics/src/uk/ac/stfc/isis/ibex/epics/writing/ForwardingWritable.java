
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

package uk.ac.stfc.isis.ibex.epics.writing;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

/**
 * Forwards the values written to it to another writable (only if the original
 * and transformed values are not null. With conversion.
 *
 * @param <TIn>
 *            the type of data coming in
 * @param <TOut>
 *            the type of data to output
 */
public class ForwardingWritable<TIn, TOut> extends BaseWritable<TIn> {

    private Optional<Writable<TOut>> destination = Optional.empty();
    
    private OnCanWriteChangeListener canWriteChangedListener = canWrite -> canWriteChanged(canWrite);
    private OnErrorListener onErrorListener = e -> error(e);
	
    private final Function<TIn, TOut> converter;

    /**
     * Constructor.
     * 
     * @param destination
     *            the destination
     * @param converter
     *            converts types from In to Out
     */
    public ForwardingWritable(Writable<TOut> destination, Function<TIn, TOut> converter) {
        this.converter = converter;
        setWritable(destination);
    }

	@Override
	public void write(TIn value) throws IOException {
		if (value != null) {
            TOut tranformedValue = transform(value);
            if (tranformedValue != null) {
                if (destination.isPresent()) {
                    destination.get().write(tranformedValue);
                }
            }
		}
	}
	
	@Override
	public void close() {
		cancelSubscriptions();
        closeResource();
	}
	
    public void setWritable(Writable<TOut> destination) {
        checkPreconditions(destination);

		cancelSubscriptions();
		canWriteChangedListener.onCanWriteChanged(false);
		canWriteChangedListener.onCanWriteChanged(destination.canWrite());
		
        destination.addOnCanWriteChangeListener(canWriteChangedListener);
        destination.addOnErrorListener(onErrorListener);

        closeResource();
        this.destination = Optional.of(destination);
	}

    private TOut transform(TIn value) {
        try {
            return converter.apply(value);
        } catch (ConversionException e) {
            error(e);
        }

        return null;
    }

	private void cancelSubscriptions() {
	    destination.ifPresent(dest -> {
	        dest.removeOnCanWriteChangeListener(canWriteChangedListener);
	        dest.removeOnErrorListener(onErrorListener);
	    });
	}

    private void closeResource() {
        destination.ifPresent(dest -> dest.close());
    }

    private void checkPreconditions(Writable<TOut> destination) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination writable cannot be null.");
        }
    }
}


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

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * Forwards the values written to it to another writable (only if the orginal
 * and transformed values are not null. With conversion.
 *
 * @param <TIn>
 *            the type of data coming in
 * @param <TOut>
 *            the type of data to outpu
 */
public class ForwardingWritable<TIn, TOut> extends BaseWritable<TIn> {

    private Closable resource;

	private ConfigurableWriter<TIn, TOut> forwardingWriter = new BaseWriter<TIn, TOut>() {
		@Override
		public void write(TIn value) throws IOException {
            TOut tranformedValue = transform(value);
            if (tranformedValue != null) {
                writeToWritables(tranformedValue);
            }
		}
		
		@Override
        public void onError(Exception e) {
			error(e);
		};
		
		@Override
        public void onCanWriteChanged(boolean canWrite) {
			canWriteChanged(canWrite);
		};
	};
	
    private Subscription readingSubscription;
	private Subscription writingSubsciption;
	
    private final Converter<TIn, TOut> converter;

    /**
     * Constructor.
     * 
     * @param destination
     *            the destination
     * @param converter
     *            converts types from In to Out
     */
    public ForwardingWritable(Writable<TOut> destination, Converter<TIn, TOut> converter) {
        this.converter = converter;
        setWritable(destination);
    }

	@Override
	public void write(TIn value) throws IOException {
		if (value != null) {
			forwardingWriter.write(value);
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
		forwardingWriter.onCanWriteChanged(false);
		forwardingWriter.onCanWriteChanged(destination.canWrite());
		
        readingSubscription = destination.subscribe(forwardingWriter);
		writingSubsciption = forwardingWriter.writeTo(destination);

        closeResource();
        resource = destination;
	}

    private TOut transform(TIn value) {
        try {
            return converter.convert(value);
        } catch (ConversionException e) {
            error(e);
        }

        return null;
    }

	private void cancelSubscriptions() {
        if (readingSubscription != null) {
            readingSubscription.removeObserver();
        }

		if (writingSubsciption != null) {
			writingSubsciption.removeObserver();
		}
	}

    private void closeResource() {
        if (resource != null) {
            resource.close();
        }
    };

    private void checkPreconditions(Writable<TOut> destination) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination writable cannot be null.");
        }
    }
}

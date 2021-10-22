
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

/**
 * Forwards the values written to it to another writable (only if the original
 * and transformed values are not null. With conversion.
 *
 * @param <TIn>
 *            the type of data coming in
 * @param <TOut>
 *            the type of data to output
 */
public abstract class TransformingWritable<TIn, TOut> extends BaseWritable<TIn> {
    private OnCanWriteChangeListener canWriteChangedListener = canWrite -> canWriteChanged(canWrite);
    private OnErrorListener onErrorListener = e -> error(e);
    
    protected Optional<Writable<TOut>> destination = Optional.empty();
    
    protected abstract TOut transform (TIn input);
    
    public TransformingWritable(Writable<TOut> destination) {
        setWritable(destination);
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
	
	private void cancelSubscriptions() {
        destination.ifPresent(dest -> {
            dest.removeOnCanWriteChangeListener(canWriteChangedListener);
            dest.removeOnErrorListener(onErrorListener);
        });
    }

    private void closeResource() {
        destination.ifPresent(dest -> dest.close());
        destination = Optional.empty();
    }

    private void checkPreconditions(Writable<TOut> destination) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination writable cannot be null.");
        }
    }
}

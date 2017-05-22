
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
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

/**
 * An abstract class for defining basic writer classes.
 * 
 * Note: Can hold multiple Writables which will have the same value written to
 * them. This is NOT currently used in IBEX.
 *
 * @param <TIn> the type of data coming in
 * @param <TOut> the type of data to output
 */
public abstract class BaseWriter<TIn, TOut> implements ConfigurableWriter<TIn, TOut> {
		
	private final Collection<Writable<TOut>> writables = new CopyOnWriteArrayList<>();
		
	private TOut lastWritten;
	private Exception lastError;
	private boolean canWrite;
	
	public TOut lastWritten() {
		return lastWritten;
	}
	
	public Exception lastError() {
		return lastError;
	}	
	
	@Override
    public boolean canWrite() {
		return canWrite;
	}

	@Override
	public Subscription writeTo(Writable<TOut> writable) {
		if (!writables.contains(writable)) {
			writables.add(writable);
		}
		
		return new Unsubscriber<Writable<TOut>>(writables, writable);
	}

	@Override
	public void onError(Exception e) {
		lastError = e;
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {		
		this.canWrite = canWrite;
	}
	
	protected void writeToWritables(TOut value) throws IOException {
		lastWritten = value;
		for (Writable<TOut> writable : writables) {
			writable.write(value);
		}
	}
	
	protected void uncheckedWriteToWritables(TOut value) {
        lastWritten = value;
        for (Writable<TOut> writable : writables) {
            writable.uncheckedWrite(value);
        }
    }
	
	public void uncheckedWrite(TIn value){
	    try {
	        write(value);
	    } catch (IOException e) {
	        // Rethrow the exception as an unchecked (runtime) exception
	        throw new RuntimeException("Unchecked write failed.", e);
	    }
	}
}

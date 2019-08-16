
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
import java.util.HashSet;
import java.util.Set;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.observing.Unsubscriber;

/**
 * Base implementation of the writable interface.
 * 
 * THREAD SAFETY NOTE:
 *   All subclasses of this class are required to be thread-safe using explicit locking. Subclasses can share
 *   this class' lock object.
 * @param <T> the type of the writable
 */
public abstract class BaseWritable<T> implements Writable<T> {

	private final Set<ConfigurableWriter<?, ?>> writers = new HashSet<>();

	private boolean canWrite;
	private Exception lastError;
	
	@Override
	public synchronized Subscription subscribe(ConfigurableWriter<?, ?> writer) {
		writer.onCanWriteChanged(canWrite());
		writer.onError(lastError());
		writers.add(writer); // As a set is used, duplicates won't be added.
		return new Unsubscriber<ConfigurableWriter<?, ?>>(writers, writer);
	}
	
	@Override
    public synchronized boolean canWrite() {
		return canWrite;
	}
	
	/**
	 * @return the last exception that occurred.
	 */
	public synchronized Exception lastError() {
		return lastError;
	}
	
	protected synchronized void error(Exception e) {
		lastError = e;
		writers.forEach(writer -> writer.onError(e));
	}
	
	/**
	 * Method triggered when the write status is changed.
	 * 
	 * @param canWrite whether the writable can write or not
	 */
	protected synchronized void canWriteChanged(boolean canWrite) {
		this.canWrite = canWrite;
		writers.forEach(writer -> writer.onCanWriteChanged(canWrite));
	}
	
	@Override
    public synchronized void uncheckedWrite(T value) {
	    try {
	        write(value);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}

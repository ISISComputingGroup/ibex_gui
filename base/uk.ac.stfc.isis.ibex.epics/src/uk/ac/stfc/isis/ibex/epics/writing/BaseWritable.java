
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

public abstract class BaseWritable<T> implements Writable<T> {

	private final Collection<ConfigurableWriter<?, ?>> writers = new CopyOnWriteArrayList<>();

	private boolean canWrite;
	private Exception lastError;
	
	@Override
	public Subscription subscribe(ConfigurableWriter<?, ?> writer) {
		writer.onCanWriteChanged(canWrite);
		writer.onError(lastError);
		
		if (!writers.contains(writer)) {
			writers.add(writer);
		}
		
		return new Unsubscriber<ConfigurableWriter<?, ?>>(writers, writer);
	}
	
	@Override
    public boolean canWrite() {
		return canWrite;
	}
	
	public Exception lastError() {
		return lastError;
	}
	
	protected void error(Exception e) {
		lastError = e;
		for (ConfigurableWriter<?, ?> writer : writers) {
			writer.onError(e);
		}
	}
	
	protected void canWriteChanged(boolean canWrite) {
		this.canWrite = canWrite;
		for (ConfigurableWriter<?, ?> writer : writers) {
			writer.onCanWriteChanged(canWrite);
		}
	}
	
	@Override
    public void uncheckedWrite(T value) {
	    try {
	        write(value);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}

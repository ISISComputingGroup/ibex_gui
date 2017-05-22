
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

import org.apache.logging.log4j.Logger;

public class LoggingForwardingWriter<T> extends ForwardingWriter<T, T> {

	protected final Logger log;
	protected final String id;
	
	public LoggingForwardingWriter(Logger log, String id, ConfigurableWriter<T, T> writer) {

        checkPreconditions(log, writer);

		this.log = log;
		this.id = id;
		setWriter(writer);
	}

    @Override
	public void write(T value) throws IOException {
		logValue(value);
		super.write(value);
	}
	
	@Override
	public void onError(Exception e) {
		log(e.toString());
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {		
		// do nothing
	}

	protected void logValue(T value) {
		log(value.toString());
	}

	private void log(String text) {
		log.info(id + " " + text);
	}

    private void checkPreconditions(Logger inputLogger, ConfigurableWriter<T, T> inputWriter) {
        if (inputLogger == null) {
            throw new IllegalArgumentException("Logger can not be null.");
        }

        if (inputWriter == null) {
            throw new IllegalArgumentException("Writer can not be null.");
        }
    }
}


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
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

/**
 * A writable which logs it's values to the logfile.
 * 
 * @param <T> the type of values to write
 */
public class LoggingForwardingWritable<T> extends TransformingWritable<T, T> {

	private final Logger log;
	private final String id;

	/**
	 * Creates a new logging writable.
	 * @param log the log to write messages to
	 * @param id an ID to be printed before each log message
	 * @param destination the destination writable
	 * @param converter a conversion function
	 */
    public LoggingForwardingWritable(Logger log, String id, Writable<T> destination, Function<T, T> converter) {
        super(destination, converter);
        this.log = log;
		this.id = id;
	}
	
	@Override
	public void write(T value) throws IOException {
		super.write(value);
		log(value.toString());
	}

	private void log(String text) {
		log.info(id + " " + text);
	}
}

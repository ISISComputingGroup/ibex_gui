
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

package uk.ac.stfc.isis.ibex.epics.observing;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;


/**
 * Writes any changes to the observable to a logger.
 *
 * @param <T>
 *            The type of the value that you are observing.
 */
public class LoggingObserver<T> extends BaseObserver<T> {

	/**
	 * The logger.
	 */
	protected final Logger log;
	
	/**
	 * An ID printed before each log message.
	 */
	protected final String id;

	/**
	 * The log level.
	 */
	protected final Level level;

	/**
	 * Create a new logging observer.
	 * @param log the log
	 * @param id an ID printed before each message
	 */
	public LoggingObserver(Logger log, String id) {
		this(log, id, null);
	}

	/**
	 * Create a new logging observer.
	 * @param log the log
	 * @param id an ID printed before each message
	 * @param level the log level
	 */
	public LoggingObserver(Logger log, String id, Level level) {
		this.log = log;
		this.id = id;
		this.level = level;
	}

	@Override
	public void onValue(T value) {
		if (null != level) {
			log.log(level, id + " value: " + value);
		} else {
			log.info(id + " value: " + value);
		}
	}

	@Override
	public void onError(Exception e) {
		log.error(id + " error: " + e);
	}

	@Override
	public void onConnectionStatus(boolean isConnected) {
		log.info(id + " connected: " + isConnected);
	}
}

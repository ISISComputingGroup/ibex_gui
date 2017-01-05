
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

import org.apache.logging.log4j.Logger;


/**
 * Writes any changes to the observable to a logger.
 *
 * @param <T>
 *            The type of the value that you are observing.
 */
public class LoggingObserver<T> extends BaseObserver<T> {

	protected final Logger log;
	protected final String id;
	
	public LoggingObserver(Logger log, String id) {
		this.log = log;
		this.id = id;
	}
	
	@Override
	public void onValue(T value) {
		log.info(id + " value: " + value);
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

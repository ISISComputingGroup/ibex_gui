
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;

/**
 * A configuration observer which logs it's values to the logfile.
 */
public class LoggingConfigurationObserver extends LoggingObserver<Configuration> {

	/**
	 * Creates the observer.
	 * @param log the log to write values to
	 * @param id an id printed before each message
	 */
	public LoggingConfigurationObserver(Logger log, String id) {
		super(log, id);
	}

	@Override
	public void onValue(Configuration value) {
		super.onValue(value);
		try {
			log.info(id + " " + new JsonSerialisingConverter<>(Configuration.class).apply(value).toString());
		} catch (ConversionException e) {
			log.info(id + " " + e.toString());
		}
	}
}


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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.logger.preferences;

import org.apache.logging.log4j.Level;
import org.eclipse.core.runtime.Platform;

/**
 * Constant definitions for plug-in preferences
 */
public final class PreferenceConstants {
	public static final String P_LOG_DIR = "logDir";
	public static final String P_LOG_FILE = "logFile";
	public static final String P_MESSAGE_PATTERN = "msgPattern";
	public static final String P_ARCHIVE_PATTERN = "archiveFilePattern";
	public static final String P_MAX_FILE_SIZE = "maxFileSize";
	public static final String P_MAX_ARCHIVE_PER_DAY = "archivePerDay";
	public static final String P_LOGGING_LEVEL = "loggingLevel";

	public static final String LABEL_LOG_DIR = "Log Directory";
	public static final String LABEL_LOG_FILE = "Rolling Log File";
	public static final String LABEL_MESSAGE_PATTERN = "Log4j Message Pattern";
	public static final String LABEL_ARCHIVE_PATTERN = "Log4j Archive Pattern";
	public static final String LABEL_MAX_FILE_SIZE = "Max File Size (kb)";
	public static final String LABEL_MAX_ARCHIVE_PER_DAY = "Max Files Per Day";
	public static final String LABEL_LOGGING_LEVEL = "Logging Level";

	public static final String DEFAULT_LOG_DIR = getLogDir();
	public static final String DEFAULT_LOG_FILE = "isis.log";
	public static final String DEFAULT_MESSAGE_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %m%n";
	public static final String DEFAULT_ARCHIVE_PATTERN = "%d{yyyy-MM}/isis-%d{yyyy-MM-dd}--%i.log";
	public static final int DEFAULT_MAX_FILE_SIZE = 1024;
	public static final int DEFAULT_MAX_ARCHIVE_PER_DAY = 20;
	public static final String DEFAULT_LOGGING_LEVEL = Level.ALL.name();
	
	private PreferenceConstants() { }

	static final String[][] LOGGING_LEVELS = new String[][] {
			{Level.ALL.name(), Level.ALL.name()},
			{Level.TRACE.name(), Level.TRACE.name()},
			{Level.DEBUG.name(), Level.DEBUG.name()},
			{Level.INFO.name(), Level.INFO.name()},
			{Level.WARN.name(), Level.WARN.name()},
			{Level.ERROR.name(), Level.ERROR.name()},
			{Level.FATAL.name(), Level.FATAL.name()},
			{Level.OFF.name(), Level.OFF.name()}, };
	
	private static String getLogDir() {
		return Platform.getInstanceLocation().getURL().getPath() + "logs/";
	}
}

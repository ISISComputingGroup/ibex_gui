
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
package uk.ac.stfc.isis.ibex.logger.preferences;

import org.apache.logging.log4j.Level;
import org.eclipse.core.runtime.Platform;

/**
 * Constant definitions for plug-in preferences.
 */
public final class PreferenceConstants {

    /**
     * The log directory preference.
     */
	public static final String P_LOG_DIR = "logDir";

    /**
     * The log file preference.
     */
	public static final String P_LOG_FILE = "logFile";

    /**
     * The message pattern preference.
     */
	public static final String P_MESSAGE_PATTERN = "msgPattern";

    /**
     * The archive file pattern preference.
     */
	public static final String P_ARCHIVE_PATTERN = "archiveFilePattern";

    /**
     * The max file size preference.
     */
	public static final String P_MAX_FILE_SIZE = "maxFileSize";
    /**
     * The maximum archive files per day preference.
     */
	public static final String P_MAX_ARCHIVE_PER_DAY = "archivePerDay";

    /**
     * The logging level preference.
     */
	public static final String P_LOGGING_LEVEL = "loggingLevel";

    /**
     * The label text accompanying the log directory preference.
     */
	public static final String LABEL_LOG_DIR = "Log Directory";

    /**
     * The label text accompanying the log file preference.
     */
	public static final String LABEL_LOG_FILE = "Rolling Log File";

    /**
     * The label text accompanying the log message pattern preference.
     */
	public static final String LABEL_MESSAGE_PATTERN = "Log4j Message Pattern";

    /**
     * The label text accompanying the log archive pattern preference.
     */
	public static final String LABEL_ARCHIVE_PATTERN = "Log4j Archive Pattern";

    /**
     * The label text accompanying the max file size preference.
     */
	public static final String LABEL_MAX_FILE_SIZE = "Max File Size (kb)";

    /**
     * The label text accompanying the max archive files per day preference.
     */
	public static final String LABEL_MAX_ARCHIVE_PER_DAY = "Max Files Per Day";

    /**
     * The label text accompanying the log directory preference.
     */
	public static final String LABEL_LOGGING_LEVEL = "Logging Level";

    /**
     * The default log directory.
     */
	public static final String DEFAULT_LOG_DIR = getLogDir();

    /**
     * The default log file.
     */
	public static final String DEFAULT_LOG_FILE = "isis.log";

    /**
     * The default message pattern.
     */
	public static final String DEFAULT_MESSAGE_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %m%n";

    /**
     * The default archive pattern.
     */
	public static final String DEFAULT_ARCHIVE_PATTERN = "%d{yyyy-MM}/isis-%d{yyyy-MM-dd}--%i.log";

    /**
     * The default max file size.
     */
	public static final int DEFAULT_MAX_FILE_SIZE = 1024;

    /**
     * The default max archive files per day.
     */
	public static final int DEFAULT_MAX_ARCHIVE_PER_DAY = 20;

    /**
     * The default logging level.
     */
	public static final String DEFAULT_LOGGING_LEVEL = Level.ALL.name();
	
	private PreferenceConstants() { }

    /**
     * The defined logging levels.
     */
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


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
package uk.ac.stfc.isis.ibex.logger.config;

import java.io.Serializable;
import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.preferences.PreferenceConstants;

public final class LoggingConfiguration {
	
	private LoggingConfiguration() { }
	
	public static void configure() {
		Log4j2ConfigurationFactory factory = new Log4j2ConfigurationFactory();
		ConfigurationFactory.setConfigurationFactory(factory);
	}

	public static class Log4j2ConfigurationFactory extends ConfigurationFactory {

		@Override
		protected String[] getSupportedTypes() {
			return null;
		}

		@Override
		public Configuration getConfiguration(ConfigurationSource source) {
			return new Log4j2Configuration();
		}

		@Override
		public Configuration getConfiguration(String name, URI configLocation) {
			return new Log4j2Configuration();
		}
	}

	/**
	 * log4j logging configuration. Logs to files and to console
	 */
	private static class Log4j2Configuration extends DefaultConfiguration {

		public Log4j2Configuration() {
			setName("isis-log4j2");
			getRootLogger().setLevel(Level.INFO);

			// Get logging preferences
			IPreferenceStore prefs = IsisLog.getDefault().getPreferenceStore();

			String directory = prefs.getString(PreferenceConstants.P_LOG_DIR);
			String filename = prefs.getString(PreferenceConstants.P_LOG_FILE);
			String patternLayout = prefs
					.getString(PreferenceConstants.P_MESSAGE_PATTERN);
			String archivePattern = prefs
					.getString(PreferenceConstants.P_ARCHIVE_PATTERN);
			int maxFiles = prefs
					.getInt(PreferenceConstants.P_MAX_ARCHIVE_PER_DAY);
			int fileSize = prefs.getInt(PreferenceConstants.P_MAX_FILE_SIZE);
			String level = prefs.getString(PreferenceConstants.P_LOGGING_LEVEL);

			// Apply minimums to int values
			if (maxFiles < 1) {
				maxFiles = 1;
			}

			if (fileSize < 32) {
				fileSize = 32;
			}

			Level loggingLevel = Level.getLevel(level);

			patternLayout = "*" + patternLayout;
			filename = directory + "/" + filename;
			String pattern = directory + "/" + archivePattern;
			String fileSizeInBytes = fileSize * 1024 + "";
			String numLogFiles = maxFiles + "";

			// Make pattern layout
			Layout<? extends Serializable> layout = PatternLayout.createLayout(
					patternLayout, this, null, null, false, false, null, null);

			// Make Console Appender
			Appender console = ConsoleAppender.createAppender(layout, null,
					"SYSTEM_OUT", "Console", "true", null);
			addAppender(console);
			getRootLogger().addAppender(console, loggingLevel, null);

			// Make File Appender
			final TimeBasedTriggeringPolicy timeBasedTriggeringPolicy = TimeBasedTriggeringPolicy
					.createPolicy("1", "false");
			final SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = SizeBasedTriggeringPolicy
					.createPolicy(fileSizeInBytes);
			final CompositeTriggeringPolicy policy = CompositeTriggeringPolicy
					.createPolicy(timeBasedTriggeringPolicy,
							sizeBasedTriggeringPolicy);

			final DefaultRolloverStrategy strategy = DefaultRolloverStrategy
					.createStrategy(numLogFiles, "1", null, null, this);

			Appender fileAppender = RollingFileAppender.createAppender(
					filename, pattern, "true", "isis-log-file-appender",
					"true", fileSizeInBytes, "true", policy, strategy, layout,
					null, "true", null, null, this);

			addAppender(fileAppender);
			getRootLogger().addAppender(fileAppender, loggingLevel, null);
		}
	}
}
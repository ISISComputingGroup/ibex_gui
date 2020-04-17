
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender.Target;
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
		System.setProperty("log4j.configurationFactory", Log4j2ConfigurationFactory.class.getName());
	}

	public static class Log4j2ConfigurationFactory extends ConfigurationFactory {

		@Override
		public Configuration getConfiguration(LoggerContext ignored, ConfigurationSource source) {
			return new Log4j2Configuration();
		}

		@Override
		protected String[] getSupportedTypes() {
			return null;
		}
	}

	/**
	 * log4j logging configuration. Logs to files and to console
	 */
	private static class Log4j2Configuration extends DefaultConfiguration {

        private static final int MIN_FILE_SIZE_KB = 32;
        private static final int BYTES_PER_KB = 1024;

        Log4j2Configuration() {
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
			int fileSizeMB = prefs.getInt(PreferenceConstants.P_MAX_FILE_SIZE);
			String level = prefs.getString(PreferenceConstants.P_LOGGING_LEVEL);

			// Apply minimums to int values
			if (maxFiles < 1) {
				maxFiles = 1;
			}

            if (fileSizeMB < MIN_FILE_SIZE_KB) {
                fileSizeMB = MIN_FILE_SIZE_KB;
			}

			Level loggingLevel = Level.getLevel(level);

			patternLayout = "*" + patternLayout;
			filename = directory + "/" + filename;
			String pattern = directory + "/" + archivePattern;
			int fileSizeInBytes = fileSizeMB * BYTES_PER_KB;
			
			Layout<? extends Serializable> layout = PatternLayout.newBuilder()
					.withPattern(patternLayout)
					.withConfiguration(this)
					.withAlwaysWriteExceptions(false)
					.withNoConsoleNoAnsi(false)
					.build();
			
			Appender console = ConsoleAppender.newBuilder()
			    .setLayout(layout)
			    .setTarget(Target.SYSTEM_OUT)
			    .setName("Console")
			    .setFollow(true)
			    .setConfiguration(this)
			    .build();
			
			addAppender(console);
			getRootLogger().addAppender(console, loggingLevel, null);

		
			final TimeBasedTriggeringPolicy timeBasedTriggeringPolicy = TimeBasedTriggeringPolicy.newBuilder()
				.withInterval(1)
				.withModulate(false)
				.build();
			
			final SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = SizeBasedTriggeringPolicy
					.createPolicy(Integer.toString(fileSizeInBytes));
			final CompositeTriggeringPolicy policy = CompositeTriggeringPolicy
					.createPolicy(timeBasedTriggeringPolicy,
							sizeBasedTriggeringPolicy);
			
			final DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
			    .withMax(Integer.toString(maxFiles))
			    .withMin(Integer.toString(1))
			    .withConfig(this)
			    .build();
			    
			
			final Appender fileAppender = RollingFileAppender.newBuilder()
				.withFileName(filename)
				.withFilePattern(pattern)
				.withAppend(true)
				.setName("isis-log-file-appender")
				.withBufferedIo(true)
				.withBufferSize(fileSizeInBytes)
				.withImmediateFlush(true)
				.withPolicy(policy)
				.withStrategy(strategy)
				.setLayout(layout)
				.setConfiguration(this)
				.build();

			addAppender(fileAppender);
			getRootLogger().addAppender(fileAppender, loggingLevel, null);
		}
	}
}

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
package uk.ac.stfc.isis.ibex.ui.log.save;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/**
 * Writes a list of IOC log messages to a log file
 */
public class LogMessageFileWriter {
	/** Title displayed at the top of the log file */
	private static final String TITLE = "### ISIS IOC Message Log ###";

	/**
	 * Ordered list of log message fields to be displayed in the log file.
	 */
	private static final LogMessageFields[] COLUMNS = {
			LogMessageFields.EVENT_TIME,
			// LogMessageFields.CREATE_TIME,
			LogMessageFields.TYPE, LogMessageFields.SEVERITY,
			LogMessageFields.CLIENT_NAME, LogMessageFields.CLIENT_HOST,
			// LogMessageFields.APPLICATION_ID,
			LogMessageFields.CONTENTS };

	/** Separation (in characters) between each adjacent column */
	private static final int COL_SEP = 3;

	/** The widths (in characters) of the columns */
	private int[] columnWidths;

	/**
	 * Save the list of log messages to a log file
	 */
	public boolean saveLogFile(List<LogMessage> messages, String filename) {
		PrintWriter out = null;
		boolean success = true;

		try {
			String contents = getLogFileText(messages);
			out = new PrintWriter(filename);

			out.print(contents);
		} catch (FileNotFoundException e) {
			success = false;
		} finally {
			if (out != null) {
				out.close();
			}
		}

		return success;
	}

	/**
	 * Return a string representation of the log file list.
	 */
	public String getLogFileText(List<LogMessage> messages) {
		// Determine how wide each column needs to be
		calculateWidths(messages);

		String formatString = getFormatString();
		String hyphenLine = getHyphenLine();
		String lineSep = System.getProperty("line.separator");

		// Get current time to print at tope of file
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = dateFormat.format(new Date());

		// Print file title and creation date
		StringBuilder fileContents = new StringBuilder();
		fileContents.append(TITLE + lineSep + lineSep);
		fileContents.append("created: " + date + lineSep + lineSep);

		// Print column labels
		fileContents.append(hyphenLine + lineSep);
		String columnTitles = String.format(formatString, (Object[]) COLUMNS);
		fileContents.append(columnTitles + lineSep);
		fileContents.append(hyphenLine);

		// Print each log messasge
		for (LogMessage msg : messages) {
			String line = String.format(formatString,
					msg.getProperties(COLUMNS));

			fileContents.append(lineSep);
			fileContents.append(line);
		}

		return fileContents.toString();
	}

	/**
	 * Calculates the minimum width of each column (which is equal to the length
	 * of the longest element in the column).
	 */
	private void calculateWidths(List<LogMessage> messages) {
		columnWidths = new int[COLUMNS.length];
		for (int c = 0; c < COLUMNS.length; ++c) {
			columnWidths[c] = COLUMNS[c].toString().length();
		}

		for (LogMessage msg : messages) {
			for (int c = 0; c < COLUMNS.length; ++c) {
				LogMessageFields field = COLUMNS[c];
				int length = msg.getProperty(field).length();

				if (length > columnWidths[c]) {
					columnWidths[c] = length;
				}
			}
		}
	}

	/**
	 * Get a format string that can be used in String.format. Sets the field
	 * width for each column.
	 */
	private String getFormatString() {
		StringBuilder formatString = new StringBuilder();

		for (int width : columnWidths) {
			formatString.append("%-" + (width + COL_SEP) + "s ");
		}

		return formatString.toString();
	}

	/**
	 * Get a string that consists of a line of hyphens long enough to cover
	 * every column
	 */
	private String getHyphenLine() {
		int totalLength = (COL_SEP + 1) * COLUMNS.length;
		for (int width : columnWidths) {
			totalLength += width;
		}

		return new String(new char[totalLength]).replace("\0", "-");
	}
}

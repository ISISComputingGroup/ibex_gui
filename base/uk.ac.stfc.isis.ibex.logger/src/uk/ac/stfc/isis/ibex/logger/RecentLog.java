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
package uk.ac.stfc.isis.ibex.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.logger.preferences.PreferenceConstants;

public class RecentLog implements IRecentLog {
	@Override
	public String getLogText() {
		String content = null;
		String filename = getLogFileName();
		File file = new File(filename);

		try (FileReader filereader = new FileReader(file)) {
			try (BufferedReader reader = new BufferedReader(filereader)) {
				// Get file contents as a list of lines
				LinkedList<String> lines = new LinkedList<String>();
				String line = "";

				while (line != null) {
					line = reader.readLine();
					if (line != null && !line.equals("")) {
						lines.add(line);
					}
				}

				// Reverse lines so newest appears first
				StringBuilder builder = new StringBuilder();
				LinkedList<String> lineBuffer = new LinkedList<String>();
				while (lines.size() > 0) {
					line = lines.removeLast();

					// Log lines start with a '*' in log file but stack trace
					// lines from exceptions don't.
					// Can't just reverse the order of all lines, otherwise the
					// stack trace lines will
					// come out upside down. Queue any lines that don't start
					// with a '*' and append them
					// after the next line that does, which will be the log
					// message associated with the
					// exception.
					if (line.startsWith("*")) {
						line = line.substring(1);
						builder.append(line + "\n");

						while (lineBuffer.size() > 0) {
							builder.append(lineBuffer.removeLast() + "\n");
						}
					} else {
						lineBuffer.add(line);
					}
				}

				content = builder.toString();
				reader.close();
			}
		} catch (IOException e) {
			content = "Error reading log file: " + e.getMessage();
		}

		return content;
	}

	private String getLogFileName() {
		IPreferenceStore preferenceStore = IsisLog.getDefault()
				.getPreferenceStore();
		String directory = preferenceStore
				.getString(PreferenceConstants.P_LOG_DIR);
		String filename = preferenceStore
				.getString(PreferenceConstants.P_LOG_FILE);

		return directory + "/" + filename;
	}

}

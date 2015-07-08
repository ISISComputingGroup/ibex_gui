
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

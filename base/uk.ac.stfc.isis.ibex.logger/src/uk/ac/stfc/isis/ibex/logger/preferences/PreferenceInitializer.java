
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = IsisLog.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_LOG_DIR, PreferenceConstants.DEFAULT_LOG_DIR);
		store.setDefault(PreferenceConstants.P_LOG_FILE, PreferenceConstants.DEFAULT_LOG_FILE);
		store.setDefault(PreferenceConstants.P_MESSAGE_PATTERN, PreferenceConstants.DEFAULT_MESSAGE_PATTERN);
		store.setDefault(PreferenceConstants.P_ARCHIVE_PATTERN, PreferenceConstants.DEFAULT_ARCHIVE_PATTERN);
		store.setDefault(PreferenceConstants.P_MAX_FILE_SIZE, PreferenceConstants.DEFAULT_MAX_FILE_SIZE);
		store.setDefault(PreferenceConstants.P_MAX_ARCHIVE_PER_DAY, PreferenceConstants.DEFAULT_MAX_ARCHIVE_PER_DAY);
		store.setDefault(PreferenceConstants.P_LOGGING_LEVEL, PreferenceConstants.DEFAULT_LOGGING_LEVEL);
	}

}

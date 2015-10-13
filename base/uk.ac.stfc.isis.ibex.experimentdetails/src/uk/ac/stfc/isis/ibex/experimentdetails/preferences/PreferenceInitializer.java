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

package uk.ac.stfc.isis.ibex.experimentdetails.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ExperimentDetails.getInstance().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_EXP_DATA_SQL_USERNAME,
				PreferenceConstants.DEFAULT_EXP_DATA_SQL_USERNAME);
		store.setDefault(PreferenceConstants.P_EXP_DATA_SQL_PASSWORD,
			PreferenceConstants.DEFAULT_EXP_DATA_SQL_PASSWORD);
		store.setDefault(PreferenceConstants.P_EXP_DATA_SQL_SCHEMA,
			PreferenceConstants.DEFAULT_EXP_DATA_SQL_SCHEMA);
    }

}


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

package uk.ac.stfc.isis.ibex.experimentdetails.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Provides a model specific to the searching of the database
 *
 */
public class SearchModel extends ModelObject {
	private Collection<UserDetails> searchResults = new ArrayList<UserDetails>();
	private static final Logger LOG = IsisLog.getLogger(SearchModel.class);
	
	public void searchExperiments(String searchName, Role searchRole, GregorianCalendar date) {
		try {
			
			IPreferenceStore preferenceStore = ExperimentDetails.getInstance()
					.getPreferenceStore();
			
			String schema = preferenceStore
					.getString(PreferenceConstants.P_EXP_DATA_SQL_SCHEMA);
			String user = preferenceStore
				.getString(PreferenceConstants.P_EXP_DATA_SQL_USERNAME);
			String password = preferenceStore
				.getString(PreferenceConstants.P_EXP_DATA_SQL_PASSWORD);
			
			Rdb rdb = Rdb.connectToDatabase(schema, user, password);
			ExperimentIDQuery query = new ExperimentIDQuery(rdb);
			List<UserDetails> userDetails = query.getExperiments(searchName, searchRole, date);
			
			setSearchResults(userDetails);
			
		} catch (Exception ex) {
		    LOG.error("Error searching experiments: "
				    + ex.getMessage());
		}
	}
	
	private void setSearchResults(Collection<UserDetails> newResults) {
		firePropertyChange("searchResults", this.searchResults, this.searchResults = newResults);
	}
	
	public Collection<UserDetails> getSearchResults() {
		return searchResults;
	}
	
	public void clearResults() {
		firePropertyChange("searchResults", this.searchResults, this.searchResults = new ArrayList<UserDetails>());
	}
}

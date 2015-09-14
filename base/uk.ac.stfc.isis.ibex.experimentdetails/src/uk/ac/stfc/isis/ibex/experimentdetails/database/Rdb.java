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

import java.sql.Connection;
import java.sql.DriverManager;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class Rdb {
    private static final String PROTOCOL = "jdbc:mysql:";

    /** RDB connection */
    private final Connection connection;

    public static Rdb connectToDatabase() throws Exception {
    	return new Rdb();
    }

    public Rdb() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		IPreferenceStore preferenceStore = ExperimentDetails.getInstance().getPreferenceStore();
	
		String schema = preferenceStore
			.getString(PreferenceConstants.P_EXP_DATA_SQL_SCHEMA);
		String user = preferenceStore
			.getString(PreferenceConstants.P_EXP_DATA_SQL_USERNAME);
		String password = preferenceStore
			.getString(PreferenceConstants.P_EXP_DATA_SQL_PASSWORD);

		String address = PreferenceSupplier.SQLAddress();
		String port = PreferenceSupplier.SQLPort();
		
		if (address.indexOf("//") != 0) {
		    address = "//" + address;
		}
		
		String url = PROTOCOL + address + ":" + port + "/" + schema;
	
		connection = DriverManager.getConnection(url, user, password);
 
    }
    /** @return JDBC connection */
    public Connection getConnection() {
    	return connection;
    }

    /** Must be called when RDB no longer used to release resources */
    public void close() {
		try {
		    connection.close();
		} catch (Exception ex) {
			/*
		    LOG.error("Error closing connection to Database: "
			    + ex.getMessage());
			    */
		}
    }
}

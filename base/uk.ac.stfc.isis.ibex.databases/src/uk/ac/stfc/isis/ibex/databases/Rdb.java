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

package uk.ac.stfc.isis.ibex.databases;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.databases.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Rdb {
    private static final Logger LOG = IsisLog.getLogger(Rdb.class);

    private final String schema; 
    
    private static final String PROTOCOL = "jdbc:mysql:";

    /** RDB connection */
    private final Connection connection;

    public static Rdb connectToDatabase(String schema, String user, String password) throws Exception {
		return new Rdb(schema, user, password);
    }

    public Rdb(String schema, String user, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	
		this.schema = schema;
		
		String address = PreferenceSupplier.sqlAddress();
		String port = PreferenceSupplier.sqlPort();
	
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

    /**
     * Must be called when RDB no longer used to release resources.
     */
    public void close() {
		try {
		    connection.close();
		} catch (Exception ex) {
		    LOG.error("Error closing connection to database (" + schema + "): "
			    + ex.getMessage());
		}
    }
}

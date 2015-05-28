package uk.ac.stfc.isis.ibex.log.rdb;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Rdb {
	private static final Logger LOG = IsisLog.getLogger(Rdb.class);
	
	private static final String PROTOCOL = "jdbc:mysql:";
	
	/** RDB connection */
	private final Connection connection;
		
	public static Rdb connectToDatabase() throws Exception {
        return new Rdb();
	}
	
	public Rdb() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();
		
		String address = preferenceStore.getString(PreferenceConstants.P_SQL_ADDRESS);
		String port = preferenceStore.getString(PreferenceConstants.P_SQL_PORT);
		String schema = preferenceStore.getString(PreferenceConstants.P_SQL_SCHEMA);
		String user = preferenceStore.getString(PreferenceConstants.P_SQL_USERNAME);
		String password = preferenceStore.getString(PreferenceConstants.P_SQL_PASSWORD);
		
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
	        LOG.error("Error closing connection to Database: " + ex.getMessage());
        }
	}
}

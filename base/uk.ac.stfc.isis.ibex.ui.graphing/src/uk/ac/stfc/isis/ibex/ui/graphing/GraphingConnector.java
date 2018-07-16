package uk.ac.stfc.isis.ibex.ui.graphing;

import java.io.IOException;

import py4j.GatewayServer;
import py4j.Py4JNetworkException;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import org.apache.logging.log4j.Logger;

/**
 * Class to handle calls from python.
 */
public class GraphingConnector {
	
	private static Logger LOG = IsisLog.getLogger(GraphingConnector.class);
	
	/**
	 * Starts listening for incoming py4j calls.
	 */
    public static void startListening() throws IOException {
    	ConnectionHandler handler = new ConnectionHandler();
    	try {
	    	GatewayServer server = new GatewayServer(handler);
		    server.start();
    	} catch (Py4JNetworkException e) {
    		LOG.error("Could not start Py4J connection listener because: " + e.getMessage());
    	}
    	LOG.info("Started Py4J connection listener.");
    }
}

package uk.ac.stfc.isis.ibex.ui.graphing;

import org.apache.logging.log4j.Logger;

import py4j.GatewayServer;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class to handle calls from python.
 */
public final class GraphingConnector {

    private static final Logger LOG = IsisLog.getLogger(GraphingConnector.class);
    
    private static ConnectionHandler handler;
    private static GatewayServer server;

    private GraphingConnector() {
	    // Private constructor for utility class
    }
    
    /**
     * Starts listening for incoming py4j calls.
     */
    public static void startListening() {
	    handler = new ConnectionHandler();
	
		try {
		    server = new GatewayServer(handler, GatewayServer.DEFAULT_PORT);
		    server.start();
		} catch (RuntimeException e) {
		    LOG.error("Could not start Py4J connection listener because: " + e.getMessage());
		}
		LOG.info("Started Py4J connection listener.");
    }
    
    /**
     * Stops listening for incoming py4j calls.
     */
    public static void stopListening() {
    	if (server != null) {
    	    server.shutdown();
    	}
    }
}

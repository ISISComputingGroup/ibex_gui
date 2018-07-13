package uk.ac.stfc.isis.ibex.ui.graphing;

import py4j.GatewayServer;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class to handle calls from python.
 */
public class GraphingConnector {
	/**
	 * Starts listening for incoming py4j calls.
	 */
    public static void startListening() {
    	ConnectionHandler handler = new ConnectionHandler();
    	GatewayServer server = new GatewayServer(handler);
    	server.start();
    	IsisLog.getLogger(GraphingConnector.class).info("Started Py4J connection listener.");
    }
}

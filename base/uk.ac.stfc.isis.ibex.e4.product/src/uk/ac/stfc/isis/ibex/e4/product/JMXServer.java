package uk.ac.stfc.isis.ibex.e4.product;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.logging.log4j.Logger;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This starts a JMX server so we can remotely monitor the GUI.
 * We do it here rather than with the JMX command line arguments so that we can dynamically allocate ports
 */
public class JMXServer {
	private static final Logger LOG = IsisLog.getLogger(JMXServer.class);
	/**
	 * Starts the server.
	 */
	public static void startJMXServer() {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			JMXServiceURL url = new JMXServiceURL("rmi", null, 0);
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
			cs.start();
		    LOG.info("JMX url: " + cs.getAddress().toString());
		} catch (Exception e) {
			LOG.error("Failed to start JMX server: " + e.getMessage());
		}
	}
}

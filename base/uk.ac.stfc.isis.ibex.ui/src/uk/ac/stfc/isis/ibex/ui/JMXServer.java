package uk.ac.stfc.isis.ibex.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.logging.log4j.Logger;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This starts a JMX server so we can remotely monitor the GUI.
 * We do it here rather than with the JMX command line arguments so that we can dynamically allocate ports
 */
public final class JMXServer {
	private static final Logger LOG = IsisLog.getLogger(JMXServer.class);
	
	private static final String JMX_URL_PATH = Paths.get(System.getProperty("user.dir"), "jmx_connection.txt").toString();
	
	/**
	 * Private constructor for JMX Server utility class.
	 */
	private JMXServer() {
	    
	}
	
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
		    
		    // Also write JMX details to a separate file.
		    try (FileWriter writer = new FileWriter(JMX_URL_PATH)) {
				writer.write(cs.getAddress().toString());
			} catch (RuntimeException | IOException e) {
				LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
			}
		} catch (Exception e) {
			LOG.error("Failed to start JMX server: " + e.getMessage());
		}
	}
}

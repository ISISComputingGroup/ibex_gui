package uk.ac.stfc.isis.ibex.e4.product;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 * This starts a JMX server so we can remotely monitor the GUI.
 * We do it here rather than with the JMX command line arguments so that we can dynamically allocate ports
 */
public class JMXServer {
	/**
	 * Starts the server.
	 */
	public static void startJMXServer() {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			JMXServiceURL url = new JMXServiceURL("rmi", null, 0);
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
			cs.start();
		    System.out.println("JMX url: " + cs.getAddress().toString());
		} catch (Exception e) {
			System.out.println("Failed to start JMX server: " + e.getMessage());
		}
	}
}

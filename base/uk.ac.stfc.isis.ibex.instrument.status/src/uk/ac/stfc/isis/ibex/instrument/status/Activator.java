package uk.ac.stfc.isis.ibex.instrument.status;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator for the instrument status OSGI bundle.
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Gets the bundle context.
	 * @return the bundle context
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Start the plugin.
	 * @param bundleContext the eclipse bundle context
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/**
	 * Stop the plugin.
	 * @param bundleContext the eclipse bundle context
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}

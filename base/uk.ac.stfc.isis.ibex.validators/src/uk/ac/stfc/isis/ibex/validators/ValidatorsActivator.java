package uk.ac.stfc.isis.ibex.validators;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator for the validators bundle.
 */
public class ValidatorsActivator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Get the bundle context.
	 * @return the context
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Starts the bundle.
	 * @param bundleContext the context
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ValidatorsActivator.context = bundleContext;
	}

	/**
	 * Stops the bundle.
	 * @param bundleContext the context
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ValidatorsActivator.context = null;
	}

}

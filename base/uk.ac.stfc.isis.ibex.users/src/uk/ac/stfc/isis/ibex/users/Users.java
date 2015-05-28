package uk.ac.stfc.isis.ibex.users;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Users implements BundleActivator {

	private static BundleContext context;
	
	private static Users instance;
	private Director director = new Director();
	
	static BundleContext getContext() {
		return context;
	}

	public static Users getInstance() {
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Users.context = bundleContext;
		instance = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Users.context = null;
	}
	
	public Director director() {
		return director;
	}
}

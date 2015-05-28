package uk.ac.stfc.isis.ibex.opis;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Opi implements BundleActivator {

	private static BundleContext context;

	private static Opi instance;
	
	private static Provider provider = new Provider();
	
	public Opi() {
		instance = this;
	}
	
	public static Opi getDefault() {
		return instance;
	}
	
	public Provider provider() {
		return provider;
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Opi.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Opi.context = null;
	}

}

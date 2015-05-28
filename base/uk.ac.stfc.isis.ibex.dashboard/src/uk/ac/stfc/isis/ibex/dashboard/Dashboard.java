package uk.ac.stfc.isis.ibex.dashboard;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class Dashboard implements BundleActivator {

    private static Dashboard instance;
	private static BundleContext context;

    public static Dashboard getInstance() { 
    	return instance; 
    }
	
	private final DashboardObservables observables;
	
	public Dashboard() {
		instance = this;
		observables = new DashboardObservables(Instrument.getInstance().channels());
	}
 
	public DashboardObservables observables() {
		return observables;
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Dashboard.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Dashboard.context = null;
		observables.close();
	}

}

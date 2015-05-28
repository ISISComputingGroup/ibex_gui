package uk.ac.stfc.isis.ibex.beamstatus;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.beamstatus.internal.BeamStatusObservables;

public class BeamStatus extends Plugin {

    private static BeamStatus instance;
	private static BundleContext context;
	
	private final Observables observables;	
	private final BeamStatusObservables status;
	
	public BeamStatus() {
		instance = this; 
		status = new BeamStatusObservables();
		observables = new Observables(status);
	}
    
    public static BeamStatus getInstance() { 
    	return instance; 
    }

    public Observables observables() {
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
		BeamStatus.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		BeamStatus.context = null;
		status.close();
		observables.close();
	}
}



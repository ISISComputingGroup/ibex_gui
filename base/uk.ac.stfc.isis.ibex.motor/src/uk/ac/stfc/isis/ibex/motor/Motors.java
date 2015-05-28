package uk.ac.stfc.isis.ibex.motor;


import java.util.Collection;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/**
 * The activator class controls the plug-in life cycle
 */
public class Motors extends Plugin {
	private static Motors instance;
	private static BundleContext context;

    public static Motors getInstance() { 
    	return instance; 
    }
	
    private final MotorsTable motorsTable;
    
	public Motors() {
		super();
		instance = this;
		
		motorsTable = new MotorsTable(Instrument.getInstance());
	}
    
	public Collection<Motor> motors() {
		return motorsTable.motors();
	}
		
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Motors.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Motors.context = null;
		motorsTable.close();
	}

}

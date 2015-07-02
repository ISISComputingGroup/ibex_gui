package uk.ac.stfc.isis.ibex.motor;

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
	
    private final MotorsTable allMotorsTable;
    private final MotorsTable additionalMotorsTable;
    
	public Motors() {
		super();
		instance = this;
		
		allMotorsTable = new MotorsTable(Instrument.getInstance(), 8, 8, 1);
		additionalMotorsTable = new MotorsTable(Instrument.getInstance(), 6, 1, 9);
	}
    	
	public MotorsTable getMainMotorsTable() {
		return allMotorsTable;
	}
	
	public MotorsTable getAdditionalMotorsTable() {
		return additionalMotorsTable;
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
		allMotorsTable.close();
	}

}

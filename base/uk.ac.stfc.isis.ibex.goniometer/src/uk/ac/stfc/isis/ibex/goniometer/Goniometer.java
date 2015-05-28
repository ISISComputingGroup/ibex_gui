package uk.ac.stfc.isis.ibex.goniometer;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.goniometer.internal.ObservableGoniometerModel;
import uk.ac.stfc.isis.ibex.goniometer.internal.Variables;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class Goniometer extends Plugin {
	private static Goniometer instance;
	private static BundleContext context;

    public static Goniometer getInstance() { 
    	return instance; 
    }
	
    private final Variables variables;
    private final ObservableGoniometerModel model;
    
	public Goniometer() {
		super();
		instance = this;
		
		variables = new Variables(Instrument.getInstance().channels());
		model = new ObservableGoniometerModel(variables);
	}
    
	public GoniometerModel model() {
		return model;
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Goniometer.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Goniometer.context = null;
		variables.close();
	}
}

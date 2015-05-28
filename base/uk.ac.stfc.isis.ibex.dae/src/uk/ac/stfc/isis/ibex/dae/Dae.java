package uk.ac.stfc.isis.ibex.dae;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class Dae extends Plugin {

    private static Dae instance;
	private static BundleContext context;

    public static Dae getInstance() { 
    	return instance; 
    }

    private static final Instrument INSTRUMENT = Instrument.getInstance();

    private final DaeModel model;
	private final DaeWritables writables;
	private final DaeObservables observables;

    public Dae() {
		super();
		instance = this;
		writables = new DaeWritables(INSTRUMENT.channels());
		observables = new DaeObservables(INSTRUMENT.channels());
		model = new DaeModel(writables, observables);
	}
    
	public IDae model() {
		return model;
	}
	
	public DaeObservables observables() {
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
		Dae.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Dae.context = null;
		model.close();
		
		writables.close();
	}
}

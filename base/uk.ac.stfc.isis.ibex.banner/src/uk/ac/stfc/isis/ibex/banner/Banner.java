package uk.ac.stfc.isis.ibex.banner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class Banner implements BundleActivator {

	private static Banner instance;
	private static BundleContext context;
	
    public static Banner getInstance() { 
    	return instance; 
    }	
    
	private final Observables observables;
    
	public Banner() {
		super();
		instance = this;
		observables = new Observables(Instrument.getInstance().channels());
	}
	
	public Observables observables() {
		return observables;
	}	
	
	static BundleContext getContext() {
		return context;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Banner.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Banner.context = null;
	}    
}

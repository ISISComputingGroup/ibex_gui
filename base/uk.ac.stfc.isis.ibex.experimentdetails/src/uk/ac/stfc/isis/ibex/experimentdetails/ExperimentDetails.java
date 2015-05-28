package uk.ac.stfc.isis.ibex.experimentdetails;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.experimentdetails.internal.ExperimentDetailsVariables;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.ObservableModel;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class ExperimentDetails implements BundleActivator {

	private static BundleContext context;
	private static ExperimentDetails instance;
	
	private ExperimentDetailsVariables variables; 
	private Model model;

	static BundleContext getContext() {
		return context;
	}
	
	public ExperimentDetails() {
		instance = this;		
		variables = new ExperimentDetailsVariables(Instrument.getInstance().channels()); //prefixes the PV
		model = new ObservableModel(variables);
	}
	
	public static ExperimentDetails getInstance() {
		return instance;
	}
	
	public Model model() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ExperimentDetails.context = bundleContext;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ExperimentDetails.context = null;
		
		variables.close();
	}

}

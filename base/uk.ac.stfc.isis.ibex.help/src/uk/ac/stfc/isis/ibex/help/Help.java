package uk.ac.stfc.isis.ibex.help;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.help.internal.Observables;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class Help extends Closer implements BundleActivator {

	private static BundleContext context;
	private static Help instance;

	static BundleContext getContext() {
		return context;
	}

	private Observables observables;
	private UpdatedValue<String> revision;
	private UpdatedValue<String> date;
	
	public Help() {
		instance = this;
		observables = registerForClose(new Observables(Instrument.getInstance().channels()));
		revision = registerForClose(new TextUpdatedObservableAdapter(observables.revision));
		date = registerForClose(new TextUpdatedObservableAdapter(observables.date));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Help.context = bundleContext;
	}

	public static Help getInstance() {
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Help.context = null;
		close();
	}
	
	public UpdatedValue<String> revision() {
		return revision;
	}
	
	public UpdatedValue<String> date() {
		return date;
	}
}

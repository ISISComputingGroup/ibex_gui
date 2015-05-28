package uk.ac.stfc.isis.ibex.rotatingbench;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.rotatingbench.internal.Variables;

public class RotatingBench extends Closer implements BundleActivator {

	private static BundleContext context;
	private static RotatingBench instance;
	
	private final Variables bench;
	private RotatingBenchModel model;
	
	public RotatingBench() {
		instance = this;
		bench = registerForClose(new Variables(Instrument.getInstance().channels()));
		model = registerForClose(new RotatingBenchModel(bench));
	}
	
	public static RotatingBench getDefault() {
		return instance;
	}
	
	public IRotatingBench model() {
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
		RotatingBench.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		RotatingBench.context = null;
		close();
	}

}

package uk.ac.stfc.isis.ibex.instrument.baton;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Baton implements BundleActivator {
	private static BundleContext context;
    private static Baton instance;
    private final BatonObservables baton;


	static BundleContext getContext() {
		return context;
	}

    public Baton() {
        instance = this;
        baton = new BatonObservables();
    }

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
        Baton.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
        Baton.context = null;
	}

    public BatonObservables baton() {
        return baton;
    }

    public static Baton getInstance() {
        return instance;
    }

}

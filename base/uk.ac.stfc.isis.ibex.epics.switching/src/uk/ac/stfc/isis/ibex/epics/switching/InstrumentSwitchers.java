package uk.ac.stfc.isis.ibex.epics.switching;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class InstrumentSwitchers implements BundleActivator {

    private static InstrumentSwitchers instance;
	private static BundleContext context;

    private static NothingSwitcher nothingSwitcher = new NothingSwitcher();
    private static ClosingSwitcher closingSwitcher = new ClosingSwitcher();
    private static ObservablePrefixChangingSwitcher observablePrefixChangingSwitcher = new ObservablePrefixChangingSwitcher();
    private static WritablePrefixChangingSwitcher writingPrefixChangingSwitcher = new WritablePrefixChangingSwitcher();

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		InstrumentSwitchers.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		InstrumentSwitchers.context = null;
	}

    public static InstrumentSwitchers getDefault() {
        return instance;
    }

    public InstrumentSwitchers() {
        instance = this;
    }

    public static NothingSwitcher getNothingSwitcher() {
        return nothingSwitcher;
    }

    public static ClosingSwitcher getClosingSwitcher() {
        return closingSwitcher;
    }

    public static ObservablePrefixChangingSwitcher getObservablePrefixChangingSwitcher() {
        return observablePrefixChangingSwitcher;
    }

    public static WritablePrefixChangingSwitcher getWritingPrefixChangingSwitcher() {
        return writingPrefixChangingSwitcher;
    }
}

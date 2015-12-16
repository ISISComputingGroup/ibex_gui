package uk.ac.stfc.isis.ibex.epics.switching;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * This is a singleton class. It provides singleton instances of the switchers,
 * which have switchInstrument methods called by the SwitchInstrumentReciever.
 */
public class InstrumentSwitchers implements BundleActivator {

    private static InstrumentSwitchers instance;
	private static BundleContext context;

    private NothingSwitcher nothingSwitcher = new NothingSwitcher();
    private ClosingSwitcher closingSwitcher = new ClosingSwitcher();
    private ObservablePrefixChangingSwitcher observablePrefixChangingSwitcher = new ObservablePrefixChangingSwitcher();
    private WritablePrefixChangingSwitcher writablePrefixChangingSwitcher = new WritablePrefixChangingSwitcher();

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

    public NothingSwitcher getNothingSwitcher() {
        return instance.nothingSwitcher;
    }

    public Switcher getWritableSwitcher(OnInstrumentSwitch switchType) {
        Switcher switcher;
        switch (switchType) {
            case NOTHING:
                switcher = instance.nothingSwitcher;
                break;
            case SWITCH:
                switcher = instance.writablePrefixChangingSwitcher;
                break;
            case CLOSE:
                switcher = instance.closingSwitcher;
                break;
            default:
                switcher = null;
                break;
        }

        return switcher;

    }

    public Switcher getObservableSwitcher(OnInstrumentSwitch switchType) {
        Switcher switcher;
        switch (switchType) {
            case NOTHING:
                switcher = instance.nothingSwitcher;
                break;
            case SWITCH:
                switcher = instance.observablePrefixChangingSwitcher;
                break;
            case CLOSE:
                switcher = instance.closingSwitcher;
                break;
            default:
                switcher = null;
                break;
        }
        
        return switcher;

    }

    public void setInstrument(InstrumentInfo instrument) {
        instance.nothingSwitcher.switchInstrument(instrument);
        instance.closingSwitcher.switchInstrument(instrument);
        instance.observablePrefixChangingSwitcher.switchInstrument(instrument);
        instance.writablePrefixChangingSwitcher.switchInstrument(instrument);
    }
}

package uk.ac.stfc.isis.ibex.ui.instrument;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class InstrumentUI extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.instrument"; //$NON-NLS-1$

	// The shared instance
	private static InstrumentUI plugin;
	
	public static Instrument INSTRUMENT = Instrument.getInstance();
	
	public InstrumentUI() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static InstrumentUI getDefault() {
		return plugin;
	}

}

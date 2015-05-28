package uk.ac.stfc.isis.ibex.ui.dae;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.dae.Dae;


/**
 * The activator class controls the plug-in life cycle
 */
public class DaeUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.dae"; //$NON-NLS-1$

	// The shared instance
	private static DaeUI plugin;
	
	private DaeViewModel viewModel;
	
	/**
	 * The constructor
	 */
	public DaeUI() {
	}

	public DaeViewModel viewModel() {
		if (viewModel == null) {
			viewModel = new DaeViewModel();
			viewModel.bind(Dae.getInstance().model());
		}
		
		return viewModel;
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

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DaeUI getDefault() {
		return plugin;
	}

}

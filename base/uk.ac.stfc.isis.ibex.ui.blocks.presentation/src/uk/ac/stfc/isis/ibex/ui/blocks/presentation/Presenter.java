package uk.ac.stfc.isis.ibex.ui.blocks.presentation;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Presenter extends Plugin {

	public static final Logger LOG = IsisLog.getLogger("Blocks");
	
	private static BundleContext context;

    private static Presenter instance;
	private PVHistoryPresenter pvHistoryPresenter;

    public static Presenter getInstance() { 
    	return instance; 
    }
	
	static BundleContext getContext() {
		return context;
	}

	public Presenter() {
		instance = this;
	}
	
	public PVHistoryPresenter pvHistoryPresenter() {
		return pvHistoryPresenter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Presenter.context = bundleContext;
		
		pvHistoryPresenter = locatePVHistoryPresenter();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Presenter.context = null;
	}
	
	private PVHistoryPresenter locatePVHistoryPresenter() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.blocks.presentation");
		
		if (elements.length == 0) {
			return new DoNothingPVHistoryPresenter();
		}
		
		try {
			final Object obj = elements[0].createExecutableExtension("class");
			return (PVHistoryPresenter) obj;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return new DoNothingPVHistoryPresenter();
	}

}


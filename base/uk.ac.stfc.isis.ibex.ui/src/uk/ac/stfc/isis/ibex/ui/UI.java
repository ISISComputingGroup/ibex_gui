package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class UI extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui"; //$NON-NLS-1$

	// The shared instance
	private static UI plugin;

	@SuppressWarnings("unused")
	private WaitFor waiting;
	
	/**
	 * The constructor
	 */
	public UI() {
	}
	
	public void switchPerspective(final String perspectiveID) {
		Display.getDefault().syncExec(new Runnable() {
		    @Override
		    public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();	   
				IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
				PerspectiveSwitcher switcher = new PerspectiveSwitcher(workbench, workbenchWindow);
				
				switcher.switchTo(perspectiveID).run();		    
			}
		});
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
	public static UI getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public void earlyStartup() {
		waiting = new WaitFor();		
	}
}

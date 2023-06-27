package uk.ac.stfc.isis.ibex.ui.moxaperspective.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle, this plugin shows the
 * alarm perspective.
 */
public class Moxas extends AbstractUIPlugin {
    
    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.moxaperspective"; // $NON-NLS-1$

    /** The shared instance. */
	private static Moxas plugin;
    
    /**
     * Instantiates a new alarms; call getInstance instead in most cases.
     */
    public Moxas() {
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static Moxas getDefault() {
		return plugin;
	}

	/**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
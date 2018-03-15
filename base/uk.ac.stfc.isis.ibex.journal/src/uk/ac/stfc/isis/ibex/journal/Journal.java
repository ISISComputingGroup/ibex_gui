package uk.ac.stfc.isis.ibex.journal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The singleton class for the journal back end.
 */
public class Journal extends AbstractUIPlugin {

    private static Journal instance;
    private static JournalModel model;
    private static BundleContext context;

    /**
     * @return The instance of this singleton.
     */
    public static Journal getDefault() {
        return instance;
    }

    /**
     * @return The journal model.
     */
    public JournalModel getModel() {
        return model;
    }
    
    /**
	 * Gets the bundle context associated with this plugin.
	 * 
	 * @return the bundle context
	 */
	static BundleContext getContext() {
		return context;
	}

    /**
     * The constructor for the activator. Creates a new model and counter.
     */
    public Journal() {
        super();
        instance = this;
        model = new JournalModel(getPreferenceStore());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
    	context = bundleContext;
        super.start(context);
        model.refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(context);
        Journal.context = null;
    }

}

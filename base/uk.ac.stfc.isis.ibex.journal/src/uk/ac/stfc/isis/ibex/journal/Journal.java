package uk.ac.stfc.isis.ibex.journal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Journal extends AbstractUIPlugin {

    private static Journal instance;
    private static JournalModel model;

    /**
     * @return The instance of this singleton.
     */
    public static Journal getInstance() {
        return instance;
    }

    /**
     * @return The instance of this singleton.
     */
    public static Journal getDefault() {
        return instance;
    }

    public JournalModel getModel() {
        return model;
    }

    /**
     * The constructor for the activator. Creates a new model and counter.
     */
    public Journal() {
        super();
        instance = this;
        model = new JournalModel(getPreferenceStore());
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(bundleContext);
    }

}

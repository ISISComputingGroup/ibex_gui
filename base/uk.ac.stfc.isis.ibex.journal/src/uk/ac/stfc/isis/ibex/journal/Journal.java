package uk.ac.stfc.isis.ibex.journal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Journal extends AbstractUIPlugin {

    private static Journal instance;
    private static JournalModel model;
//    private static BundleContext context;

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

//    private final JournalModel model;

    /**
     * The constructor for the activator. Creates a new model and counter.
     */
    public Journal() {
        super();
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
        instance = this;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        instance = null;
        super.stop(bundleContext);
//        model.stop();
    }

    public static void saySomething() {
        System.out.println("something");
    }

}

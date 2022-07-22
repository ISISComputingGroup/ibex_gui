package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.Collections;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.graphing.GraphingConnector;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static final SettableUpdatedValue<List<Integer>> PRIMARY_FIGURES = new SettableUpdatedValue<>();
	private static final SettableUpdatedValue<List<Integer>> SECONDARY_FIGURES = new SettableUpdatedValue<List<Integer>>();
	
    private static final SettableUpdatedValue<String> PRIMARY_PLOT_URL = new SettableUpdatedValue<String>();
    private static final SettableUpdatedValue<String> SECONDARY_PLOT_URL = new SettableUpdatedValue<String>();
	
	static {
		PRIMARY_FIGURES.setValue(Collections.emptyList());
		SECONDARY_FIGURES.setValue(Collections.emptyList());
	}

	public static BundleContext getContext() {
		return context;
	}
	
	public static final String BUNDLE_NAME = "matplotlibrcp";

	public void start(BundleContext bundleContext) {
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext){
		Activator.context = null;
		GraphingConnector.stopListening();
	}
	
	public static void setPrimaryFigures(List<Integer> figures) {
		PRIMARY_FIGURES.setValue(figures);
	}
	
	public static void setSecondaryFigures(List<Integer> figures) {
		SECONDARY_FIGURES.setValue(figures);
	}
	
	public static UpdatedValue<List<Integer>> getPrimaryFigures() {
		return PRIMARY_FIGURES;
	}
	
	public static UpdatedValue<List<Integer>> getSecondaryFigures() {
		return SECONDARY_FIGURES;
	}
	
    /**
     * Sets the url for the primary plot.
     * @param url the url
     */
    public static void setPrimaryUrl(String url) {
    	PRIMARY_PLOT_URL.setValue(url);
    }
    
    /**
     * Sets the url for the primary plot.
     * @param url the url
     */
    public static void setSecondaryUrl(String url) {
    	SECONDARY_PLOT_URL.setValue(url);
    }
    
    /**
     * Gets the url for the primary plot.
     * @return the url
     */
    public static UpdatedValue<String> getPrimaryUrl() {
    	return PRIMARY_PLOT_URL;
    }
    
    /**
     * Gets the url for the secondary plot.
     * @return the url
     */
    public static UpdatedValue<String> getSecondaryUrl() {
    	return SECONDARY_PLOT_URL;
    }
}
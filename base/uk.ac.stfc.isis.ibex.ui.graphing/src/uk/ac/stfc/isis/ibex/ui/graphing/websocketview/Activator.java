package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.graphing.GraphingConnector;

/**
 * Bundle activator.
 */
@SuppressWarnings("restriction")
public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static final SettableUpdatedValue<List<Integer>> PRIMARY_FIGURES = new SettableUpdatedValue<>();
	private static final SettableUpdatedValue<List<Integer>> SECONDARY_FIGURES = new SettableUpdatedValue<>();
	
    private static final SettableUpdatedValue<String> PRIMARY_PLOT_URL = new SettableUpdatedValue<String>();
    private static final SettableUpdatedValue<String> SECONDARY_PLOT_URL = new SettableUpdatedValue<String>();
    
    private static final Set<String> PRIMARY_FIGURE_IDS = Set.of(
    	"uk.ac.stfc.isis.ibex.e4.client.part.reflectometry.matplotlib",
    	"uk.ac.stfc.isis.ibex.e4.client.part.scripting.matplotlib"
    );
    
    private static final Set<String> SECONDARY_FIGURE_IDS = Set.of(
    	"uk.ac.stfc.isis.ibex.e4.client.part.scripting.matplotlib_secondary"
    );
	
	static {
		PRIMARY_FIGURES.setValue(Collections.emptyList());
		SECONDARY_FIGURES.setValue(Collections.emptyList());
	}

	/**
	 * Gets the bundle context.
	 * @return the bundle context
	 */
	public static BundleContext getContext() {
		return context;
	}
	
	/**
	 * The bundle name.
	 */
	public static final String BUNDLE_NAME = "matplotlibrcp";

	/**
	 * Start the bundle.
	 * @param bundleContext the bundle context
	 */
	public void start(BundleContext bundleContext) {
		Activator.context = bundleContext;
	}

	/**
	 * Stop the bundle.
	 * @param bundleContext the bundle context
	 */
	public void stop(BundleContext bundleContext) {
		Activator.context = null;
		GraphingConnector.stopListening();
	}
	
	/**
	 * Sets the primary figures.
	 * @param figures the figure numbers
	 */
	public static void setPrimaryFigures(List<Integer> figures) {
		PRIMARY_FIGURES.setValue(figures);
	}
	
	/**
	 * Sets the secondary figures.
	 * @param figures the figure numbers
	 */
	public static void setSecondaryFigures(List<Integer> figures) {
		SECONDARY_FIGURES.setValue(figures);
	}
	
	/**
	 * Gets the primary figures.
	 * @return the figure numbers
	 */
	public static UpdatedValue<List<Integer>> getPrimaryFigures() {
		return PRIMARY_FIGURES;
	}
	
	/**
	 * Gets the secondary figures.
	 * @return the figure numbers
	 */
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
    
    /**
     * Unhides any matplotlib parts.
     * @param isPrimary whether we are unhiding primary or secondary plots.
     */
    public static void unhidePlots(boolean isPrimary) {
    	Display.getDefault().asyncExec(() -> {
	    	var application = E4Workbench.getServiceContext().get(MApplication.class);
	    	for (var window : application.getChildren()) {
	    		var partService = window.getContext().get(EPartService.class);
	    		for (String id : (isPrimary ? PRIMARY_FIGURE_IDS : SECONDARY_FIGURE_IDS)) {
	    		    partService.showPart(id, PartState.CREATE);
	    		}
	    	}
    	});
    }
}
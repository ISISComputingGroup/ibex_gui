package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.Collections;
import java.util.List;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static final SettableUpdatedValue<List<Integer>> PRIMARY_FIGURES = new SettableUpdatedValue<>();
	private static final SettableUpdatedValue<List<Integer>> SECONDARY_FIGURES = new SettableUpdatedValue<List<Integer>>();
	
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
}
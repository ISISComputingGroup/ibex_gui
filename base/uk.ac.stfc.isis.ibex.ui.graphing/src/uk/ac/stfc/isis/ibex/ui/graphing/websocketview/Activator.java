package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}
	
	private static final Map<Integer, MatplotlibWebsocketModel> MODELS = new HashMap<>();
	
	public static final String BUNDLE_NAME = "matplotlibrcp";

	public void start(BundleContext bundleContext) {
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext){
		Activator.context = null;
	}
	
	public static synchronized MatplotlibWebsocketModel getModel(int figureNumber) {
		return MODELS.computeIfAbsent(figureNumber, k -> new MatplotlibWebsocketModel("127.0.0.1", 8988, k));
	}
}
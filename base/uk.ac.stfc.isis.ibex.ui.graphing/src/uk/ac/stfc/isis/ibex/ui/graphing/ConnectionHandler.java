package uk.ac.stfc.isis.ibex.ui.graphing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.Activator;

/**
 * Class to handle function calls from python regarding opening the plot window.
 * 
 * THE METHODS IN THIS CLASS ARE CALLED FROM PYTHON SO MAY NOT HAVE ANY LOCAL REFERENCES IN JAVA CODE.
 */
public class ConnectionHandler {

    /**
     * The ID of the reflectometry and scripting perspectives
     */
    private static final String REFL_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.reflectometry";
    private static final String SCRIPTING_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective";
    
    private Set<String> perspectiveIdsToRefresh = new HashSet<String>();
    
    private String url;
    private boolean isPrimary;
    
    private IPerspectiveListener openOnSwitchingPerspective = new PerspectiveAdapter() {   
        @Override
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
            if (!perspectiveIdsToRefresh.isEmpty()) {
                Display.getDefault().asyncExec(() -> openPlotInCurrentPerspective(perspective, url, isPrimary));
            }
        }
    };
    
    /**
     * Tries to open the plot in current perspective, if it is the correct one to open it in.
     * @param currentPerspective The current perspective
     * @param url The URL of the plot
     * @param isPrimary Whether the plot is the primary plot
     */
	private void openPlotInCurrentPerspective(IPerspectiveDescriptor currentPerspective, final String url,
			final boolean isPrimary) {
		if (perspectiveIdsToRefresh.removeIf(currentPerspective.getId()::equals)) {
			MatplotlibOpiTargetView.displayOpi(url, isPrimary);
		}
    }
    
    /**
     * Opens the plotting OPI pointing at the given URL.
     * @param url the URL to point at
     * @param isPrimary True if this is the primary plot window; False for secondary
     */
    public void openPlot(final String url, final boolean isPrimary) {
        this.url = url;
        this.isPrimary = isPrimary;
    	IsisLog.getLogger(ConnectionHandler.class).info("Opening matplotlib OPI. Primary display " + isPrimary);
    	Display.getDefault().asyncExec(() -> {
	        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(openOnSwitchingPerspective);
    		IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();    		
    		perspectiveIdsToRefresh.add(REFL_PERSPECTIVE_ID);
    		perspectiveIdsToRefresh.add(SCRIPTING_PERSPECTIVE_ID);
    		openPlotInCurrentPerspective(currentPerspective, url, isPrimary);
	    });
    }
    
    public void openMplRenderer(final List<Integer> figures, final String url, final boolean isPrimary) {
    	if (isPrimary) {
    		Activator.setPrimaryUrl(url);
    	    Activator.setPrimaryFigures(figures);
    	} else {
    		Activator.setSecondaryUrl(url);
    		Activator.setSecondaryFigures(figures);
    	}
    }
    
    /**
     * Removes the perspective listener.
     */
    public void removePerspectiveListener() {
    	var workbench = PlatformUI.getWorkbench();
    	if (workbench != null) {
    		var activeWindow = workbench.getActiveWorkbenchWindow();
    		if (activeWindow != null) {
    			activeWindow.removePerspectiveListener(openOnSwitchingPerspective);
    		}
    	}
    }
}

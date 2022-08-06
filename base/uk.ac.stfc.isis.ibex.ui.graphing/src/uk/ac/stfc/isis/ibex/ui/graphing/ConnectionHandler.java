package uk.ac.stfc.isis.ibex.ui.graphing;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.UIThreadUtils;

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
    
    private ArrayList<String> perspectivesToRefresh = new ArrayList<String>();
    
    private String url;
    private boolean isPrimary;
    
    private IPerspectiveListener openOnSwitchingPerspective = new IPerspectiveListener() {   
        @Override
        public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
        }
        
        @Override
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
            if (!perspectivesToRefresh.isEmpty()) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        openPlotInCurrentPerspective(perspective, url, isPrimary);
                    }
                });
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
		if (perspectivesToRefresh.contains(currentPerspective.getId())) {
			MatplotlibOpiTargetView.displayOpi(url, isPrimary);
			perspectivesToRefresh.remove(SCRIPTING_PERSPECTIVE_ID);
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
    	UIThreadUtils.asyncExec(new Runnable() {
    	    @Override
    	    public void run() {
    	        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(openOnSwitchingPerspective);
        		IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();    		
        		perspectivesToRefresh.add(REFL_PERSPECTIVE_ID);
        		perspectivesToRefresh.add(SCRIPTING_PERSPECTIVE_ID);
        		openPlotInCurrentPerspective(currentPerspective, url, isPrimary);
    	    }
    	});
    }
    
    @Override
    protected void finalize() {
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().removePerspectiveListener(openOnSwitchingPerspective);
    }
}

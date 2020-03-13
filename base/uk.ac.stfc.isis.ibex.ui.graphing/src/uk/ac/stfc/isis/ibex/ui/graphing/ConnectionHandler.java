package uk.ac.stfc.isis.ibex.ui.graphing;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Class to handle function calls from python regarding opening the plot window.
 * 
 * THE METHODS IN THIS CLASS ARE CALLED FROM PYTHON SO MAY NOT HAVE ANY LOCAL REFERENCES IN JAVA CODE.
 */
public class ConnectionHandler {

	/**
	 * The ID of the reflectometry perspective
	 */
	private static final String REFL_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.reflectometry";
	
	/**
	 * Opens the plotting OPI pointing at the given URL.
	 * @param url the URL to point at
	 */
    public void openPlot(final String url) {
    	IsisLog.getLogger(ConnectionHandler.class).info("Opening matplotlib OPI.");
    	Display.getDefault().asyncExec(new Runnable() {
    		@Override
    		public void run() {
    	    	IPerspectiveDescriptor currentPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
    			boolean isReflectometryView = currentPerspective.getId().equals(REFL_PERSPECTIVE_ID);
    			
    			if (isReflectometryView) {
    				FixedMatplotlibOpiTargetView.displayOpi(url);
    			} else {
    				MatplotlibOpiTargetView.displayOpi(url);
    			}
    		}
    	});
    }
}

package uk.ac.stfc.isis.ibex.ui.graphing;

import java.util.List;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.Activator;

/**
 * Class to handle function calls from python regarding opening the plot window.
 * 
 * THE METHODS IN THIS CLASS ARE CALLED FROM PYTHON SO MAY NOT HAVE ANY LOCAL REFERENCES IN JAVA CODE.
 */
public class ConnectionHandler {
    
    private static final IPerspectiveListener OPEN_MPL_RENDERER_LISTENER = new PerspectiveAdapter() {
    	@Override
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
	        if (!Activator.getPrimaryFigures().getValue().isEmpty()) {
	        	Activator.unhidePlots(true);
	        }
	        if (!Activator.getSecondaryFigures().getValue().isEmpty()) {
	        	Activator.unhidePlots(false);
	        }
    	}
    };
    
    /**
     * Opens the matplotlib renderer.
     * @param figures the figures which are currently open.
     * @param url the websocket URL to look at
     * @param isPrimary whether this is the primary plot
     */
    public void openMplRenderer(final List<Integer> figures, final String url, final boolean isPrimary) {
    	if (isPrimary) {
    		Activator.setPrimaryUrl(url);
    	    Activator.setPrimaryFigures(figures);
    	} else {
    		Activator.setSecondaryUrl(url);
    		Activator.setSecondaryFigures(figures);
    	}
    	Activator.unhidePlots(isPrimary);
    	Display.getDefault().asyncExec(() -> {
    	    PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(OPEN_MPL_RENDERER_LISTENER);
    	});
    }
}

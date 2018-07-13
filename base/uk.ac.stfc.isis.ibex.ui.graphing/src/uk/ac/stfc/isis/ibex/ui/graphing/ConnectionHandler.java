package uk.ac.stfc.isis.ibex.ui.graphing;

import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;

/**
 * Class to handle function calls from python regarding opening the plot window.
 * 
 * THE METHODS IN THIS CLASS ARE CALLED FROM PYTHON SO MAY NOT HAVE ANY LOCAL REFERENCES IN JAVA CODE.
 */
public class ConnectionHandler {
	
	/**
	 * Opens the plotting OPI pointing at the given URL.
	 * @param url the URL to point at
	 */
    public void openPlot(String url) {
    	IsisLog.getLogger(ConnectionHandler.class).info("Opening matplotlib OPI with url: " + url);
    	Display.getDefault().asyncExec(new Runnable() {
    		@Override
    		public void run() {
    			try {
					MatplotlibOpiTargetView.displayPlotopiWithUrl(url);
				} catch (OPIViewCreationException e) {
					IsisLog.getLogger(ConnectionHandler.class).error(e.getMessage(), e);
				}
    		}
    	});
    }
}

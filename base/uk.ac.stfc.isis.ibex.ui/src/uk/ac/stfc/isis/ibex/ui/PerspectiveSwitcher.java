package uk.ac.stfc.isis.ibex.ui;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;

import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class PerspectiveSwitcher {

	private static final Logger LOG = IsisLog.getLogger(PerspectiveSwitcher.class);
	
	private final IWorkbench workbench;	   
	private final IWorkbenchWindow workbenchWindow;

	private final Display display = Display.getDefault();

	private static LogCounter counter = Log.getInstance().getCounter();	
	
	private static final Runnable DO_NOTHING = new Runnable() {	
		@Override
		public void run() {	
			// Do nothing
		}
	};
	
	public static final String LOG_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.ui.log.perspective";
	
	public PerspectiveSwitcher(IWorkbench workbench, IWorkbenchWindow workbenchWindow) {
		this.workbench = workbench;
		this.workbenchWindow = workbenchWindow;
	}
		
	public Runnable switchTo(final String perspectiveID) {
		return isCurrentPerspective(perspectiveID) 
					? DO_NOTHING : makeTheSwitch(perspectiveID);
    }
	
	private Runnable makeTheSwitch(final String perspectiveID) {
		return runInDisplayThread(new Runnable() {
    		public void run() {
    			try {
    				LOG.info("Switching to: " + perspectiveID);
    				
    				//Restart counter if switching to or from IOC log
    				if (isLogPerspective(perspectiveID) || isCurrentPerspective(LOG_PERSPECTIVE_ID)) {
						counter.start();
					}
    					
                	workbench.showPerspective(perspectiveID, workbenchWindow);
                } catch (WorkbenchException e) {
                	LOG.warn("Workbench Error: " + e.getMessage());
                }
            }
        }); 		
	}	
	
	private Runnable runInDisplayThread(final Runnable runnable) {
		return new Runnable() {		
			@Override
			public void run() {
				display.syncExec(runnable);
			}
		};
	}
	
	private boolean isLogPerspective(String perspectiveID) {
		return perspectiveID.equals(LOG_PERSPECTIVE_ID);
	}
	
	private boolean isCurrentPerspective(String perspectiveID) {
		IPerspectiveDescriptor activePerspective = workbenchWindow.getActivePage().getPerspective();		
	    return activePerspective != null && activePerspective.getId().equals(perspectiveID); 
	}
}

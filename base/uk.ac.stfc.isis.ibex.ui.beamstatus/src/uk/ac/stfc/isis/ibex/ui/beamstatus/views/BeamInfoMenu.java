package uk.ac.stfc.isis.ibex.ui.beamstatus.views;



import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.action.Action;


import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.NewBlockHandler;


/*
 * Right-click menu from Beam Information
 * 
 */
public class BeamInfoMenu extends MenuManager  {
	
	FacilityPV facilityPV;
	private static final String LOG_PLOTTER_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";

	
	 /**
     * The constructor, creates the menu for when the specific facility PV is right-clicked on.
     *
     * @param facilityPV the selected PV
     */
    public BeamInfoMenu(FacilityPV facilityPV) {
    	
    	// Creating right-click menu
    	 
        add(new Action("Add to config: "+facilityPV.pv) { //Opening configuration dialog window
            @Override
            public void run() {
               
            	try {
            		
					new NewBlockHandler().createDialog(facilityPV.pv);
					
				} catch (TimeoutException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
                super.run();
            }
        });
        
        add(new Action("Log Plotter "+ facilityPV.pv) { //Opening log plotter window
        	public void run() {
        	    		
        			switchToLogPlotter();
        			Presenter.pvHistoryPresenter().newDisplay(facilityPV.pv, facilityPV.pv);
      
        	}
        });
    }
    
    /**
     * Switching perspective to log plotter
     */
    private static void switchToLogPlotter() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
        final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        page.setPerspective(registry.findPerspectiveWithId(LOG_PLOTTER_PERSPECTIVE_ID));
    }
    
   
}

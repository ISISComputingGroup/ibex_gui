package uk.ac.stfc.isis.ibex.ui.beamstatus.views;



import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.action.Action;


import org.eclipse.jface.action.MenuManager;


import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;

import uk.ac.stfc.isis.ibex.ui.configserver.commands.NewBlockHandler;

/*
 * Right-click menu from Beam Information
 * 
 */
public class BeamInfoMenu extends MenuManager  {
	
	FacilityPV facilityPV;
	
	 /**
     * The constructor, creates the menu for when the specific facility PV is right-clicked on.
     *
     * @param facilityPV the selected PV
     */
    public BeamInfoMenu(FacilityPV facilityPV) {
    	
    	 
        add(new Action("Add/log: "+facilityPV.pv) {
            @Override
            public void run() {
               
            	try {
            		
					new NewBlockHandler().createDialog(facilityPV.pv); // Creating right-click menu
					
				} catch (TimeoutException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
                super.run();
            }
        });
    }
    
   
}

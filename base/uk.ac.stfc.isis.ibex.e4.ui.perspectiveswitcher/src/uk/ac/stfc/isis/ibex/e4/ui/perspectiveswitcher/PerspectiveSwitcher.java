package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;

/**
 * Helper class for switching perspectives.
 */
public class PerspectiveSwitcher {

	private PerspectivesProvider provider;
	
	/**
	 * The Constructor. 
	 * 
	 * @param provider Provider for perspective objects based on a given element ID.
	 */
	public PerspectiveSwitcher(PerspectivesProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * Switches to a given perspective based on its element ID.
	 * 
	 * @param id The ID of the desired perspective.
	 */
	public void switchPerspective(String id) {
		MPerspective element = provider.getPerspective(id);
	    if (element != null) {
	      	provider.getPartService().switchPerspective(element);
	    } else {
			System.out.println("Unable to find perspective part with ID: " + id);
		}
	}
	
}

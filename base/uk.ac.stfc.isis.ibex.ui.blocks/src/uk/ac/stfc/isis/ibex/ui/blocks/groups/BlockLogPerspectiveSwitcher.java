package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Helper class for switching to the log plotter perspective.
 */
public class BlockLogPerspectiveSwitcher {

	private static final String LOGPLOTTER_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
	private PerspectivesProvider provider;
	
	/**
	 * The Constructor. 
	 * 
	 * @param app The E4 application model
	 * @param partService The E4 service responsible for showing/hiding parts
	 * @param modelService The E4 service responsible for handling model elements
	 */
	public BlockLogPerspectiveSwitcher(MApplication app, EPartService partService, EModelService modelService) {
		provider = new PerspectivesProvider(app, partService, modelService);
	}
	
	/**
	 * Switches to the log plotter perspective.
	 */
	public void switchPerspective() {
		MPerspective element = provider.getPerspective(LOGPLOTTER_ID);
	    if (element != null) {
	      	provider.getPartService().switchPerspective(element);
	    } else {
			System.out.println("Unable to find perspective part with ID: " + LOGPLOTTER_ID);
		}
	}
	
}

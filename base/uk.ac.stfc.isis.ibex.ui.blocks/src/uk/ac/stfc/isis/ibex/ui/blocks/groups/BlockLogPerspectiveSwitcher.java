package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class BlockLogPerspectiveSwitcher {

	private static final String LOGPLOTTER_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
	private PerspectivesProvider provider;
	
	public BlockLogPerspectiveSwitcher(MApplication app, EPartService partService, EModelService modelService) {
		provider = new PerspectivesProvider(app, partService, modelService);
	}
	
	public void switchPerspective() {
		MPerspective element = provider.getPerspective(LOGPLOTTER_ID);
	    if (element != null) {
	      	provider.getPartService().switchPerspective(element);
	    } else {
			System.out.println("Unable to find perspective part with ID: " + LOGPLOTTER_ID);
		}
	}
	
}

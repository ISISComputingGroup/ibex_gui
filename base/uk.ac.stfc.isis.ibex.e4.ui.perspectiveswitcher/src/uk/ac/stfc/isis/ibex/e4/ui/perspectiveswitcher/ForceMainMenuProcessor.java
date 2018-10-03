package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import uk.ac.stfc.isis.ibex.logger.IsisLog;


/**
 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=388808
 */
public class ForceMainMenuProcessor {
	
	private static final Logger LOG = IsisLog.getLogger(ForceMainMenuProcessor.class);
	private static final String MAIN_WINDOW_ID = "uk.ac.stfc.isis.ibex.client.e4.product.trimmedwindow.0";

	@Inject
	public EModelService modelService;
	
	@Inject
	public MApplication app;
	
	@Execute
	public void run() {
		LOG.info("Entering force main menu processor");

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(MAIN_WINDOW_ID, app);
		
		if (window == null) {
			LOG.info("No window - cannot run processor");
			return;
		}
		
	    if (window.getMainMenu() == null) {
	    	LOG.info("Main menu was null, replacing it with an empty main menu.");
	        window.setMainMenu(createEmptyMainMenu());
	        
	        if (window.getMainMenu() == null) {
	        	LOG.error("Main menu was still null after replacement.");
	        }
	    }
	}
	
	private MMenu createEmptyMainMenu() {
	    final MMenu mainMenu = modelService.createModelElement(MMenu.class);
	    mainMenu.setElementId("org.eclipse.ui.main.menu");
	    return mainMenu;
	  }
}

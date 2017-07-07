package uk.ac.stfc.isis.ibex.e4.perspectiveswitcher;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectiveSwitcher {

	public final void openPerspective(IEclipseContext context, String perspectiveID) {
		MApplication application = context.get(MApplication.class);
		EPartService partService = context.get(EPartService.class);
		EModelService modelService = context.get(EModelService.class);
		
		List<MPerspective> perspectives = modelService.findElements(application, perspectiveID, MPerspective.class, null);
		partService.switchPerspective(perspectives.get(0));
	}

}

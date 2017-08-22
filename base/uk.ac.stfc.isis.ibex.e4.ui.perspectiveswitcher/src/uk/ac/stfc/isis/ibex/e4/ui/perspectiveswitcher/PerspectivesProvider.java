package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectivesProvider {
	
	private final EPartService partService;
	private final MApplication app;
	private final EModelService modelService;
	private List<MPerspective> perspectives = new ArrayList<MPerspective>();
	
	public PerspectivesProvider(MApplication app, EPartService partService, EModelService modelService) {
		this.partService = partService;
		this.app = app;
		this.modelService = modelService;
		this.perspectives =  modelService.findElements(app, null, MPerspective.class, null);
	}
	
	public EPartService getPartService() {
		return partService;
	}

	public List<MPerspective> getPerspectives() {
        return perspectives;
	}
	
	private boolean matchPerspectivesById(MPerspective p, String id) {
	    // The 2nd condition is because E4 has a bug that creates orphan perspectives with IDs of the form ...perspective.alarms.<Alarms>
		return p.getElementId().equals(id) || p.getElementId().equals(id + "." + p.getLabel());
	}
	
	public MPerspective getPerspective(String elementId) {
		return perspectives.stream().filter(p-> matchPerspectivesById(p, elementId)).findAny().get();
	}
}

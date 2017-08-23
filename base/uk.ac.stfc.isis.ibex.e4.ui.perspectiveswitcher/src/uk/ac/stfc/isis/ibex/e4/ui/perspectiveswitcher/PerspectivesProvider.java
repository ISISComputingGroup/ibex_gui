package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectivesProvider {
	
	private final EPartService partService;
	private List<MPerspective> perspectives = new ArrayList<MPerspective>();
	private MPerspectiveStack perspectivesStack;
	
	public PerspectivesProvider(MApplication app, EPartService partService, EModelService modelService) {
		this.partService = partService;
		this.perspectives =  modelService
				.findElements(app, null, MPerspective.class, null)
				.stream()
				.filter(p -> p.isVisible())
				.collect(Collectors.toList());
		this.perspectivesStack = modelService.findElements(app, null, MPerspectiveStack.class, null).get(0);
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
	
	public boolean isSelected(MPerspective perspective) {
		return perspectivesStack.getSelectedElement().equals(perspective);		
	}
}

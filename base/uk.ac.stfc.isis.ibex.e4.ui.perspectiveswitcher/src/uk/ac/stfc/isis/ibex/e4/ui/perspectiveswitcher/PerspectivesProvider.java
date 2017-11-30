package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectivesProvider {
	
	private static PerspectivesProvider instance;
	
	private final EPartService partService;
	private List<MPerspective> perspectives = new ArrayList<MPerspective>();
	private MPerspectiveStack perspectivesStack;
	private MApplication app;
	private EModelService modelService;
	
    private static final String MAIN_PERSPECTIVE_STACK_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspectivestack.0";
	
    public static PerspectivesProvider getInstance(){
    	return instance;
    }
    
	public PerspectivesProvider(MApplication app, EPartService partService, EModelService modelService) {
		this.partService = partService;
		this.app = app;
		this.modelService = modelService;
		this.perspectives =  modelService
				.findElements(app, null, MPerspective.class, null)
				.stream()
				.filter(p -> p.isVisible())
				.collect(Collectors.toList());
		this.perspectivesStack = modelService.findElements(app, null, MPerspectiveStack.class, null).get(0);
		instance = this;
	}
	
	public EPartService getPartService() {
		return partService;
	}
	
	public EModelService getModelService() {
		return modelService;
	}
	
	public MApplication getApp() {
		return app;
	}

	public List<MPerspective> getPerspectives() {
        return perspectives;
	}
	
	private boolean matchPerspectivesById(MPerspective p, String id) {
	    // The 2nd condition is because E4 has a bug that creates orphan perspectives with IDs of the form ...perspective.alarms.<Alarms>
		return p.getElementId().equals(id) || p.getElementId().equals(id + "." + p.getLabel());
	}
	
	public MPerspective getPerspective(String elementId) {
		return perspectives.stream().filter(p -> matchPerspectivesById(p, elementId)).findAny().get();
	}
	
	public boolean isSelected(MPerspective perspective) {
		return perspectivesStack.getSelectedElement().equals(perspective);		
	}

	public MPerspectiveStack getTopLevelStack() {
		return (MPerspectiveStack) modelService.find(MAIN_PERSPECTIVE_STACK_ID, app);
	}
	
	public List<MPerspective> getInitialPerspectives() {
		List<MPerspective> perspectives = new ArrayList<MPerspective>();
        for (MUIElement snippet : app.getSnippets()) {
            if (snippet instanceof MPerspective) {
                perspectives.add((MPerspective) modelService.cloneSnippet(app, snippet.getElementId(), null));                
            }
        }
        return perspectives;
	}
}

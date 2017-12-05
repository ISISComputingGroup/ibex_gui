package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Class for accessing the perspectives in the application model.
 */
public class PerspectivesProvider {
		
	private final EPartService partService;
	private List<MPerspective> perspectives = new ArrayList<MPerspective>();
	private MPerspectiveStack perspectivesStack;
	private MApplication app;
	private EModelService modelService;
	
    private static final String MAIN_PERSPECTIVE_STACK_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspectivestack.0";
    
    /**
     * Instantiates the class and initialises the internal perspective list with 
     * all perspectives found in the application model.
     * 
	 * @param app The E4 application model
	 * @param partService The E4 service responsible for showing/hiding parts
	 * @param modelService The E4 service responsible for handling model elements
	 */
	public PerspectivesProvider(MApplication app, EPartService partService, EModelService modelService) {
		this.partService = partService;
		this.app = app;
		this.modelService = modelService;
		
		perspectives = new ArrayList<>();
		for (MPerspective perspective : modelService.findElements(app, null, MPerspective.class, null)){
			if (perspective.isVisible()) {
				perspectives.add(perspective);
			}
		}
		
		this.perspectivesStack = modelService.findElements(app, null, MPerspectiveStack.class, null).get(0);
	}
	
	/**
	 * Returns the E4 part service.
	 * 
	 * @return The part service
	 */
	public EPartService getPartService() {
		return partService;
	}
	
	/**
	 * Returns the E4 model service.
	 * 
	 * @return The model service
	 */
	public EModelService getModelService() {
		return modelService;
	}
	
	/**
	 * Returns the E4 application model.
	 * 
	 * @return The application model
	 */
	public MApplication getApp() {
		return app;
	}

	/**
	 * Returns the list of all perspectives.
	 * 
	 * @return The list of perspectives
	 */
	public List<MPerspective> getPerspectives() {
        return perspectives;
	}
	
	private boolean matchPerspectivesById(MPerspective p, String id) {
	    // The 2nd condition is because E4 has a bug that creates orphan perspectives with IDs of the form ...perspective.alarms.<Alarms>
		return p.getElementId().equals(id) || p.getElementId().equals(id + "." + p.getLabel());
	}
	
	/**
	 * Looks for a perspective in the application model given its element ID and
	 * returns the corresponding object if present.
	 * 
	 * @param elementId The ID of the perspective
	 * @return The perspective corresponding to the given ID
	 */
	public MPerspective getPerspective(String elementId) {
		
		for (MPerspective perspective : perspectives) {
			if (matchPerspectivesById(perspective, elementId)) {
				return perspective;
			}
		}
		throw new NoSuchElementException();
	}
	
	/**
	 * Checks whether a given perspective is currently selected.
	 * 
	 * @param perspective The perspective to check
	 * @return True if the given perspective is currently selected, false otherwise
	 */
	public boolean isSelected(MPerspective perspective) {
		return perspectivesStack.getSelectedElement().equals(perspective);		
	}

	/**
	 * Returns the perspective stack top level object.
	 * 
	 * @return The perspective stack
	 */
	public MPerspectiveStack getTopLevelStack() {
		return (MPerspectiveStack) modelService.find(MAIN_PERSPECTIVE_STACK_ID, app);
	}
	
	/**
	 * Returns the list of perspectives present in the E4 application model's snippets.
	 * 
	 * @return The list of perspectives
	 */
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

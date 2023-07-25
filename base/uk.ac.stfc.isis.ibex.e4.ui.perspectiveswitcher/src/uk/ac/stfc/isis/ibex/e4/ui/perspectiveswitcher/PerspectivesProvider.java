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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetupViewModel;

/**
 * Class for accessing the perspectives in the application model.
 */
public class PerspectivesProvider {

    private ExperimentSetupViewModel experimentSetupViewModel;
    private Shell shell;
    private final EPartService partService;
    private List<MPerspective> perspectives = new ArrayList<MPerspective>();
    private MPerspectiveStack perspectivesStack;
    private MApplication app;
    private EModelService modelService;
    
    private static final String MAIN_PERSPECTIVE_STACK_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspectivestack.0";
    private static final String DEFAULT_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.weblinks";
    
    /**
     * Instantiates the class and initialises the internal perspective list with
     * all perspectives found in the application model.
     * 
     * @param app
     *            The E4 application model
     * @param partService
     *            The E4 service responsible for showing/hiding parts
     * @param modelService
     *            The E4 service responsible for handling model elements
     */
    public PerspectivesProvider(MApplication app, EPartService partService, EModelService modelService) {
        this.partService = partService;
        this.app = app;
        this.modelService = modelService;
        experimentSetupViewModel = DaeUI.getDefault().viewModel().experimentSetup();
        
        perspectives = new ArrayList<>();
        for (MPerspective perspective : modelService.findElements(app, null, MPerspective.class, null)) {
            perspectives.add(perspective);
        }

        this.perspectivesStack = modelService.findElements(app, null, MPerspectiveStack.class, null).get(0);
        
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
        // The 2nd condition is because E4 has a bug that creates orphan
        // perspectives with IDs of the form ...perspective.alarms.<Alarms>
        return p.getElementId().equals(id) || p.getElementId().equals(id + "." + p.getLabel());
    }

    /**
     * Looks for a perspective in the application model given its element ID and
     * returns the corresponding object if present.
     * 
     * @param elementId
     *            The ID of the perspective
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
     * @param perspective
     *            The perspective to check
     * @return True if the given perspective is currently selected, false
     *         otherwise
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
     * Returns the list of perspectives present in the E4 application model's
     * snippets.
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
    
    /**
     * @return the default perspective id (usually displayed on launch as landing page)
     */
    public String getDefaultPerspectiveId() {
    	return DEFAULT_PERSPECTIVE_ID;
    }

    /**
     * Switches to a given perspective by id.
     * @param id the id of the perspective to switch to
     */
    public void switchPerspective(String id) {
        switchIfDaeIsNotCurrentPerspective(id);
    }

    private void switchIfDaeIsNotCurrentPerspective(String id) {
        for (MPerspective perspectiveInService : modelService.findElements(app, null, MPerspective.class, null)) {
            if (isSelected(perspectiveInService)) {
                if (perspectiveInService.getLabel().equals("DAE")) {
                    showDialogOrSwitchPerspective(perspectiveInService, id);
                } else {
                    partService.switchPerspective(id);
                    break;
                }
            }
        }
    }

    private void showDialogOrSwitchPerspective(MPerspective perspectiveInService, String id) {
        if (experimentSetupViewModel.getIsChanged()) {
            switchIfDialogConfirmed(perspectiveInService, id);
        } else {
            partService.switchPerspective(id);
        }
    }

    private void switchIfDialogConfirmed(MPerspective perspectiveInService, String id) {
        if (leaveWithoutApplyingDaeChangesDialog(perspectiveInService.getLabel(), shell)) {
            partService.switchPerspective(id);
        }
    }

    private boolean leaveWithoutApplyingDaeChangesDialog(String perspectiveName, Shell shell) {
        return MessageDialog.openQuestion(shell, "Confirm Leaving " + perspectiveName + " Perspective",
                "You have unapplied changes in the \"Experiment Setup\" tab, are you sure you want to leave "
                + "the perspective? \n\nThese changes will remain but will not be applied.");
    }
}

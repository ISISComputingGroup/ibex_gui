package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class PerspectivesVisibleModel extends ModelObject {
    private PerspectivesProvider perspectivesProvider;
    private ArrayList<PerspectiveInfo> perspectiveInfos = new ArrayList<PerspectiveInfo>();
    
    public PerspectivesVisibleModel(MApplication app, EPartService partService, EModelService modelService) {
        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
        
        for (MPerspective perspective : perspectivesProvider.getInitialPerspectives()) {
            boolean locallyVisible = !(new PreferenceSupplier().perspectivesToHide().contains(perspective.getElementId()));
            perspectiveInfos.add(new PerspectiveInfo(perspective.getLabel(), perspective.getElementId(), true, locallyVisible));
        }
    }
    
    public ArrayList<PerspectiveInfo> getPerspectiveInfo() {
        return perspectiveInfos;
    }
    
    public void saveState() {
        List<String> locallyVisiblePerspectiveIDs = perspectiveInfos.stream()
                .filter(persp -> !persp.getVisibleLocally())
                .map(persp -> persp.getId())
                .collect(Collectors.toList());
        new PreferenceSupplier().setPerspectivesToHide(locallyVisiblePerspectiveIDs);
        MPerspectiveStack perspectiveStack = perspectivesProvider.getTopLevelStack();
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                perspectiveStack.getChildren().stream()
                .forEach(persp -> persp.setVisible(!locallyVisiblePerspectiveIDs.contains(persp.getElementId())));
            }
        });
    }
}

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Class containing the eclipse command to switch perspectives.
 */
public class SwitchPerspective {

    /**
     * The application (injected by eclipse framework).
     */
    @Inject
    public MApplication app;

    /**
     * The part service (injected by eclipse framework).
     */
    @Inject
    public EPartService partService;

    /**
     * The model service (injected by eclipse framework).
     */
    @Inject
    public EModelService modelService;

    /**
     * Eclipse command to switch perspectives.
     * 
     * @param id
     *            the id of the perspective to switch to
     */
    @Execute
    public void execute(@Named("uk.ac.stfc.isis.ibex.e4.client.switchperspectives.perspectiveid") String id) {
        PerspectivesProvider provider = new PerspectivesProvider(app, partService, modelService);
        provider.switchPerspective(id);
    }
}

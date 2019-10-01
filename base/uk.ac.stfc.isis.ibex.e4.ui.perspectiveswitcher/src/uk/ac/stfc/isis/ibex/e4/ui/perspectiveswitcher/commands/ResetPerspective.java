package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Class containing the eclipse command to reset perspectives.
 */
public class ResetPerspective extends AbstractHandler{

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

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
      PerspectivesProvider provider = new PerspectivesProvider(app, partService, modelService);
      PerspectiveResetAdapter resetAdapter = new PerspectiveResetAdapter(provider);
      resetAdapter.resetPerspective();
		return null;
	}

    /**
     * Eclipse command to rest perspectives.
     */
//    @Execute
//    public Object execute(ExecutionEvent event) {
//        PerspectivesProvider provider = new PerspectivesProvider(app, partService, modelService);
//        PerspectiveResetAdapter resetAdapter = new PerspectiveResetAdapter(provider);
//        resetAdapter.resetPerspective();
//		return null;
//    }

}

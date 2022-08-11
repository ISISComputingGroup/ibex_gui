package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views.PerspectiveHidingDialog;

/**
 * Class containing the eclipse command to reset perspectives.
 */
public class SelectVisiblePerspectives {   
    @Inject
    private IEclipseContext context;
    
    /**
     * Eclipse command to rest perspectives.
     */
    @Execute
    public void execute() {
        PerspectiveHidingDialog dialog = ContextInjectionFactory.make(PerspectiveHidingDialog.class, context);
        dialog.open();
    }
}

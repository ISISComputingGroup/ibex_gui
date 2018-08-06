package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with resetting the perspective.
 */
public class PerspectiveResetAdapter extends SelectionAdapter {

    private final PerspectivesProvider provider;

    /**
     * Constructor.
     * 
     * @param provider
     *            PerspectivesProvider
     */
    public PerspectiveResetAdapter(PerspectivesProvider provider) {
        this.provider = provider;
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
        resetPerspective();
    }

    /**
     * Method that resets the current perspective.
     */
    public void resetPerspective() {
        MPerspectiveStack perspectiveStack = provider.getTopLevelStack();
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
        perspectiveStack.getSelectedElement().setVisible(true);
        perspectiveStack.setVisible(true);
    }
}

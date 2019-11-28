package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with resetting the perspective.
 */
public class PerspectiveResetAdapter extends SelectionAdapter {

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void widgetSelected(final SelectionEvent event_ignored) {
        resetPerspective();
    }

    /**
     * Method that resets the current perspective.
     */
    public void resetPerspective() {
        final IWorkbench workbench_before = PlatformUI.getWorkbench();
        final IWorkbenchWindow window_before = workbench_before.getActiveWorkbenchWindow();
        System.out.println("Window before is " + window_before);
        final IWorkbenchPage page_before = window_before.getActivePage();
        
        page_before.resetPerspective();
        
        final IWorkbench workbench_after = PlatformUI.getWorkbench();
        final IWorkbenchWindow window_after = workbench_after.getActiveWorkbenchWindow();
        final IWorkbenchPage page_after = window_after.getActivePage();
        
        System.out.println(page_before + ", " + page_after);
    }
}

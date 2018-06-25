package uk.ac.stfc.isis.ibex.ui.scripting;

import javax.inject.Inject;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Perspective factory, defines how the perspective is initialised.
 */
public class PerspectiveFactory implements IPerspectiveFactory {
	/**
     * The ID of this perspective.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective";

    /**
     * {@inheritDoc}
     */
	@Inject
	@Override
	public void createInitialLayout(IPageLayout layout) {
		System.out.println("HELLO");
		Consoles.createConsole();
	}
}

package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {
	
	/**
     * The ID of this perspective.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective";

    /**
     * {@inheritDoc}
     */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		Consoles.createConsole();
	}

}

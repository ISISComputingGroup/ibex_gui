package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveResetAdapter extends SelectionAdapter {

	private final PerspectivesProvider provider;

	public PerspectiveResetAdapter(PerspectivesProvider provider) {
		super();
		this.provider = provider;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
        MPerspectiveStack perspectiveStack = provider.getTopLevelStack();
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
        perspectiveStack.getSelectedElement().setVisible(true);
        perspectiveStack.setVisible(true);
	}
}

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PlatformUI;

public class PerspectiveResetAdapter extends SelectionAdapter {

	private final PerspectivesProvider provider;
    private static final String MAIN_PERSPECTIVE_STACK_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspectivestack.0";

	public PerspectiveResetAdapter(PerspectivesProvider provider) {
		super();
		this.provider = provider;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
        MPerspectiveStack perspectiveStack = (MPerspectiveStack) provider.getModelService().find(MAIN_PERSPECTIVE_STACK_ID, provider.getApp());
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
        perspectiveStack.getSelectedElement().setVisible(true);
        perspectiveStack.setVisible(true);
	}
}

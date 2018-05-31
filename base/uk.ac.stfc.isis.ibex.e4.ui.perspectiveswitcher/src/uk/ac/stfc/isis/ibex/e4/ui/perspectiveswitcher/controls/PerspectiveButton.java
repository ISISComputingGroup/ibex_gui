package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveButton extends Button {
    
    private final MPerspective perspective;
    private final PerspectivesProvider perspectivesProvider;

	public PerspectiveButton(Composite parent, MPerspective perspective, PerspectivesProvider perspectivesProvider) {
		super(parent, perspective.getLabel(), perspective.getIconURI(), perspective.getTooltip());
		
		this.perspective = perspective;
		this.perspectivesProvider = perspectivesProvider;
	}
	
	@Override
	protected void mouseClickAction() {
		super.mouseClickAction();
		perspectivesProvider.getPartService().switchPerspective(perspective);
	}
}

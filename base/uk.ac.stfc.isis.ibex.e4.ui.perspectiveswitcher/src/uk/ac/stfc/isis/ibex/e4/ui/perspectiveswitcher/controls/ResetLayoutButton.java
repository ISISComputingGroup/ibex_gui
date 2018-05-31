package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class ResetLayoutButton extends Button {

	private final PerspectiveResetAdapter resetAdapter;
	private static final String RESET_PERSPECTIVE_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/reset.png";

	public ResetLayoutButton(Composite parent, PerspectivesProvider perspectivesProvider) {
		super(parent, "Reset Layout", RESET_PERSPECTIVE_URI, "Sets the layout of the current perspective back to its default");
		resetAdapter = new PerspectiveResetAdapter(perspectivesProvider);
	}
	
	@Override
	protected void mouseClickAction() {
		resetAdapter.widgetSelected(null);
	}
}

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Reset Layout button. Sits apart from the Perspective Buttons.
 * 
 */
public class ResetLayoutButton extends Button {

    private final PerspectiveResetAdapter resetAdapter;
    private final ResetLayoutButtonViewModel model;
    private static final String RESET_PERSPECTIVE_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/reset.png";

    /**
     * Constructor.
     * 
     * @param parent
     *            Composite
     * @param perspectivesProvider
     *            PerspectivesProvider
     * @param model
     *            ResetLayoutButtonViewModel
     */
    public ResetLayoutButton(Composite parent, PerspectivesProvider perspectivesProvider, ResetLayoutButtonViewModel model) {
        super(parent, RESET_PERSPECTIVE_URI, "Reset Layout: Sets the layout of the current perspective back to its default", model);
        resetAdapter = new PerspectiveResetAdapter(perspectivesProvider);
        this.model = model;
    }

    @Override
    protected void mouseClickAction() {
        resetAdapter.widgetSelected(null);
        ResetLayoutButtonModel.getInstance().setChanged(false);
    }
}

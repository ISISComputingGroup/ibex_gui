package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Class for Perspective Buttons.
 *
 */
public class PerspectiveButton extends Button {

    private final MPerspective perspective;
    private final PerspectivesProvider perspectivesProvider;

    /**
     * Call constructor for Button and copy passed perspective and
     * perspectivesProvider.
     * 
     * @param parent
     *            Composite
     * @param perspective
     *            MPerspective
     * @param perspectivesProvider
     *            PerspectivesProvider
     * @param model
     *            ButtonViewModel
     */
    public PerspectiveButton(Composite parent, MPerspective perspective, PerspectivesProvider perspectivesProvider,
            PerspectiveButtonViewModel model) {
        super(parent, perspective.getIconURI(), perspective.getLabel() + ": " + perspective.getTooltip(), model);

        this.perspective = perspective;
        this.perspectivesProvider = perspectivesProvider;
        if (this.perspectivesProvider.isSelected(perspective)) {
            model.setActive(true);
        }
    }

    @Override
    protected void mouseClickAction() {
        super.mouseClickAction();
        perspectivesProvider.switchPerspective(perspective.getElementId());
    }
}

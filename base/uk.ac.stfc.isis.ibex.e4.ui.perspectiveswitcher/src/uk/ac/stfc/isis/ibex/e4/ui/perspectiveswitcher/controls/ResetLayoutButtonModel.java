package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ResetLayoutButtonModel extends ModelObject {
    private boolean changed = false;
    private MPerspective currentPerspective;
    private Set<MPerspective> changedPerspectives = new HashSet<MPerspective>();

    private static ResetLayoutButtonModel instance = null;

    /**
     * Get the instance of this singleton.
     *
     * @return The instance of this singleton.
     */
    public static ResetLayoutButtonModel getInstance() {
        if (instance == null) {
            instance = new ResetLayoutButtonModel();
        }
        return instance;
    }

    /**
     * Reset changed to false a set the current perspective.
     *
     * @param perspective
     *            The perspective that is now the current perspective.
     */
    public void reset(MPerspective perspective) {
        this.changed = false;
        this.currentPerspective = perspective;
    }

    /**
     * Checks whether the current layout has changed.
     *
     * @return isChanged True if the current perspective has changed.
     */
    public boolean isChanged() {
        return this.changed;
    }

    /**
     * Set whether or not the layout of the current perspective has changed.
     *
     * @param changed
     *            True if the layout of the current perspective has changed.
     *            False if not.
     */
    public void setChanged(boolean changed) {
        currentPerspective = getCurrentPerspective();
        if (changed) {
            this.changedPerspectives.add(currentPerspective);
        } else {
            this.changedPerspectives.remove(currentPerspective);
        }
        firePropertyChange("layoutModified", null, this.changed = changed);
    }

    /**
     * Get the set of changed perspectives.
     *
     * @return The set of changed perspectives.
     */
    public Set<MPerspective> getChangedPerspectives() {
        return this.changedPerspectives;
    }

    /**
     * Return the current perspective.
     *
     * @return The current perspective.
     */
    public MPerspective getCurrentPerspective() {
        return this.currentPerspective;
    }

    /**
     * Set the current perspective.
     *
     * @param perspective
     *            The perspective that is now the current perspective.
     */
    public void setCurrentPerspective(MPerspective perspective) {
        this.currentPerspective = perspective;
        if (this.changedPerspectives.contains(perspective)) {
            this.setChanged(true);
        } else {
            this.setChanged(false);
        }
    }
}

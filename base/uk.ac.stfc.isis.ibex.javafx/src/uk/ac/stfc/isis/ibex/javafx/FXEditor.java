package uk.ac.stfc.isis.ibex.javafx;

import org.eclipse.fx.ui.workbench3.FXViewPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.EditorPart;

import javafx.scene.Scene;

public abstract class FXEditor extends EditorPart {
	@Override
    public void createPartControl(Composite parent) {
        FXViewPart part = new FXViewPart() {
            @Override
            protected void setFxFocus() {
            	FXEditor.this.setFxFocus();
            }

            @Override
            protected Scene createFxScene() {
                return FXEditor.this.createFxScene();
            }
        };
        part.createPartControl(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus() {
        setFxFocus();
    }

    /**
     * Set the focus on the FX {@link Node} when this editor receives focus.
     */
    protected abstract void setFxFocus();

    /**
     * Construct the main scene that will be placed on the canvas of this editor.
     *
     * @return the scene
     */
    protected abstract Scene createFxScene();
}

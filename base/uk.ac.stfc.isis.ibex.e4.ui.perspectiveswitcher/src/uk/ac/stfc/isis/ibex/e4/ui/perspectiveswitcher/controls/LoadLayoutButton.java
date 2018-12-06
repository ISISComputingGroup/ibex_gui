package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * Reset Layout button. Sits apart from the Perspective Buttons.
 * 
 */
public class LoadLayoutButton extends Button {

    private final SelectionAdapter adapter;

    /**
     * Constructor.
     * 
     * @param parent
     *            Composite
     * @param perspectivesProvider
     *            PerspectivesProvider
     */
    public LoadLayoutButton(Composite parent, PerspectivesProvider perspectivesProvider) {
        super(parent, ResetLayoutButton.RESET_PERSPECTIVE_URI, "Sets the layout of the current perspective back to its default",
                new ResetLayoutButtonViewModel());
        model.setText("Load Layout");
        adapter = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		
        	}
		};
    }

    @Override
    protected void mouseClickAction() {
        adapter.widgetSelected(null);
        ResetLayoutButtonModel.getInstance().setChanged(false);
    }
}

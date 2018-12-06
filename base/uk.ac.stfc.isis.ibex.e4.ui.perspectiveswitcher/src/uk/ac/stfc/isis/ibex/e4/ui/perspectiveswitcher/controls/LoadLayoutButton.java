package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

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
    public LoadLayoutButton(Composite parent) {
        super(parent, ResetLayoutButton.RESET_PERSPECTIVE_URI, "Sets the layout of the current perspective back to its default",
                new ButtonViewModel());
        model.setText("Load Layout");
        adapter = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		boolean loadAndRestart = MessageDialog.openQuestion(
        				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
        				"Close user interface and load layout?", 
        				"Loading a layout requires the user interface to restart. This will terminate running scripts. \n\nWould you like to restart now to load a layout?");
        		
        		if (loadAndRestart) {
	        		IsisLog.getLogger(getClass()).info("User interface restarting to load a layout.");
	        		System.setProperty("SHUTDOWN_WITHOUT_PROMPT", Boolean.TRUE.toString());
	        		PlatformUI.getWorkbench().restart();
        		}
        	}
		};
    }

    @Override
    protected void mouseClickAction() {
        adapter.widgetSelected(null);
    }
}

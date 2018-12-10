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
    
    private static final String RESET_PERSPECTIVE_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/reset.png";
    
    private static final String RESTART_GUI_TITLE = "Load Layout";
    
    private static final String RESTART_GUI_PROMPT = "Would you like to restart the user interface now and load the last saved layout?"
			+ "\n\nWarning: this will terminate scripts which are currently running in the client.";

    /**
     * Constructor.
     * 
     * @param parent
     *            Composite
     * @param perspectivesProvider
     *            PerspectivesProvider
     */
    public LoadLayoutButton(Composite parent) {
        super(parent, RESET_PERSPECTIVE_URI, "Loads the last saved user interface layout",
                new ButtonViewModel());
        
        model.setText("Load Layout");
        adapter = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		boolean loadAndRestart = MessageDialog.openQuestion(
        				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        				RESTART_GUI_TITLE, RESTART_GUI_PROMPT);
        		
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

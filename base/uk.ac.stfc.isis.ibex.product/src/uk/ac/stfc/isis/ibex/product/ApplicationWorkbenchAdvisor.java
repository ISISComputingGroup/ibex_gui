package uk.ac.stfc.isis.ibex.product;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.ui.perspectives.Startup;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	private static final String DIALOG_BOX_TITLE = "Close the application?";
	private static final String DIALOG_QUESTION = "Are you sure you want to close this application?";
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return Startup.ID;
	}
	
	@Override
	public boolean preShutdown() {
		Instrument.getInstance().setInitial();
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		return MessageDialog.openQuestion(shell, DIALOG_BOX_TITLE, DIALOG_QUESTION);
	}
}

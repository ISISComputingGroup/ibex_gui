package uk.ac.stfc.isis.ibex.ui.instrument;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.scripting.Consoles;

public class InstrumentHandler extends AbstractHandler {

	private final Consoles consoles = Consoles.getDefault();
	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InstrumentDialog dialog = new InstrumentDialog(shell);
		
		if (dialog.open() == Window.CANCEL || dialog.selectedInstrument() == null) {
			return null;
		}
		
		InstrumentInfo selected = dialog.selectedInstrument();
		if (!consoles.anyActive()) {
			setInstrument(selected);
			return null;
		}
			
		if (shouldCloseAllConsoles(dialog)) {
			consoles.closeAll();
		}
		setInstrument(selected);
		consoles.createConsole();
		
		return null;
	}

	private boolean shouldCloseAllConsoles(InstrumentDialog dialog) {
		return MessageDialog.openConfirm(
				dialog.getShell(), 
				"Confirm Instrument Switch", 
				"All console scripting sessions for this instrument will be closed.\nWould you like to continue?");
	}

	private void setInstrument(InstrumentInfo selected) {
		InstrumentUI.INSTRUMENT.setInstrument(selected);
	}
}

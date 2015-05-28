package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;

public class LoadSynopticHandler extends SynopticHandler {

	private static final String TITLE = "Load Synoptic";
	
	public LoadSynopticHandler() {
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Synoptic synoptic = Synoptic.getInstance();
		SynopticSelectionDialog dialog = new SynopticSelectionDialog(shell, TITLE, synoptic.availableSynoptics());
		if (dialog.open() == Window.OK) {
			synoptic.setViewerSynoptic(dialog.selectedSynoptic());
		}
		return null;
	}
}

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.MultipleSynopticsSelectionDialog;

public class DeleteSynopticHandler extends SynopticHandler {

	private static final String TITLE = "Delete Synoptic";

	public DeleteSynopticHandler() {
		super();
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		MultipleSynopticsSelectionDialog dialog = new MultipleSynopticsSelectionDialog(shell(), TITLE, SYNOPTIC.availableSynoptics());
		if (dialog.open() == Window.OK) {
			delete(dialog.selectedSynoptics());
		}
		return null;
	}
	
	private void delete(Collection<String> names)
	{
		SYNOPTIC.edit().deleteSynoptics().write(names);
	}
}

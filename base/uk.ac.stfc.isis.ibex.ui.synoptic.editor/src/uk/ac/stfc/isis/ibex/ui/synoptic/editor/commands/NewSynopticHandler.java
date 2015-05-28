package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class NewSynopticHandler extends SynopticHandler {

	private static final String TITLE = "New Synoptic";
	
	public NewSynopticHandler() {
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		openDialog(new InstrumentDescription(), TITLE, true);
		return null;
	}
}

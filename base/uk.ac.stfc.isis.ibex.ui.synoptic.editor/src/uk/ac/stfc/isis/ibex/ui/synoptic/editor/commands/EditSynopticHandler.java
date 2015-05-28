package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;

public class EditSynopticHandler extends SynopticHandler {

	private static final String TITLE = "Edit Synoptic";

	public EditSynopticHandler() {
		super();
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		SynopticSelectionDialog dialog = new SynopticSelectionDialog(shell(), TITLE, SYNOPTIC.availableSynoptics());
		if (dialog.open() == Window.OK) {
			openDialog(load(dialog.selectedSynoptic()), TITLE, false);
		}
		return null;
	}
	
	private InstrumentDescription load(SynopticInfo info)
	{
		UpdatedValue<InstrumentDescription> instrumentDescription = new UpdatedObservableAdapter<>(SYNOPTIC.synoptic(info));					
		if (Awaited.returnedValue(instrumentDescription, 1)) {
			return instrumentDescription.getValue();
		}
		return null;
	}
}

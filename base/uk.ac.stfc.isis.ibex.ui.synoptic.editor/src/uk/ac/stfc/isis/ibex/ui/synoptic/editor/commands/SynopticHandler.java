package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.EditSynopticDialog;

public abstract class SynopticHandler extends AbstractHandler {

	protected final Synoptic SYNOPTIC = Synoptic.getInstance();
	
	public SynopticHandler() {
		SYNOPTIC.edit().saveSynoptic().canSave().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setBaseEnabled(SYNOPTIC.edit().saveSynoptic().canSave().getValue());
			}
		});
	}		
	
	protected void openDialog(InstrumentDescription synoptic, String title, boolean isBlank) {
		EditSynopticDialog editDialog = new EditSynopticDialog(shell(), title, synoptic, isBlank);	
		if (editDialog.open() == Window.OK) {
			SYNOPTIC.edit().saveSynoptic().write(editDialog.getSynoptic());
		}
	}
	
	protected Shell shell()
	{
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}	
	
}

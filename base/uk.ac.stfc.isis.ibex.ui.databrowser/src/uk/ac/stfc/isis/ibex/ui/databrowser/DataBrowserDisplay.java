package uk.ac.stfc.isis.ibex.ui.databrowser;

import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;

import uk.ac.stfc.isis.ibex.ui.UI;

public class DataBrowserDisplay {
		
	public void displayPVHistory(String pvAddress) {	
				
		UI.getDefault().switchPerspective(Perspective.ID);
		
	    // Create new editor
	    final DataBrowserEditor editor = DataBrowserEditor.createInstance();
	    if (editor == null) {
	        return;
	    }
	    
	    // Add received items
	    final Model model = editor.getModel();
	    final double period = Preferences.getScanPeriod();
	    try {
			final PVItem item = new PVItem(pvAddress, period);
			item.useDefaultArchiveDataSources();
			// Add item to new axes
			item.setAxis(model.addAxis(item.getDisplayName()));
			model.addItem(item);
	    } catch (Exception ex) {
	        MessageDialog.openError(editor.getSite().getShell(),
	                Messages.Error,
	                NLS.bind(Messages.ErrorFmt, ex.getMessage()));
	    }
	}
}

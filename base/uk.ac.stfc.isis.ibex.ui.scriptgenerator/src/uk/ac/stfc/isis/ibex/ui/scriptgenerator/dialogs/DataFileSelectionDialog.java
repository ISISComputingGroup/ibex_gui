package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.dialogs.SelectionDialog;
/**
 * Dialog for asking user to select the data file they want to load on script generator
 * @author mjq34833
 *
 */
public class DataFileSelectionDialog extends SelectionDialog{
	/**
	 *List of available data file
	 */
	private List<String> available;
	/**
	 * User selected file
	 */
	private Collection<String> selected;
	
	public DataFileSelectionDialog(Shell parentShell, String title, List<String> availableData) {
		super(parentShell, title);
		this.available = availableData;
		
	}

	@Override
	protected void createSelection(Composite container) {
		Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select a file to load:");
        items = createTable(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        
        String [] names;
        names = available.toArray(new String[0]);
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        setItems(names);
        
		Group group = new Group(container, SWT.SHADOW_IN);
		group.setLayout(new RowLayout(SWT.HORIZONTAL));

		
	}
	
	@Override
	protected void okPressed() {
		selected = asString(items.getSelection());
		super.okPressed();
	}

	/**
	 * Return user selected file
	 * @return String representation of user selected file name
	 */
	public String selectedFile() {
		return selected.toArray(new String[1])[0];
	}


}

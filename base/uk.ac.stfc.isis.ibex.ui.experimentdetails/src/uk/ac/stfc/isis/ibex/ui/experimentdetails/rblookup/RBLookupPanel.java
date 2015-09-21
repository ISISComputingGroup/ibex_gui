package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.database.SearchModel;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.UserDetailsTable;

public class RBLookupPanel extends Composite{
	private Text txtName;
	private ComboViewer cmboRole;
	private UserDetailsTable experimentIDTable;
	private final RBLookupDialog dialog;
	
	private SearchModel SEARCH = ExperimentDetails.getInstance().searchModel();
	
	private final PropertyChangeListener resultsListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			Collection<UserDetails> results = SEARCH.getSearchResults();
			experimentIDTable.setRows(results);	
			dialog.setOKEnabled(!results.isEmpty());
		}
	};
	
	public RBLookupPanel(Composite parent, int style, final RBLookupDialog dialog) {
		super(parent, style);
		this.dialog = dialog;
		
		setLayout(new GridLayout(1, false));
		
		Composite searchComposite = new Composite(this, SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		searchComposite.setLayout(new GridLayout(3, false));
		
		Label lblName = new Label(searchComposite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(searchComposite, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		txtName.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				searchForExperimentID();
			}
		});
		
		Label lblRole = new Label(searchComposite, SWT.NONE);
		lblRole.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRole.setText("Role:");
		
		cmboRole = new ComboViewer(searchComposite, SWT.READ_ONLY);
		cmboRole.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmboRole.setContentProvider(ArrayContentProvider.getInstance());
		cmboRole.setInput(Role.values());
		cmboRole.getCombo().select(0);
		
		cmboRole.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				searchForExperimentID();
			}
		});

		new Label(searchComposite, SWT.NONE);
		new Label(searchComposite, SWT.NONE);
		
		Button btnSearch = new Button(searchComposite, SWT.NONE);
		GridData gd_btnSearch = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnSearch.widthHint = 100;
		btnSearch.setLayoutData(gd_btnSearch);
		btnSearch.setText("Search");
		btnSearch.setFocus();
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				searchForExperimentID();
			}
		});
		
		experimentIDTable = new UserDetailsWithExperimentsTable(this, SWT.NONE, SWT.NO_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		experimentIDTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		experimentIDTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				dialog.okPressed();
			}
		});
		
		bindTable();
		
	}
	
	private void searchForExperimentID() {
		int roleIndex = cmboRole.getCombo().getSelectionIndex();
		Role role = Arrays.asList(Role.values()).get(roleIndex);
		
		SEARCH.searchExperiments(txtName.getText(), role);
	}
	
	public void bindTable() {
		SEARCH.addPropertyChangeListener("searchResults", resultsListener);
	}
	
	public UserDetails getSelectedUser() {
		return experimentIDTable.firstSelectedRow();
	}
	
	public void close() {
		SEARCH.removePropertyChangeListener("searchResults", resultsListener);
		SEARCH.clearResults();
	}
}

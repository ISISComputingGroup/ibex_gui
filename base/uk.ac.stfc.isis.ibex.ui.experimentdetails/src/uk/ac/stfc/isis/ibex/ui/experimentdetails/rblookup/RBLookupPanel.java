package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.experimentdetails.UserDetailsTable;

@SuppressWarnings("checkstyle:magicnumber")
public class RBLookupPanel extends Composite {
	private Text txtName;
	private ComboViewer cmboRole;
	private UserDetailsTable experimentIDTable;
	private DateTime date;
	private Button btnSearch;
	private Button btnOnDate;
	
	private DataBindingContext bindingContext;
	
	public RBLookupPanel(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new GridLayout(1, false));
		
		Composite searchComposite = new Composite(this, SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		searchComposite.setLayout(new GridLayout(4, false));
		
		Label lblName = new Label(searchComposite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(searchComposite, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblRole = new Label(searchComposite, SWT.NONE);
		lblRole.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRole.setText("Role:");
		
		cmboRole = new ComboViewer(searchComposite, SWT.READ_ONLY);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gridData.widthHint = 144;
		cmboRole.getCombo().setLayoutData(gridData);
		cmboRole.setContentProvider(ArrayContentProvider.getInstance());
		cmboRole.setInput(RoleViews.values());
		
		btnOnDate = new Button(searchComposite, SWT.CHECK);
		btnOnDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnOnDate.setText("On Date:");
		
		date = new DateTime(searchComposite, SWT.BORDER | SWT.DROP_DOWN);
		
		new Label(searchComposite, SWT.NONE);
		new Label(searchComposite, SWT.NONE);
		new Label(searchComposite, SWT.NONE);
		
		btnSearch = new Button(searchComposite, SWT.NONE);
		GridData gdBtnSearch = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdBtnSearch.widthHint = 100;
		btnSearch.setLayoutData(gdBtnSearch);
		btnSearch.setText("Search");
		btnSearch.setFocus();
		
		experimentIDTable = new UserDetailsWithExperimentsTable(this, SWT.NONE, SWT.NO_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		experimentIDTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	}
	
	public void setModel(final RBLookupViewModel viewModel) {
		bindingContext = new DataBindingContext();
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(date), BeanProperties.value("dateEnabled").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.selection().observe(date), BeanProperties.value("dateSearch").observe(viewModel));
		bindingContext.bindValue(ViewersObservables.observeSingleSelection(cmboRole), BeanProperties.value("roleSearch").observe(viewModel));
        bindingContext.bindValue(SWTObservables.observeText(txtName, SWT.Modify), BeanProperties.value("nameSearch").observe(viewModel));
		
		experimentIDTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				viewModel.setSelectedUser(experimentIDTable.firstSelectedRow());
			}
		});
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				viewModel.searchForExperimentID();
			}
		});
		
		viewModel.addPropertyChangeListener("searchResults", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				experimentIDTable.setRows(viewModel.searchResults());
			}
		});
		
		btnOnDate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				viewModel.setDateEnabled(btnOnDate.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		txtName.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				viewModel.searchForExperimentID();
			}
		});
		
		bindingContext.updateModels();
	}

}

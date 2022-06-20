package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

/**
 * A panel for looking up RB numbers. It is a composite GUI element that is 
 * the parent of all GUI elements used for searching RB numbers. This Panel 
 * is meant to be but in a RBLookupDialog window.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class RBLookupPanel extends Composite {
    
    private static final String NAME_SEARCH_WARNING = "Warning! This dialog is only for searching "
            + "for scheduled runs. For Xpress runs, you will have to \ntype in your RB number "
            + "manually in the RB Number text box in Experiment Details.";
    
	private Text txtName;
	private ComboViewer cmboRole;
	private UserDetailsTable experimentIDTable;
	private DateTime date;
	private Button btnSearch;
	private Button btnOnDate;
	
	private DataBindingContext bindingContext;
	
	/**
	 * Creates an RB lookup panel. Adds all labels, buttons, etc. to the panel.
	 *
	 * @param parent the parent composite.
	 * @param style the SWT style.
	 */
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
		
		Label searchWarning = new Label(searchComposite, SWT.NONE);
		searchWarning.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
		searchWarning.setText(NAME_SEARCH_WARNING);
		
		experimentIDTable = new UserDetailsWithExperimentsTable(this, SWT.NONE, 
		        SWT.FULL_SELECTION | SWT.BORDER);
		experimentIDTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	/**
	 * Sets the model and binds all of the values.
	 * 
	 * @param viewModel the view model.
	 */
	public void setModel(final RBLookupViewModel viewModel) {
		bindingContext = new DataBindingContext();
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(date), BeanProperties.value("dateEnabled").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.dateTimeSelection().observe(date), BeanProperties.value("dateSearch").observe(viewModel));
		bindingContext.bindValue(ViewerProperties.singleSelection().observe(cmboRole), BeanProperties.value("roleSearch").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName), BeanProperties.value("nameSearch").observe(viewModel));
        
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
		
        btnOnDate.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				viewModel.setDateEnabled(btnOnDate.getSelection());
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

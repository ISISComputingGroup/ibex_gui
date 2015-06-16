package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservingTextBox;

public class ExperimentDetailsPanel extends Composite {
	
	private final ViewModel viewModel = new ViewModel();
	
	private final ParametersTable sampleParameters;
	private final ParametersTable beamParameters;
	private final UserDetailsTable userDetails;
	private Label lblRbNumber;
	private Label lblSampleParameters;
	private Label lblBeamlineParameters;
	private Label lblExperimentTeam;
	private WritableObservingTextBox rbNumber;
	private Button btnUpdateUserDetails;
	private Button btnAddUserDetails;
	private Composite experimentTeamButtons;
	private Button btnClearUserDetails;
	
	public ExperimentDetailsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		
		lblRbNumber = new Label(composite, SWT.NONE);
		lblRbNumber.setText("RB Number");
		
		rbNumber = new WritableObservingTextBox(composite, SWT.NONE, viewModel.rbNumber);
		GridData gd_rbNumber = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_rbNumber.minimumWidth = 100;
		gd_rbNumber.widthHint = 100;
		rbNumber.setLayoutData(gd_rbNumber);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblExperimentTeam = new Label(composite, SWT.NONE);
		lblExperimentTeam.setText("Experiment Team");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		userDetails = new UserDetailsTable(composite, SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd_userDetails = new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1);
		gd_userDetails.heightHint = 110;
		gd_userDetails.minimumHeight = 110;
		gd_userDetails.widthHint = 400;
		gd_userDetails.minimumWidth = 400;
		userDetails.setLayoutData(gd_userDetails);
		
		updateUserDetails();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateUserDetails();				
			}
		});
		
		experimentTeamButtons = new Composite(composite, SWT.NONE);
		experimentTeamButtons.setLayout(new GridLayout(1, false));
		
		btnAddUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnAddUserDetails.setSize(34, 25);
		btnAddUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.addUser();
				viewModel.model.sendUserDetails();
			}
		});
		btnAddUserDetails.setText("Add");
		
		btnClearUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnClearUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.clearUserDetails();
			}
		});
		btnClearUserDetails.setText("Clear");
		
		btnUpdateUserDetails = new Button(experimentTeamButtons, SWT.NONE);
		btnUpdateUserDetails.setSize(50, 25);
		btnUpdateUserDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.model.sendUserDetails();
			}
		});
		btnUpdateUserDetails.setText("Update");
		new Label(composite, SWT.NONE);
		
		lblSampleParameters = new Label(composite, SWT.NONE);
		lblSampleParameters.setText("Sample Parameters");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		sampleParameters = new ParametersTable(composite, SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd_sampleParameters = new GridData(SWT.LEFT, SWT.FILL, false, true, 4, 1);
		gd_sampleParameters.widthHint = 400;
		gd_sampleParameters.minimumWidth = 400;
		gd_sampleParameters.minimumHeight = 150;
		gd_sampleParameters.heightHint = 150;
		sampleParameters.setLayoutData(gd_sampleParameters);
		
		updateSampleParameters();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateSampleParameters();
				
			}
		});
		
		lblBeamlineParameters = new Label(composite, SWT.NONE);
		lblBeamlineParameters.setText("Beamline Parameters");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		beamParameters = new ParametersTable(composite, SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd_beamParameters = new GridData(SWT.LEFT, SWT.FILL, true, true, 4, 1);
		gd_beamParameters.widthHint = 400;
		gd_beamParameters.minimumWidth = 400;
		gd_beamParameters.minimumHeight = 200;
		gd_beamParameters.heightHint = 200;
		beamParameters.setLayoutData(gd_beamParameters);
		
		updateBeamParameters();
		viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateBeamParameters();
			}
		});
	}
	
	private void updateUserDetails() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				userDetails.setRows(viewModel.model.getUserDetails());
			}	
		});
	}

	protected void updateSampleParameters() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				sampleParameters.setRows(viewModel.model.getSampleParameters());
			}	
		});
	}
	
	protected void updateBeamParameters() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				beamParameters.setRows(viewModel.model.getBeamParameters());
			}	
		});
	}
}

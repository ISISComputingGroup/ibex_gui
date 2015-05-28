package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsPanel;

public class ExperimentSetup extends Composite {

	private ExperimentSetupViewModel viewModel;
	
	private TimeChannelsPanel timeChannels;
	private DataAcquisitionPanel dataAcquisition;
	private PeriodsPanel periods;
	
	private final int timeToDisplayDialog = 2;
	
	private SendingChangesDialog sendingChanges = new SendingChangesDialog(getShell(), timeToDisplayDialog);
	
	public ExperimentSetup(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		CTabFolder tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmTimeChannels = new CTabItem(tabFolder, SWT.NONE);
		tbtmTimeChannels.setText("Time Channels");
		
		Composite timeChannelsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmTimeChannels.setControl(timeChannelsComposite);
		timeChannelsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		timeChannels = new TimeChannelsPanel(timeChannelsComposite, SWT.NONE);
		
		CTabItem tbtmDataAcquisition = new CTabItem(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setText("Data Acquisition");
		
		Composite dataAcquisitionComposite = new Composite(tabFolder, SWT.NONE);
		tbtmDataAcquisition.setControl(dataAcquisitionComposite);
		dataAcquisitionComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		dataAcquisition = new DataAcquisitionPanel(dataAcquisitionComposite, SWT.NONE);
		
		CTabItem tbtmPeriods = new CTabItem(tabFolder, SWT.NONE);
		tbtmPeriods.setText("Periods");
		
		Composite periodsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmPeriods.setControl(periodsComposite);
		periodsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		periods = new PeriodsPanel(periodsComposite, SWT.NONE);
		
		tabFolder.setSelection(0);
		
		Composite sendChangesComposite = new Composite(this, SWT.NONE);
		sendChangesComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		sendChangesComposite.setLayout(new GridLayout(1, false));
		
		Button btnSendChanges = new Button(sendChangesComposite, SWT.NONE);
		btnSendChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (viewModel != null) {
					viewModel.updateDae();
				}
				sendingChanges.open();
			}
		});
		btnSendChanges.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSendChanges.setText("Send Changes");
	}
	
	public void bind(final ExperimentSetupViewModel viewModel) {
		this.viewModel = viewModel;
		
		timeChannels.setModel(viewModel.timeChannels());
		dataAcquisition.setModel(viewModel.daeSettings());
		periods.setModel(viewModel.periodSettings());
	}

}

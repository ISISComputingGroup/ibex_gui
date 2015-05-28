package uk.ac.stfc.isis.ibex.ui.dae.run;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.dae.widgets.LogMessageBox;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservingTextBox;

public class RunSummary extends Composite {

	private Label instrument;
	private Label runStatus;
	private Label runNumber;
	private Label isisCycle;
	private WritableObservingTextBox title;
	private LogMessageBox messageBox;
	
	private DaeActionButtonPanel daeButtonPanel;
	
	public RunSummary(Composite parent, int style, RunSummaryViewModel model) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Composite lhsComposite = new Composite(this, SWT.NONE);
		lhsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lhsComposite.setLayout(new GridLayout(1, false));
		
		Composite infoComposite = new Composite(lhsComposite, SWT.NONE);
		infoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		infoComposite.setLayout(new GridLayout(5, false));
		
		Label lblInstrument = new Label(infoComposite, SWT.NONE);
		lblInstrument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInstrument.setText("Instrument:");
		
		instrument = new Label(infoComposite, SWT.NONE);
		GridData gd_instrument = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_instrument.minimumWidth = 100;
		gd_instrument.widthHint = 100;
		instrument.setLayoutData(gd_instrument);
		instrument.setText("UNKNOWN");
		Label lblRunStatus = new Label(infoComposite, SWT.NONE);
		lblRunStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunStatus.setText("Run Status:");
		
		runStatus = new Label(infoComposite, SWT.NONE);
		GridData gd_runStatus = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_runStatus.widthHint = 100;
		gd_runStatus.minimumWidth = 100;
		runStatus.setLayoutData(gd_runStatus);
		runStatus.setText("UNKNOWN");
		
		Label spacer = new Label(infoComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblRunNumber = new Label(infoComposite, SWT.NONE);
		lblRunNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunNumber.setText("Run Number:");
		
		runNumber = new Label(infoComposite, SWT.NONE);
		GridData gd_runNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_runNumber.minimumWidth = 100;
		gd_runNumber.widthHint = 100;
		runNumber.setLayoutData(gd_runNumber);
		runNumber.setText("UNKNOWN");
		
		Label lblIsisCycle = new Label(infoComposite, SWT.NONE);
		lblIsisCycle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIsisCycle.setText("ISIS Cycle:");
		
		isisCycle = new Label(infoComposite, SWT.NONE);
		GridData gd_isisCycle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_isisCycle.minimumWidth = 100;
		gd_isisCycle.widthHint = 100;
		isisCycle.setLayoutData(gd_isisCycle);
		isisCycle.setText("UNKNOWN");
		
		Label spacer2 = new Label(infoComposite, SWT.NONE);
		spacer2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblTitle = new Label(infoComposite, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText("Title:");
		
		title = new WritableObservingTextBox(infoComposite, SWT.NONE, model.title());
		title.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		GridData gd_title = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_title.widthHint = 180;
		title.setLayoutData(gd_title);
		new Label(infoComposite, SWT.NONE);

		messageBox = new LogMessageBox(lhsComposite, SWT.NONE);
		messageBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
				
		daeButtonPanel = new DaeActionButtonPanel(this, SWT.NONE, model.actions());
		daeButtonPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}
	
	public void setModel(RunSummaryViewModel viewModel) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(instrument), BeanProperties.value("value").observe(viewModel.instrument()));
		bindingContext.bindValue(WidgetProperties.text().observe(runStatus), BeanProperties.value("value").observe(viewModel.runStatus()));
		bindingContext.bindValue(WidgetProperties.text().observe(runNumber), BeanProperties.value("value").observe(viewModel.runNumber()));
		bindingContext.bindValue(WidgetProperties.text().observe(isisCycle), BeanProperties.value("value").observe(viewModel.isisCycle()));
		
		messageBox.setModel(viewModel.logMessageSource());		
	}
}

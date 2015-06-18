package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;

public class PeriodsPanel extends Composite {

	private DataBindingContext bindingContext;
	
	private Combo setupSource;
	private Text periodFile;
	private Combo periodType;
	private Text softwarePeriods;
	private Text hardwarePeriods;
	private Text outputDelay;
	private PeriodsTableView periods;
	
	private static final Display DISPLAY = Display.getCurrent();
	
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename"})
	public PeriodsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group composite = new Group(this, SWT.NONE);
		composite.setText("Setup");
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(6, false));
		
		Label lblPeriodSetupSource = new Label(composite, SWT.NONE);
		lblPeriodSetupSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodSetupSource.setSize(107, 15);
		lblPeriodSetupSource.setText("Period setup source:");
		
		setupSource = new Combo(composite, SWT.NONE);
		setupSource.setSize(177, 23);
		setupSource.setItems(PeriodSetupSource.allToString().toArray(new String[0]));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblPeriodFile = new Label(composite, SWT.NONE);
		lblPeriodFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodFile.setSize(58, 15);
		lblPeriodFile.setText("Period File:");
		
		periodFile = new Text(composite, SWT.BORDER);
		GridData gd_periodFile = new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1);
		gd_periodFile.widthHint = 400;
		periodFile.setLayoutData(gd_periodFile);
		periodFile.setSize(412, 25);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filePath = filePathFromDialog(periodFile.getText());
				periodFile.setText(filePath);
			}
		});
		btnNewButton.setSize(59, 25);
		btnNewButton.setText("Browse...");
		
		Label lblPeriodType = new Label(composite, SWT.NONE);
		lblPeriodType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodType.setSize(66, 15);
		lblPeriodType.setText("Period Type:");
		
		periodType = new Combo(composite, SWT.NONE);
		periodType.setSize(177, 23);
		periodType.setItems(PeriodControlType.allToString().toArray(new String[0]));
		new Label(composite, SWT.NONE);
		
		Label lblNumberOfSoftware = new Label(composite, SWT.NONE);
		lblNumberOfSoftware.setSize(91, 15);
		lblNumberOfSoftware.setText("Software periods:");
		
		softwarePeriods = new Text(composite, SWT.BORDER | SWT.RIGHT);
		GridData gd_softwarePeriods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_softwarePeriods.widthHint = 60;
		softwarePeriods.setLayoutData(gd_softwarePeriods);
		softwarePeriods.setSize(76, 23);
		new Label(composite, SWT.NONE);
		
		Group periodsComposite = new Group(this, SWT.NONE);
		periodsComposite.setText("Hardware Periods");
		periodsComposite.setLayout(new GridLayout(1, false));
		periodsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_2 = new Composite(periodsComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(5, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lblHardwarePeriodSequences = new Label(composite_2, SWT.NONE);
		lblHardwarePeriodSequences.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHardwarePeriodSequences.setText("Hardware period sequences:");
		
		hardwarePeriods = new Text(composite_2, SWT.BORDER | SWT.RIGHT);
		GridData gd_hardwarePeriods = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_hardwarePeriods.widthHint = 80;
		hardwarePeriods.setLayoutData(gd_hardwarePeriods);
		new Label(composite_2, SWT.NONE);
		
		Label lblOutputDelayus = new Label(composite_2, SWT.NONE);
		lblOutputDelayus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOutputDelayus.setText("Output delay (μs):");
		
		outputDelay = new Text(composite_2, SWT.BORDER | SWT.RIGHT);
		GridData gd_outputDelay = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_outputDelay.widthHint = 80;
		outputDelay.setLayoutData(gd_outputDelay);
		
		periods = new PeriodsTableView(periodsComposite, SWT.NONE);
		periods.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public void setModel(final PeriodsViewModel viewModel) {
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(setupSource), BeanProperties.value("setupSource").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(periodFile), BeanProperties.value("periodFile").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(periodType), BeanProperties.value("periodType").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(softwarePeriods), BeanProperties.value("softwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(hardwarePeriods), BeanProperties.value("hardwarePeriods").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(outputDelay), BeanProperties.value("outputDelay").observe(viewModel));

		setPeriods(viewModel.periods());
		viewModel.addPropertyChangeListener("periods", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setPeriods(viewModel.periods());
			}
		});
	}
	
	private void setPeriods(final List<Period> newPeriods) {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				periods.setPeriods(newPeriods);
			}
		});
	}
	
	private String filePathFromDialog(String filePath) {
		FileDialog dialog = new FileDialog(this.getShell(), SWT.SINGLE);
		dialog.setFileName(filePath);
		
		return dialog.open();
	}
}

package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummaryViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.RecordLabel;
import uk.ac.stfc.isis.ibex.ui.widgets.RecordSetter;
import uk.ac.stfc.isis.ibex.ui.widgets.styles.RecordSetterStyle;

public class DaeTitlePanel extends Composite {

	private RecordLabel runTitle;
	private RecordSetter newRunTitle;
	
	public DaeTitlePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblRunTitle = new Label(this, SWT.NONE);
		lblRunTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRunTitle.setText("Run title:");
		
		runTitle = new RecordLabel(this, SWT.NONE);
		runTitle.setAlignment(SWT.CENTER);
		runTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runTitle.setText("UNKNOWN");
		
		Label lblNewTitle = new Label(this, SWT.NONE);
		lblNewTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewTitle.setText("New Title:");
		
		newRunTitle = new RecordSetter(this, RecordSetterStyle.FULL);
		newRunTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	public void bind(RunSummaryViewModel viewModel) {
		//runTitle.bindModel(viewModel.title());
		//newRunTitle.setModel(viewModel.titleSetter());
	}
}

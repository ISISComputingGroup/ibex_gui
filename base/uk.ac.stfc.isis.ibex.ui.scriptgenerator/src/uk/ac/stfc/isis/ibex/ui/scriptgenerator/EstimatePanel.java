package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;

public class EstimatePanel extends Composite {
	private Text text;
	public EstimatePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		
		Group grpEstimate = new Group(this, SWT.NONE);
		grpEstimate.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		grpEstimate.setLayout(new GridLayout(2, false));
		
		Label lblCountRate = new Label(grpEstimate, SWT.RIGHT);
		lblCountRate.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblCountRate.setText("Est. count rate:");

		Text txtCountRate = new Text(grpEstimate, SWT.BORDER);
		txtCountRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtCountRate.setText("40");
				
		Label lblTimeBetween = new Label(grpEstimate, SWT.RIGHT);
		lblTimeBetween.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));  
		lblTimeBetween.setText("Est. time between moves:");
		
		Text txtTimeBetween = new Text(grpEstimate, SWT.BORDER);
		txtTimeBetween.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTimeBetween.setText("120");
		
		Label lblScriptTime = new Label(grpEstimate, SWT.RIGHT);
		lblScriptTime.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblScriptTime.setText("Est. script time:");

		Label lblTimeValue = new Label(grpEstimate, SWT.RIGHT);
		lblTimeValue.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));  
		lblTimeValue.setText("00:00:00");
		
		Button btnCalculate = new Button(grpEstimate, SWT.NONE);
		btnCalculate.setText("Calculate");
		btnCalculate.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));
	}
}

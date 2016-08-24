package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

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
		grpEstimate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpEstimate.setText("Estimated Script Time");
		grpEstimate.setLayout(new GridLayout(3, false));
		
		Label lblCountRate = new Label(grpEstimate, SWT.RIGHT);
		lblCountRate.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblCountRate.setText("Est. count rate:");

		Text txtCountRate = new Text(grpEstimate, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblTimeBetween = new Label(grpEstimate, SWT.RIGHT);
		lblTimeBetween.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));  
		lblTimeBetween.setText("Est. time between moves:");
		new Label(grpEstimate, SWT.NONE);
		
		Text txtTimeBetween = new Text(grpEstimate, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblScriptTime = new Label(grpEstimate, SWT.RIGHT);
		lblScriptTime.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblScriptTime.setText("Est. script time:");

		Text txtScriptTime= new Text(grpEstimate, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
}

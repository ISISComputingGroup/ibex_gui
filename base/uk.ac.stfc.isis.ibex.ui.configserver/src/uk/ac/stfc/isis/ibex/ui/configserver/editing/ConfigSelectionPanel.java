package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;

public class ConfigSelectionPanel extends Composite {

	public ConfigSelectionPanel(Composite parent, int style, ConfigSelection selection) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblEditingConfiguration = new Label(this, SWT.NONE);
		lblEditingConfiguration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEditingConfiguration.setText("Configuration to edit:");
		
		Combo configurationSelector = new Combo(this, SWT.NONE);
		GridData gd_configurationSelector = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_configurationSelector.widthHint = 150;
		configurationSelector.setLayoutData(gd_configurationSelector);
	}
}

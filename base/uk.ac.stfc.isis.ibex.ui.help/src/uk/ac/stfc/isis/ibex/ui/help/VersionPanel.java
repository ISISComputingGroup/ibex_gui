package uk.ac.stfc.isis.ibex.ui.help;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.help.Help;

public class VersionPanel extends Composite {

	private Label date;
	private Label version;

	public VersionPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblVersion = new Label(this, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText("Version:");
		
		version = new Label(this, SWT.NONE);
		version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDate = new Label(this, SWT.NONE);
		lblDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDate.setText("Date:");
		
		date = new Label(this, SWT.NONE);
		date.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if (Help.getInstance() != null) {
			bind(Help.getInstance());
		}
	}

	private void bind(Help help) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(version), BeanProperties.value("value").observe(help.revision()));
		bindingContext.bindValue(WidgetProperties.text().observe(date), BeanProperties.value("value").observe(help.date()));		
	}
}

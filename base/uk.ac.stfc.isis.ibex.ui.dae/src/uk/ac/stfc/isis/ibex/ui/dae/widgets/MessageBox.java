package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MessageBox extends Composite {
	private Text text;
	private Label title;
	
	public MessageBox(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		title = new Label(this, SWT.NONE);
		title.setText("Messages:");
		
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void setModel(UpdatedValue<String> model) {
		DataBindingContext bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.text().observe(text), BeanProperties.value("value").observe(model));
	}
	
}

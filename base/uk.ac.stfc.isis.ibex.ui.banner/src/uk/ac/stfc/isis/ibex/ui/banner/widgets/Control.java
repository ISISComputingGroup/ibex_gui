package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;

public class Control extends Composite {

	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private Button btnLoremIpsum;
	
	public Control(Composite parent, int style, ControlModel model, Font font) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		btnLoremIpsum = new Button(this, SWT.NONE);
		btnLoremIpsum.setText("Lorem Ipsum");
		btnLoremIpsum.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		
		if (model != null) {
			btnLoremIpsum.setText(model.text());
			bind(model);
		}
	}

	private void bind(final ControlModel model) {
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnLoremIpsum), BeanProperties.value("value").observe(model.enabled()));
		
		btnLoremIpsum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.click();
			}
		});
	}
}

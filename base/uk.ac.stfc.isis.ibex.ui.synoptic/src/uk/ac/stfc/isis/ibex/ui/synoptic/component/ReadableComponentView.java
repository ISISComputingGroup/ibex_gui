package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;

public class ReadableComponentView extends Composite {
	
	private Label propertyName;
	private StyledText value;
	
	private final DataBindingContext bindingContext = new DataBindingContext();

	public ReadableComponentView(Composite parent, ReadableComponentProperty property) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		propertyName = new Label(this, SWT.NONE);		
		propertyName.setAlignment(SWT.CENTER);
		propertyName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		propertyName.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		value = new StyledText(this, SWT.READ_ONLY);
		value.setDoubleClickEnabled(false);
		value.setEditable(false);
		value.setBackground(SWTResourceManager.getColor(240, 240, 240));
		value.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		value.setAlignment(SWT.RIGHT);
		
		setProperty(property);
	}

	private void setProperty(ReadableComponentProperty property) {
		propertyName.setText(property.displayName());
		bindingContext.bindValue(WidgetProperties.text().observe(value), BeanProperties.value("value").observe(property.value()));
	}
}

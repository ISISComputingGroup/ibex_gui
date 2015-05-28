package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.widgets.models.LabelModel;

public class StatusLabel extends CLabel {
	
	private DataBindingContext bindingContext;
	private LabelModel model;
	
	public StatusLabel(Composite parent, int style) {
		super(parent, style);

		setAlignment(SWT.RIGHT);
		setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));	
		
		this.pack();
	}	
		
	public LabelModel getModel() {
		return model;
	}
	
	public void setModel(LabelModel model) {
		this.model = model;
		bindModel(model);
	}
	
	public void bindModel(LabelModel model) {
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(this), BeanProperties.value("description").observe(model));
		bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("color").observe(model));
		bindingContext.bindValue(WidgetProperties.image().observe(this), BeanProperties.value("image").observe(model));
	}
}

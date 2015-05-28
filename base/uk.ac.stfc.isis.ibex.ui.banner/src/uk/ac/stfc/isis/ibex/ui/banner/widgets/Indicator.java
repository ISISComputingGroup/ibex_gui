package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

public class Indicator extends Composite {

	private final StyledText text;
	
	public Indicator(Composite parent, int style, IndicatorModel model, Font font) {
		super(parent, style);
		setEnabled(false);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new StyledText(this, SWT.READ_ONLY | SWT.SINGLE);
		text.setMargins(0, 1, 0, 2);
		text.setText("Lorem Ipsum");
		text.setAlignment(SWT.CENTER);
		text.setEnabled(false);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		text.setFont(font);
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(IndicatorModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(text), BeanProperties.value("value").observe(model.text()));
		bindingContext.bindValue(WidgetProperties.foreground().observe(text), BeanProperties.value("value").observe(model.color()));
	}
}

package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.RuncontrolState;
import uk.ac.stfc.isis.ibex.ui.banner.models.InstrumentStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.StatusColourConverter;

public class StatusIndicator extends Composite {
	
	private Label statusLabel;
	
	public StatusIndicator(Composite parent, int style, InstrumentStatusViewModel model) {
		super(parent, style);
	    setLayout(new FillLayout(SWT.HORIZONTAL));
		statusLabel = new Label(this, SWT.NONE);
		statusLabel.setText("SHUTDOWN");
	    this.pack();
	    this.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		bind(model);
	}
	
	private void bind(InstrumentStatusViewModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
        final UpdateValueStrategy<Boolean, Color> colourStrategy = new UpdateValueStrategy<>();
        colourStrategy.setConverter(new StatusColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(statusLabel),
                BeanProperties.<DisplayBlock, RuncontrolState>value("runControlRunning").observe(model), null, colourStrategy);
	}

}

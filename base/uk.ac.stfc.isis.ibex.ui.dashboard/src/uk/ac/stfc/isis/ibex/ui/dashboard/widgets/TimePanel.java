package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.dashboard.models.TimePanelModel;

public class TimePanel extends Composite {

	private final StyledText instrumentTime; 
	private final Label runTime;
	private final Label period;
	
	public TimePanel(Composite parent, int style, Font font, TimePanelModel model) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label lblInstTime = new Label(this, SWT.NONE);
		lblInstTime.setFont(font);
		lblInstTime.setText("Inst. Time:");
		
		instrumentTime = new StyledText(this, SWT.FULL_SELECTION | SWT.READ_ONLY);
		instrumentTime.setEnabled(false);
		instrumentTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrumentTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		instrumentTime.setFont(font);
		instrumentTime.setText("DD:HHHH:MM:SS");
		
		Label lblRunTime = new Label(this, SWT.NONE);
		lblRunTime.setFont(font);
		lblRunTime.setText("Run Time:");
		
		runTime = new Label(this, SWT.NONE);
		runTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runTime.setFont(font);
		runTime.setText("x s");
		
		Label lblPeriod = new Label(this, SWT.NONE);
		lblPeriod.setFont(font);
		lblPeriod.setText("Period:");
		
		period = new Label(this, SWT.NONE);
		period.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		period.setFont(font);
		period.setText("x / x");
		
		if (model != null) {
			bind(model);
		}
	}

	private void bind(TimePanelModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(instrumentTime), BeanProperties.value("value").observe(model.instrumentTime()));
		bindingContext.bindValue(WidgetProperties.text().observe(runTime), BeanProperties.value("value").observe(model.runTime()));
		bindingContext.bindValue(WidgetProperties.text().observe(period), BeanProperties.value("value").observe(model.period()));
	}
}

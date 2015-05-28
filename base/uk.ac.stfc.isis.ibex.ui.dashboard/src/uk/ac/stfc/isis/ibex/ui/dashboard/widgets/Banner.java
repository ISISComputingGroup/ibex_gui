package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;

public class Banner extends Composite {

	private final Label bannerText;
	private final Composite details;
	private final Label lblRun;
	private final Label runNumber;
	private final Label lblShutter;
	private final Label shutter;
	
	public Banner(Composite parent, int style, BannerModel model, Font titleFont, Font textFont) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		
		bannerText = new Label(this, SWT.WRAP | SWT.CENTER);
		bannerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		bannerText.setFont(titleFont);
		bannerText.setText("INSTRUMENT   is   RUNNING");
		
		details = new Composite(this, SWT.NONE);
		details.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		details.setLayout(new GridLayout(4, false));
		details.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		lblRun = new Label(details, SWT.NONE);
		lblRun.setFont(textFont);
		lblRun.setText("Run:");
		
		runNumber = new Label(details, SWT.NONE);
		runNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		runNumber.setFont(textFont);
		runNumber.setText("0000001");
		
		lblShutter = new Label(details, SWT.NONE);
		lblShutter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblShutter.setFont(textFont);
		lblShutter.setText("Shutter:");
		
		shutter = new Label(details, SWT.NONE);
		GridData gd_shutter = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_shutter.widthHint = 100;
		gd_shutter.minimumWidth = 100;
		shutter.setLayoutData(gd_shutter);
		shutter.setFont(textFont);
		shutter.setText("UNKNOWN");
		
		if (model != null) {
			bind(model);
		}
	}
	
	public void bind(BannerModel model) {		
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(bannerText), BeanProperties.value("value").observe(model.bannerText()));
		bindingContext.bindValue(WidgetProperties.background().observe(this), BeanProperties.value("value").observe(model.background()));
		bindingContext.bindValue(WidgetProperties.text().observe(runNumber), BeanProperties.value("value").observe(model.runNumber()));
		bindingContext.bindValue(WidgetProperties.text().observe(shutter), BeanProperties.value("value").observe(model.shutter()));
	}
}

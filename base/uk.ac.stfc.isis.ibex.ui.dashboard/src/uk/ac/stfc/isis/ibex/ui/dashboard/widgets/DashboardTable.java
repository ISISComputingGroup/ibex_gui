package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.dashboard.DashboardPv;
import uk.ac.stfc.isis.ibex.ui.dashboard.models.BannerModel;

public class DashboardTable extends Composite {
	
	private final BannerModel model;
	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private static final List<DashboardPv> PVS_IN_TABLE = List.of(
			DashboardPv.TABLE_1_1, 
			DashboardPv.TABLE_1_2, 
			DashboardPv.TABLE_2_1, 
			DashboardPv.TABLE_2_2, 
			DashboardPv.TABLE_3_1, 
			DashboardPv.TABLE_3_2);
	
	public DashboardTable(Composite parent, BannerModel model, Font textFont) {
		super(parent, SWT.NONE);
		this.model = model;
		
		this.setLayout(new GridLayout(2, true));
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		for (DashboardPv pv : PVS_IN_TABLE) {
			singleTableItem(this, pv, textFont);
		}
	}
	
	private void singleTableItem(Composite parent, DashboardPv pv, Font textFont) {
		Composite itemContainer = new Composite(parent, SWT.NONE);
		itemContainer.setLayout(new GridLayout(2, true));
		itemContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label label = new Label(itemContainer, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		label.setFont(textFont);
		label.setText("");
		
		Label value = new Label(itemContainer, SWT.NONE);
		value.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		value.setFont(textFont);
		value.setText("");
		
		bindingContext.bindValue(WidgetProperties.text().observe(label), 
				BeanProperties.value("value").observe(model.dashboardLabel(pv)));
		bindingContext.bindValue(WidgetProperties.text().observe(value), 
				BeanProperties.value("value").observe(model.dashboardValue(pv)));
	}

}

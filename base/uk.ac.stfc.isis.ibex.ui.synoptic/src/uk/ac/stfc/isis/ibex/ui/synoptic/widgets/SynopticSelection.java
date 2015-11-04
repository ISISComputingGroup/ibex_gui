package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Contains the widget showing the synoptic drop down menu for switching, refresh button, and bread crumb trail.
 */
public class SynopticSelection extends Composite {
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	
	// The synoptic drop down menu selector
	private Combo synopticCombo;
	
	public SynopticSelection(Composite parent, int style, final SynopticSelectionViewModel model) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginRight = -2;
		gridLayout.marginLeft = 0;
		gridLayout.marginTop = -6;
		gridLayout.marginBottom = -6;

		setLayout(gridLayout);
		
		GridData gd_gotoLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_gotoLabel.verticalAlignment = SWT.CENTER;
		
		GridData gd_synopticCombo = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_synopticCombo.widthHint = 120;
		synopticCombo = new Combo(this, SWT.READ_ONLY);
		synopticCombo.setLayoutData(gd_synopticCombo);
		
		Button refreshButton = new Button(this, SWT.NONE);
		refreshButton.setText("Refresh Synoptic");
		refreshButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		refreshButton.setBackground(BACKGROUND);
		
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				model.refreshSynoptic();
			}
		});
		
		bind(model);
	}
	
	private void bind(SynopticSelectionViewModel model) {
		DataBindingContext bindingContext = new DataBindingContext();
		
		bindingContext.bindList(SWTObservables.observeItems(synopticCombo), BeanProperties.list("synopticList").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(synopticCombo), BeanProperties.value("selected").observe(model));
	}

}

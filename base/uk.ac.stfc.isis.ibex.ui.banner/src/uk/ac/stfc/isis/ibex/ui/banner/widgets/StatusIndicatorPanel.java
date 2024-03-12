package uk.ac.stfc.isis.ibex.ui.banner.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatus;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusColourConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusTextConverter;
import uk.ac.stfc.isis.ibex.ui.banner.views.StatusDetailsDialog;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/** 
 * A panel displaying the overall status of the IBEX server.
 */
public class StatusIndicatorPanel extends Composite {
	
	private StatusDetailsDialog detailsDialog;
	private static final int NUM_COLS = 3;
	private static final int STATUS_LABEL_W = 100;
	private static final int STATUS_LABEL_H = 25;
	
	/**
	 * The constructor. 
	 * 
	 * @param parent The parent composite
	 * @param style  The SWT style
	 * @param model  The view model providing the status information
	 */
	public StatusIndicatorPanel(Composite parent, int style, ServerStatusViewModel model) {
		super(parent, style);
	    setLayout(new GridLayout(NUM_COLS, false));
	    Label serverLabel = new Label(this, SWT.NONE);
	    serverLabel.setText("Server Status is: ");
	    serverLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
	    
	    Composite overallStatusPanel = new Composite(this, SWT.NONE);
	    overallStatusPanel.setLayout(new GridLayout());
		GridData gdOverallStatusPanel = new GridData();
		gdOverallStatusPanel.widthHint = STATUS_LABEL_W;
		gdOverallStatusPanel.heightHint = STATUS_LABEL_H;
		overallStatusPanel.setLayoutData(gdOverallStatusPanel);
		
		Label overallStatusLabel = new Label(overallStatusPanel, SWT.NONE);
		overallStatusLabel.setText("");
		overallStatusLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		Button showDetails = new IBEXButtonBuilder(this, SWT.NONE).text("Details").customLayoutData(IBEXButtonBuilder.centerGrid).listener(e -> showDetailsDialog()).build();
		
	    this.pack();
	    
	    detailsDialog = new StatusDetailsDialog(getShell(), model);

		DataBindingContext bindingContext = new DataBindingContext();
        final UpdateValueStrategy<ServerStatus, Color> colourStrategy = new UpdateValueStrategy<>();
        colourStrategy.setConverter(new ServerStatusColourConverter());
        final UpdateValueStrategy<ServerStatus, String> textStrategy = new UpdateValueStrategy<>();
        textStrategy.setConverter(new ServerStatusTextConverter());

		bindingContext.bindValue(WidgetProperties.background().observe(overallStatusPanel),
				BeanProperties.<ServerStatusViewModel, ServerStatus>value("overallStatus").observe(model), null,
				colourStrategy);
		bindingContext.bindValue(WidgetProperties.text().observe(overallStatusLabel),
				BeanProperties.<ServerStatusViewModel, ServerStatus>value("overallStatus").observe(model), null,
				textStrategy);
	}
	
	private void showDetailsDialog() {
		detailsDialog.open();
	}
	

}

package uk.ac.stfc.isis.ibex.ui.banner.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.banner.models.InstrumentStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.StatusColourConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.StatusTextConverter;


public class StatusDetailsDialog extends TitleAreaDialog {
    
    private InstrumentStatusViewModel viewModel;
    private DataBindingContext bindingContext;
    private final UpdateValueStrategy<Boolean, Color> colourStrategy = new UpdateValueStrategy<>();
    private final UpdateValueStrategy<Boolean, String> textStrategy = new UpdateValueStrategy<>();

	public StatusDetailsDialog(Shell parentShell, InstrumentStatusViewModel viewModel) {
		super(parentShell);	
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.viewModel = viewModel;
		
		bindingContext = new DataBindingContext();
        colourStrategy.setConverter(new StatusColourConverter());
        textStrategy.setConverter(new StatusTextConverter());
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("IBEX Server Status Details");
		
		Composite container = (Composite) super.createDialogArea(parent);
		Composite detailsPanel = new Composite(container, SWT.NONE);
		detailsPanel.setLayout(new GridLayout(2, true));
		detailsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		addIndicator(detailsPanel, "runControlRunning", "Run Control");
		addIndicator(detailsPanel, "blockServerRunning", "Block Server");
		addIndicator(detailsPanel, "blockGatewayRunning", "Block Gateway");
		addIndicator(detailsPanel, "isisDaeRunning", "DAE");
		addIndicator(detailsPanel, "instetcRunning", "INSTETC");
		addIndicator(detailsPanel, "dbServerRunning", "Database Server");
		addIndicator(detailsPanel, "psControlRunning", "Procserv Control IOC");
		addIndicator(detailsPanel, "arAccessRunning", "Archiver Access");
		addIndicator(detailsPanel, "alarmServerRunning", "Alarm Server");
		return container;
	}	

	private void addIndicator(Composite parent, String propertyName, String displayName) {

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText(displayName + " status is: ");
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
		Label lblStatus = new Label(parent, SWT.NONE);
		lblStatus.setText("");
		lblStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		bindingContext.bindValue(WidgetProperties.background().observe(lblStatus),
				BeanProperties.<InstrumentStatusViewModel, Boolean>value(propertyName).observe(this.viewModel),
				null, colourStrategy);
		bindingContext.bindValue(WidgetProperties.text().observe(lblStatus),
				BeanProperties.<InstrumentStatusViewModel, Boolean>value(propertyName).observe(this.viewModel),
				null, textStrategy);
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID) {
			return null;
		}
		return super.createButton(parent, id, label, defaultButton);
	}
}

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

import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatus;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusColourConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusTextConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusViewModel;

/**
 * A dialog box displaying the status of various services in the IBEX Server.
 */
public class StatusDetailsDialog extends TitleAreaDialog {
    
    private ServerStatusViewModel viewModel;
    private DataBindingContext bindingContext;
    private final UpdateValueStrategy<ServerStatus, Color> colourStrategy = new UpdateValueStrategy<>();
    private final UpdateValueStrategy<ServerStatus, String> textStrategy = new UpdateValueStrategy<>();

	/**
	 * The constructor.
	 * 
	 * @param parentShell The shell to open the dialog from
	 * @param viewModel   The viewmodel providing the status information
	 */
	public StatusDetailsDialog(Shell parentShell, ServerStatusViewModel viewModel) {
		super(parentShell);	
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.viewModel = viewModel;
		
		bindingContext = new DataBindingContext();
        colourStrategy.setConverter(new ServerStatusColourConverter());
        textStrategy.setConverter(new ServerStatusTextConverter());
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("IBEX Server Status Details");
		
		Composite container = (Composite) super.createDialogArea(parent);
		Composite detailsPanel = new Composite(container, SWT.NONE);
		detailsPanel.setLayout(new GridLayout(2, true));
		detailsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		

		addIndicator(detailsPanel, "runControlStatus", "Run Control");
		addIndicator(detailsPanel, "blockServerStatus", "Block Server");
		addIndicator(detailsPanel, "blockGatewayStatus", "Block Gateway");
		addIndicator(detailsPanel, "isisDaeStatus", "DAE");
		addIndicator(detailsPanel, "instetcStatus", "INSTETC");
		addIndicator(detailsPanel, "dbServerStatus", "Database Server");
		addIndicator(detailsPanel, "psControlStatus", "Procserv Control IOC");
		addIndicator(detailsPanel, "alarmServerStatus", "Alarm Server");

		new Label(detailsPanel, SWT.NONE);  // spacer
		
		Label message = new Label(detailsPanel, SWT.NONE);
		message.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		message.setText(
				"Note: Services may have issues even if they are running (more info in IOC logs). \n"
				+ "Contact experiment controls support if you are experiencing server issues.");
		
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
				BeanProperties.<ServerStatusViewModel, ServerStatus>value(propertyName).observe(this.viewModel),
				null, colourStrategy);
		bindingContext.bindValue(WidgetProperties.text().observe(lblStatus),
				BeanProperties.<ServerStatusViewModel, ServerStatus>value(propertyName).observe(this.viewModel),
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

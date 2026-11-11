package uk.ac.stfc.isis.ibex.ui.banner.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatus;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusColourConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusTextConverter;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusViewModel;

/**
 * A dialog box displaying the status of various services in the IBEX Server.
 */
public class StatusDetailsDialog extends TitleAreaDialog {
    
    private static final int CONFIRM_DIALOG_WIDTH = 600;
    
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
		detailsPanel.setLayout(new GridLayout(3, false));
		detailsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		

		addIndicator(detailsPanel, "runControlStatus", "Run Control", "RUNCTRL_01");
		addIndicator(detailsPanel, "blockServerStatus", "Block Server", "BLOCKSVR");
		addIndicator(detailsPanel, "blockGatewayStatus", "Block Gateway", "GWBLOCK");
		addIndicator(detailsPanel, "isisDaeStatus", "ISISDAE IOC", "ISISDAE_01");
		addIndicator(detailsPanel, "instetcStatus", "INSTETC", "INSTETC_01");
		addIndicator(detailsPanel, "dbServerStatus", "Database Server", "DBSVR");
		addIndicator(detailsPanel, "alarmServerStatus", "Alarm Server", "ALARM");
		addIndicator(detailsPanel, "archiveInstStatus", "Instrument Archive", "ARINST");
		addIndicator(detailsPanel, "archiveBlockserverStatus", "Block Archive", "ARBLOCK");
		addIndicator(detailsPanel, "psControlStatus", "Procserv Control IOC", null);

		Label spacer = new Label(detailsPanel, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label message = new Label(detailsPanel, SWT.NONE);
		message.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		message.setText(
				"Note: Services may have issues even if they are running (more info in IOC logs). \n"
				+ "Contact experiment controls support if you are experiencing server issues.");
		
		return container;
	}

	private void addIndicator(Composite parent, String propertyName, String displayName, String iocName) {

		PreferenceSupplier preferences = new PreferenceSupplier();
		
		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText(displayName + " status is: ");
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		Label lblStatus = new Label(parent, SWT.NONE);
		lblStatus.setText("");
		GridData statusLayout = new GridData(SWT.LEFT, SWT.CENTER, false, true);
		statusLayout.widthHint = 80;
		lblStatus.setLayoutData(statusLayout);

		if (iocName == null) {
			new Label(parent, SWT.NONE);
		} else {
			Button restartButton = new Button(parent, SWT.PUSH);
			restartButton.setText("Restart");
			restartButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			restartButton.addListener(SWT.Selection, e -> restartIoc(iocName, displayName, preferences.restartWarning(iocName)));
			bindingContext.bindValue(WidgetProperties.enabled().observe(restartButton),
					BeanProperties.<ServerStatusViewModel, Boolean>value("restartAvailable").observe(this.viewModel));
		}

		bindingContext.bindValue(WidgetProperties.background().observe(lblStatus),
				BeanProperties.<ServerStatusViewModel, ServerStatus>value(propertyName).observe(this.viewModel),
				null, colourStrategy);
		bindingContext.bindValue(WidgetProperties.text().observe(lblStatus),
				BeanProperties.<ServerStatusViewModel, ServerStatus>value(propertyName).observe(this.viewModel),
				null, textStrategy);
	}

	/**
	 * Confirms and requests a restart of the named protected server IOC via the block server.
	 * 
	 * @param iocName     The procServ name of the IOC to restart
	 * @param displayName The display name of the service (for messages)
	 */

	private void restartIoc(String iocName, String displayName, String comment) {
		MessageDialog confirmDialog = new MessageDialog(
			    getShell(), 
			    "Restart " + displayName, 
			    null, 
			    "Are you sure you want to restart " + displayName + "?", 
			    MessageDialog.CONFIRM, 
			    new String[] { 
			        IDialogConstants.CANCEL_LABEL, 
			        IDialogConstants.OK_LABEL 
			    },
			    0
			) {
		    @Override
		    protected boolean isResizable() {
		        return true;
		    }
		    
		    @Override
		    protected Point getInitialSize() {
		        // Lay out at a fixed width so the warning wraps onto multiple lines
		        return getShell().computeSize(CONFIRM_DIALOG_WIDTH, SWT.DEFAULT, true);
		    }
		    
		    @Override
		    protected Control createCustomArea(Composite parent) {
		        Label warning = new Label(parent, SWT.WRAP);
		        warning.setText("WARNING: " + comment + " This action requires manager mode.");
		        warning.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		        warning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		        return warning;
		    }
		};
		boolean confirmed = confirmDialog.open() == 1;
		if (!confirmed) {
			return;
		}
		try {
			viewModel.restart(iocName);
		} catch (Exception e) {
			MessageDialog.openError(getShell(), "Restart failed",
					"Failed to request restart of " + displayName + ":\n" + e);
		}
	}

	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID) {
			return null;
		}
		return super.createButton(parent, id, label, defaultButton);
	}
}

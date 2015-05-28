package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.log.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplay;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplayModel;

public class LogMessageBox extends Composite {
	private Label title;
	private LogDisplay logDisplay;
	
	public LogMessageBox(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		title = new Label(this, SWT.NONE);
		title.setText("Messages:");
				
		logDisplay = new LogDisplay(this, null);
		logDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// TODO: get the name of the DAE IOC so that only messages coming from it are shown
		String daeName = "ISISDAE_01";
		
		LogMessageFilter filter = new LogMessageFilter(LogMessageFields.CLIENT_NAME, daeName, false);
		
		logDisplay.addMessageFilter(filter);
	}
	
	public void setModel(ILogMessageProducer model) {
		// Create model and subscribe to updates from Log
		LogDisplayModel logDisplayModel = new LogDisplayModel(model);
		logDisplay.setModel(logDisplayModel);
	}
}
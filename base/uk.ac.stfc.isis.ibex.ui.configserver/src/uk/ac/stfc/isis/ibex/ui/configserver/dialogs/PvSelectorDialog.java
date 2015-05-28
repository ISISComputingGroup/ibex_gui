package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs.PVSelectorPanel;


/**
 * A dialog for selecting a PV
 *
 */
public class PvSelectorDialog extends TitleAreaDialog{

	private PVSelectorPanel selector;
	private EditableConfiguration config;
	private PV pv;
	
	public PvSelectorDialog(Shell parentShell, EditableConfiguration config, String address) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
		this.config = config;
		pv = new PV(address, "", "", "");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		selector = new PVSelectorPanel(parent, SWT.NONE);
		selector.setConfig(config, pv);
		selector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
		setTitle("PV Selector");
		
		return selector;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, 
				IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	public String getPVAddress() {
		return pv.getAddress();
	}

}

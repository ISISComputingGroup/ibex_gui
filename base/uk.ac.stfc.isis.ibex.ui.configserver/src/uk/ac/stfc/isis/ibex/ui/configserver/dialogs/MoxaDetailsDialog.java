package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.ui.moxas.views.MoxaInfoPanel;
import uk.ac.stfc.isis.ibex.ui.moxas.views.MoxasViewModel;

/*
 * Dialogue for showing Moxa port mappings.
 */
public class MoxaDetailsDialog extends TitleAreaDialog {
	
	Configurations control;
	
	private static final String MAIN_TITLE = "View port mappings";
	private static final String SUB_TITLE = "View Moxa to COM port mappings";
	
	private static final Point INITIAL_SIZE = new Point(800, 600);
	
	/*
	 * Dialog constructor.
	 */
	public MoxaDetailsDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(MAIN_TITLE);
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
	    setBlockOnOpen(false);
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(SUB_TITLE);
		
		parent.setLayout(new GridLayout(1, true));
		control = Configurations.getInstance();
		MoxasViewModel moxasViewModel = new MoxasViewModel(control);
		MoxaInfoPanel moxaView = new MoxaInfoPanel(parent, SWT.FILL, moxasViewModel);
		moxaView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return moxaView;
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
	

}

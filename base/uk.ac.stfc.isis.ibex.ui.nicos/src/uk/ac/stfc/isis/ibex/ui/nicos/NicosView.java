package uk.ac.stfc.isis.ibex.ui.nicos;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.CreateScriptDialog;

@SuppressWarnings("checkstyle:magicnumber")
/**
 * The main view for the NICOS scripting perspective
 */
public class NicosView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.nicosview";
	
	private final Shell shell;
	
	public NicosView() {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout glParent = new GridLayout(3, false);
		glParent.marginRight = 10;
		glParent.marginHeight = 10;
		glParent.marginWidth = 10;
		parent.setLayout(glParent);
		
		Label lblCurrentScript = new Label(parent, SWT.NONE);
		lblCurrentScript.setText("Current Script");
		new Label(parent, SWT.NONE);
		
		Label lblOutput = new Label(parent, SWT.NONE);
		lblOutput.setText("Output");
		
		StyledText txtCurrentScript = new StyledText(parent, SWT.BORDER);
		txtCurrentScript.setEditable(false);
		txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblSpacer = new Label(parent, SWT.NONE);
		GridData gdSpacer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpacer.widthHint = 200;
		gdSpacer.minimumWidth = 200;
		lblSpacer.setLayoutData(gdSpacer);
		
		StyledText txtOutput = new StyledText(parent, SWT.BORDER);
		txtOutput.setEditable(false);
		txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnCreateScript = new Button(parent, SWT.NONE);
		btnCreateScript.setText("Create Script");
		
		btnCreateScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateScriptDialog dialog = new CreateScriptDialog(shell);
				dialog.open();
			}
		});
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

	}

	@Override
	public void setFocus() {
		
	}

}

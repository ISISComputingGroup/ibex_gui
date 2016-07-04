package uk.ac.stfc.isis.ibex.ui.nicos;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.CreateScriptDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class NicosView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.nicosview";
	
	private final Shell shell;
	
	public NicosView() {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(3, false);
		gl_parent.marginRight = 10;
		gl_parent.marginHeight = 10;
		gl_parent.marginWidth = 10;
		parent.setLayout(gl_parent);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Current Script");
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("Output");
		
		StyledText styledText_1 = new StyledText(parent, SWT.BORDER);
		styledText_1.setEditable(false);
		styledText_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblNewLabel_2 = new Label(parent, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.widthHint = 200;
		gd_lblNewLabel_2.minimumWidth = 200;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		
		StyledText styledText = new StyledText(parent, SWT.BORDER);
		styledText.setEditable(false);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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

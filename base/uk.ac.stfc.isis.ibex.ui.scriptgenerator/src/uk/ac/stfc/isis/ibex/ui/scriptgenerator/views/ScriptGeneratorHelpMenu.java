package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.net.URL;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ScriptGeneratorHelpMenu {
	
	Composite helpMenuComposite;
	
	public ScriptGeneratorHelpMenu(Composite containingComposite, Optional<URL> manualUrl) {
		setUpHelpMenuComposite(containingComposite);
		createLabel();
		createDropDownButton(manualUrl);
	}
	
	private void setUpHelpMenuComposite(Composite containingComposite) {
    	helpMenuComposite = new Composite(containingComposite, SWT.BORDER);
    	helpMenuComposite.setLayout(new GridLayout(2, false));
    	helpMenuComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 0, 1));
	}
	
	private void createLabel() {
		CLabel lbl = new CLabel(helpMenuComposite, SWT.NONE);
        lbl.setText("Help");
	}
	
	private void createDropDownButton(Optional<URL> manualUrl) {
		Button btn = new Button(helpMenuComposite, SWT.ARROW|SWT.DOWN);
        btn.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        btn.addSelectionListener(new ScriptGeneratorHelpMenuItems(manualUrl));
	}

}

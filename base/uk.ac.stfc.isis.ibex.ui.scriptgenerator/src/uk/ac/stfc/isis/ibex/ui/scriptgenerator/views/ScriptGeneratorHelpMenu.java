package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * A menu containing help items for the script generator including a button to open the manual and 
 * to display information about the script generator.
 */
public class ScriptGeneratorHelpMenu {
	
	Composite helpMenuComposite;
	Path scriptDefinitionsLocation;
	
	/**
	 * Creates a help menu.
	 * @param containingComposite The containing composite
	 */
	public ScriptGeneratorHelpMenu(Composite containingComposite, Path scriptDefinitionsLocation) {
		setUpHelpMenuComposite(containingComposite);
		createLabel();
		createDropDownButton();
		this.scriptDefinitionsLocation = scriptDefinitionsLocation;
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
	
	private void createDropDownButton() {
		Button btn = new Button(helpMenuComposite, SWT.ARROW | SWT.DOWN);
        btn.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        btn.addSelectionListener(new ScriptGeneratorHelpMenuItems(scriptDefinitionsLocation));
	}

}

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

public class ScriptGeneratorAboutDialogBox extends AboutDialogBox {
	
	private Path scriptDefinitionsLocation;

	public ScriptGeneratorAboutDialogBox(Shell parentShell, Path scriptDefinitionsLocation) {
		super(parentShell, "Script Generator");
		this.scriptDefinitionsLocation = scriptDefinitionsLocation;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("About Script Generator");
		Composite container = super.superCreateDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		new ScriptGeneratorVersionPanel(container, SWT.NONE, this.scriptDefinitionsLocation);
		return container;
	}

}

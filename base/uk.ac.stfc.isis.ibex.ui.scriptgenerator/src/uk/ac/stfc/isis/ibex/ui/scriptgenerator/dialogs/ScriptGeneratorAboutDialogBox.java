package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

/**
 * A dialog box to display to the user that contains information about the script generator.
 */
public class ScriptGeneratorAboutDialogBox extends AboutDialogBox {
	
	private Optional<Path> scriptDefinitionsLocation = Optional.empty();

	/**
	 * Create a new dialog box to display information about the script definitions.
	 * @param parentShell The shell to contain the dialog box.
	 * @param scriptDefinitionsLocation Optionally the location of the script definitions.
	 */
	public ScriptGeneratorAboutDialogBox(Shell parentShell, Optional<Path> scriptDefinitionsLocation) {
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

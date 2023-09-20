package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

/**
 * Handler for button which shows the percentage of console buffer in use and clears the console on click.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConsoleHelpButton extends WorkbenchWindowControlContribution {
	private Button button;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.NONE); // This needs to be a HelpButton instance, which needs to be modified to inherit from Button. Then we can return it as a Control.
		return button;
	}
}
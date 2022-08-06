package uk.ac.stfc.isis.ibex.ui.scripting;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import uk.ac.stfc.isis.ibex.model.UIThreadUtils;

/**
 * Handler for label which shows the number of active consoles.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConsolesCountMonitor extends WorkbenchWindowControlContribution {

	private static final String FORMAT = "Active consoles: %d";
	private static final int MAX_LENGTH = String.format(FORMAT, 100).length();
	private static final String TOOLTIP_TEXT = "The number of currently active consoles";

	private Label label;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setToolTipText(TOOLTIP_TEXT);
		label.setText("Active consoles: NaN");
		updateLabelText();

		// Subscribe to the numbers of active consoles changing
		Consoles.getDefault().addConsoleCountListener(this::updateLabelText);

		// When this widget gets disposed, remove the listener
		label.addDisposeListener(e_ignored -> Consoles.getDefault().removeConsoleCountListener(this::updateLabelText));

		return label;
	}

	private void updateLabelText() {
		long consolesNumber;
		try {
			consolesNumber = ConsolePlugin.getDefault().getConsoleManager().getConsoles().length;
		} catch (RuntimeException e) {
			consolesNumber = 0;
		}
				
		final String text = StringUtils.rightPad(String.format(FORMAT, consolesNumber), MAX_LENGTH);
		UIThreadUtils.asyncExec(() -> label.setText(text));
		
		// Display count only if there are multiple active consoles
		boolean visible = !(consolesNumber == 0 || consolesNumber == 1);
		UIThreadUtils.asyncExec(() -> label.setVisible(visible));
	}
}

package uk.ac.stfc.isis.ibex.ui.scripting;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;

/**
 * Handler for button which shows the percentage of console buffer in use and clears the console on click.
 */
public class ConsoleLengthMonitor extends WorkbenchWindowControlContribution {

	private static final String FORMAT = "Buffer %.0f%% full. Click to clear console now.";
	private static final int MAX_LENGTH = String.format(FORMAT, 100.0).length();

	private static final String TOOLTIP_TEXT = "The scripting console can only hold a certain amount of output.\n\n"
			+ "When the buffer is full, the console will be automatically cleared. The console can also be cleared\n"
			+ "manually by clicking this button or using the right-click context menu.\n\n"
			+ "If desired, the present console output can also be saved via the console's right click context menu for "
			+ "future reference.";

	private Button button;

	private String getText() {
		double percentageFull;
		try {
			percentageFull = 100.0 * ScriptConsole.getActiveScriptConsole().getDocument().getLength() / Consoles.MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE;
		} catch (RuntimeException e) {
			percentageFull = Double.NaN;
		}

		return StringUtils.rightPad(String.format(FORMAT, percentageFull), MAX_LENGTH);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.NONE);
		button.setToolTipText(TOOLTIP_TEXT);
		button.setText(getText());

		// Subscribe to mouse click events & clear console on click.
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				ScriptConsole.getActiveScriptConsole().clearConsole();
			}
		});

		// Subscribe to the length of the console output changing
		Consoles.getDefault().addConsoleLengthListener(this::updateButtonText);

		// When this widget gets disposed, remove the listener
		button.addDisposeListener(e_ignored -> Consoles.getDefault().removeConsoleLengthListener(this::updateButtonText));

		return button;
	}

	private void updateButtonText() {
		final String text = getText();
		Display.getCurrent().asyncExec(() -> button.setText(text));
	}
}

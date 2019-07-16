package uk.ac.stfc.isis.ibex.ui.scripting;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.python.pydev.shared_interactive_console.console.ui.ScriptConsole;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Handler for button which shows the percentage of console buffer in use and clears the console on click.
 */
public class ConsoleLengthHandler extends AbstractHandler implements IElementUpdater {

	private static final String FORMAT = "Buffer %.0f%% full. Click to clear console now.";
	private static final int MAX_LENGTH = String.format(FORMAT, 100.0).length();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return Consoles.getDefault().anyActive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ScriptConsole.getActiveScriptConsole().clearConsole();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateElement(final UIElement element, final @SuppressWarnings("rawtypes") Map map) {
		double percentageFull;
		try {
			percentageFull = 100.0 * ScriptConsole.getActiveScriptConsole().getDocument().getLength() / Consoles.MAXIMUM_CHARACTERS_TO_KEEP_PER_CONSOLE;
		} catch (RuntimeException e) {
			IsisLog.getLogger(getClass()).error(e);
			percentageFull = Double.NaN;
		}

		final String finalText = StringUtils.rightPad(String.format(FORMAT, percentageFull), MAX_LENGTH);
		final HandlerEvent handlerEvent = new HandlerEvent(this, true, true);

		Display.getDefault().asyncExec(() -> {
			element.setText(finalText);
			fireHandlerChanged(handlerEvent);
			IsisLog.getLogger(getClass()).info("set text to " + finalText);
		});
	}

}

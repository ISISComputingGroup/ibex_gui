package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * An action that deals with file/script interactions. E.g. saving and loading.
 */
public abstract class ScriptFileInteractor {
	private final Shell shell;
	private final String action;
	private final int fileStyle;
	
	/**
	 * The script.
	 */
	protected final QueuedScript script;

	/**
	 * The system line separator.
	 */
	protected static final String LINE_SEP = System.lineSeparator();
	private static final String DEFAULT_SCRIPT_DIRECTORY = "c:\\scripts";
	private static final String[] ALLOWED_SCRIPT_NAMES = {"Python Scripts (*.py)"};
	private static final String[] ALLOWED_SCRIPT_EXTENSIONS = {"*.py"};

	/**
	 * Create the manipulator.
	 * 
	 * @param shell
	 *            The shell to produce dialog boxes from.
	 * @param action
	 *            The name of the action being performed.
	 * @param fileStyle
	 *            The style of file dialog to open.
	 * @param script
	 *            The script to act on.
	 */
	protected ScriptFileInteractor(Shell shell, String action, int fileStyle, QueuedScript script) {
		this.shell = shell;
		this.action = action;
		this.fileStyle = fileStyle;
		this.script = script;
	}

	private String getScriptPath() {
		FileDialog dialog = new FileDialog(shell, fileStyle);
		dialog.setFilterExtensions(ALLOWED_SCRIPT_EXTENSIONS);
		dialog.setFilterNames(ALLOWED_SCRIPT_NAMES);
		dialog.setFilterPath(DEFAULT_SCRIPT_DIRECTORY);
		dialog.setOverwrite(fileStyle == SWT.SAVE);
		dialog.setFileName(script.getName() + ".py");
		return dialog.open();
	}

	private void errorDialog(String scriptPath) {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
		dialog.setMessage(action + " failed on path " + scriptPath);
		dialog.setText(action + " Script Failed");
		dialog.open();
	}

	/**
	 * Executes the action.
	 */
	public void execute() {
		String scriptPath = getScriptPath();
		if (!Strings.isNullOrEmpty(scriptPath)) {
			try {
				manipulateFile(scriptPath);
			} catch (IOException error) {
				errorDialog(scriptPath);
			}
		}
	}

	/**
	 * Perform the file interaction.
	 * 
	 * @param path
	 *            The path to the file.
	 * @throws IOException
	 *             Thrown if the file interaction fails.
	 */
	protected abstract void manipulateFile(String path) throws IOException;
}
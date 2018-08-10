package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * Loads a script from a file.
 */
public class LoadScriptAction extends ScriptFileInteractor {
	/**
	 * Constructor for the loading action.
	 * 
	 * @param shell
	 *            The shell to open any dialogs in.
	 * @param script
	 *            The script to load.
	 */
	public LoadScriptAction(Shell shell, QueuedScript script) {
		super(shell, "Load", SWT.OPEN, script);
	}

	@Override
	protected void manipulateFile(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path));
		String contents = lines.stream().collect(Collectors.joining(LINE_SEP));
		script.setCode(contents);
	}
}

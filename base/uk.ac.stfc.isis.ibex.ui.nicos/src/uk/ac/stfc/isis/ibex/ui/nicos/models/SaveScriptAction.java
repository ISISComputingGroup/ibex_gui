package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * Saves a script to file.
 */
public class SaveScriptAction extends ScriptFileInteractor {	
	/**
	 * Constructor for the saving action.
	 * @param shell The shell to open any dialogs in.
	 * @param script The script to save.
	 */
	public SaveScriptAction(Shell shell, QueuedScript script) {
		super(shell, "Save", SWT.SAVE, script);
	}

	@Override
	protected void manipulateFile(String path) throws IOException {
		List<String> lines = Arrays.asList(script.getCode().split(LINE_SEP));
		Files.write(Paths.get(path), lines);
	}

}

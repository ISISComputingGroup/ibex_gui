package uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.OpenFileException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;


/**
 * A message dialog box for saving a generated script.
 */
public class SaveScriptGeneratorFileMessageDialog {
	
	private FileDialog saveDialog;
	
	private Shell parentShell;

	/**
	 * The generated script to write to file.
	 */
	private String generatedScript;
	
	/**
	 * The script generator model.
	 */
	private ScriptGeneratorSingleton model;
	
	/**
	 * Create the dialog box.
	 * 
	 * @param parentShell The shell that the dialog sits in.
	 * @param dialogTitle The title of the dialog box.
	 * @param defaultFilepath The default save directory
	 * @param defaultFilename The default file name to save the script files with.
	 * @param generatedScript The generated script to write to file.
	 * @param model The script generator model to get the file handler from.
	 */
	public SaveScriptGeneratorFileMessageDialog(Shell parentShell, String dialogTitle, String defaultFilename, String defaultFilepath, String generatedScript, ScriptGeneratorSingleton model) {
		this.parentShell = parentShell;
		saveDialog = new FileDialog(parentShell, SWT.SAVE);
		saveDialog.setText(dialogTitle);
		saveDialog.setFilterExtensions(new String[] {"*" + ScriptGeneratorSingleton.PYTHON_EXT});
		saveDialog.setFilterPath(defaultFilepath);
		saveDialog.setFileName(defaultFilename);
		saveDialog.setOverwrite(true);
		this.generatedScript = generatedScript;
		this.model = model;
	}

	/**
	 * Opens the dialog and saves the file to the location specified by the user.
	 */
	public void open() {
		String filepath = saveDialog.open();
		
		if (filepath != null) {
			try {
				model.getFileHandler().generate(filepath, generatedScript);
				model.saveParameters(filepath);
			} catch (IOException e) {
				MessageDialog.openError(parentShell, "Error", "Failed to write generated script to file");
			} catch (NoScriptDefinitionSelectedException e) {
				MessageDialog.openError(parentShell, "Error", "No Script Definition selected");
			}
			
			String dialogMessage = "Script successfully saved to " + filepath + ". Would you like to open the script in an editor?";
			Boolean openInEditor = MessageDialog.openQuestion(parentShell, "Open in Editor", dialogMessage); 
			
			if (openInEditor) {
				try {
					model.getFileHandler().openFile(filepath);
				} catch (OpenFileException | IOException e) {
					MessageDialog.openWarning(parentShell, "Error", "Failed to open file: " + e.getMessage());
				}
			}
		}
	}
}

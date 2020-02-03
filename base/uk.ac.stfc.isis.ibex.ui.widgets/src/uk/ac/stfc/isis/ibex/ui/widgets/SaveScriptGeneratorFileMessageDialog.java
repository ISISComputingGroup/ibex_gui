package uk.ac.stfc.isis.ibex.ui.widgets;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import uk.ac.stfc.isis.ibex.logger.IsisLog;


/**
 * A message dialog box that allows you to copy the contents of the dialog and open the file the dialog is tied to.
 * 
 * @author https://stackoverflow.com/a/30630475 modified by James King
 *
 */
public class SaveScriptGeneratorFileMessageDialog extends MessageDialog {
	
	/**
	 * The filepath prefix to add to the front of the filename.
	 */
	private String filepathPrefix;
	
	/**
	 * The file to save the python generated script to.
	 */
	private String filename;
	
	/**
	 * The parent of the message dialog.
	 */
	private Shell parentShell;

	/**
	 * The generated script to write to file.
	 */
	private String generatedScript;
	
	/**
	 * The file extenstion for Python files.
	 */
	private String PYTHON_EXT = ".py";
	
	private static final Logger LOG = IsisLog.getLogger(SaveScriptGeneratorFileMessageDialog.class);

	/**
	 * Create the dialog box.
	 * 
	 * @param parentShell The shell that the dialog sits in.
	 * @param dialogTitle The title of the dialog box.
	 * @param dialogTitleImage The image that goes with the title.
	 * @param dialogMessage The message that sits prepended to the filepath.
	 * @param filepathPrefix The path to the file that we can open in this dialog.
	 * @param dialogImageType one of the following values:
	 *                           <ul>
	 *                           <li><code>MessageDialog.NONE</code> for a dialog
	 *                           with no image</li>
	 *                           <li><code>MessageDialog.ERROR</code> for a dialog
	 *                           with an error image</li>
	 *                           <li><code>MessageDialog.INFORMATION</code> for a
	 *                           dialog with an information image</li>
	 *                           <li><code>MessageDialog.QUESTION </code> for a
	 *                           dialog with a question image</li>
	 *                           <li><code>MessageDialog.WARNING</code> for a dialog
	 *                           with a warning image</li>
	 *                           </ul>
	 * @param dialogButtonLabels an array of labels for the buttons in the button
	 *                           bar
	 * @param defaultIndex       the index in the button label array of the default
	 *                           button
	 * @param defaultFilename	 The default file name to save the script files with.
	 */
	public SaveScriptGeneratorFileMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, String filepathPrefix,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String defaultFilename, String generatedScript) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
		this.filepathPrefix = filepathPrefix;
		this.filename = defaultFilename;
		this.parentShell = parentShell;
		this.generatedScript = generatedScript;
	}

	/**
	 * Create the area of the message dialog with text in.
	 * 
	 * @param composite The composite parent to display the message area in.
	 */
	@Override
    protected Control createMessageArea(final Composite composite) {
		composite.setLayout(new GridLayout(3, false));
		
		Image image = getImage();
		if (image != null) {
			imageLabel = new Label(composite, SWT.NULL);
			image.setBackground(imageLabel.getBackground());
			imageLabel.setImage(image);

			imageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		}
		
		// Create filename edit area
		
		Label filenameLabel = new Label(composite, SWT.NONE);
		
		filenameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		filenameLabel.setText("File name: ");
			
		Text msg = new Text(composite, SWT.BORDER);
		
		msg.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		msg.setText(filename);
		
		msg.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				filename = msg.getText();
			}
		});

		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);

		msg.setLayoutData(data);

		return composite;
	}
	
	/**
	 * Create a save and, save and open file button to place in the dialog
	 * 
	 * @param parent The parent to put the buttons in.
	 */
	@Override 
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		// create save button
		Button saveFileButton = createButton(parent, IDialogConstants.OK_ID, "Save File", false);
		saveFileButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		saveFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			boolean fileWritten = generateWithOverwriteAndNameCheck(generatedScript, PYTHON_EXT);
			if(fileWritten) {
				this.close();
			} else {
				this.open();
			}
		}));
		// create save and open file button
		Button saveOpenFileButton = createButton(parent, IDialogConstants.OK_ID, "Save and Open File", true);
		saveOpenFileButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		saveOpenFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			boolean fileWritten = generateWithOverwriteAndNameCheck(generatedScript, PYTHON_EXT);
			if(fileWritten) {
				openFile(PYTHON_EXT);
				this.close();
			} else {
				this.open();
			}
		}));
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * With a check to see if we are overwriting a file.
	 * 
	 * @param toWrite The string to write to file
	 * @param fileExtension The file extension to save 
	 * @return true if file written, false if not.
	 */
	private boolean generateWithOverwriteAndNameCheck(String toWrite, String fileExtension) {
		// Don't generate if filename contains extension or file path.
		if(!isFilenameValid()) {
			MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Cannot save", "Cannot save: filename contains a ., ;, / or \\");
			return false;
		}
		File scriptFile = new File(filepathPrefix + filename + fileExtension);
		try {
			if(scriptFile.createNewFile()) {
				// There was no file preventing creation so we are not overwriting
				writeToFile(toWrite, scriptFile);
				return true;
			} else {
				// There was a file preventing creation, check to see if we wish to overwrite
				boolean overwriteChosen = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
						"File already exists",
						"File already exists, would you like to overwite?");
				if(overwriteChosen) {
					writeToFile(toWrite, scriptFile);
					return true;
				} else {
					return false;
				}
			}
		} catch(IOException e) {
			LOG.error("Failed to write generated file");
			LOG.error(e);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Failed to write generated script to file");
			return false;
		}
	}
	
	/**
	 * Checks if the filename contains a ., :, / or \
	 * 
	 * @return true if filename does not contain these, or false if it does.
	 */
	private boolean isFilenameValid() {
		return !(filename.contains(".") || filename.contains(":") || filename.contains("/") || filename.contains("\\"));
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * 
	 * @param toWrite The string to write to file
	 * @param scriptFile The file to write to
	 */
	private void writeToFile(String toWrite, File scriptFile) throws IOException {
		try (BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(scriptFile))) {
			scriptWriter.write(toWrite);
			scriptWriter.flush();
		}
	}
	
	/**
	 * Open the file specified in this dialog by the filepathPrefix, filename and passed fileExtension in notepad++.
	 * 
	 * @param fileExtension The file extension of the file to open.
	 */
	private void openFile(String fileExtension) {
		File file = new File(filepathPrefix + filename + fileExtension);
		try {
			if(file.exists()) {
				Runtime rs = Runtime.getRuntime();
				String notepadExe = findNotepadExe();
				rs.exec(String.format("%s %s", notepadExe, file));
			} else {
				String notepadLaunchWarning = "Could not launch notepad++, file does not exist";
				LOG.info(notepadLaunchWarning);
				handleFailedToOpenFile(notepadLaunchWarning, file);
			}
		} catch(IOException e) {
			LOG.catching(e);
			handleFailedToOpenFile(e.getMessage(), file);
		}
	}
	
	/**
	 * Find the notepad executable so we can launch it to open the file.
	 * 
	 * @return The location of notepad.exe
	 * @throws IOException If we fail to find notepad throw this
	 */
	private String findNotepadExe() throws IOException {
		String[] possibleLocations = {"C:\\Program Files\\Notepad++", "C:\\Program Files (x86)\\Notepad++"};
		for(String location : possibleLocations) {
			File directory = new File(location);
			if(directory.exists()) {
				File[] possibleFiles = directory.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return name.equals("notepad++.exe");
					}
				});
				if(possibleFiles.length > 0) {
					return possibleFiles[0].getAbsolutePath();
				}
			}
		}
		throw new IOException("Failed to find notepad to launch");
	}
		
	
	/**
	 * Handle the case where we fail to open the file by showing a warning to the user.
	 * 
	 * @param message The warning message to display to the user.
	 * @param file The file that failed to open.
	 */
	private void handleFailedToOpenFile(String message, File file) {
		LOG.error("Failed to open file " + file.getAbsolutePath());
		Display.getDefault().asyncExec( () -> {
			MessageDialog.openWarning(parentShell, "Error", "Failed to open file: " + file.getAbsolutePath() + "\n" + message);
		});
	}
		
	
	/**
	 * Open the information dialog.
	 * 
	 * @param parent The shell parent for the dialog.
	 * @param title The title for the dialog box.
	 * @param message The message to display in front of the filepath for the box.
	 * @param filepath The filepath of the file this box refers to.
	 * @param defaultFilename The default filename to save the script with
	 * @param generatedScript 
	 */
	public static void openInformation(Shell parent, String title,  String message, String filepath, String defaultFilename, String generatedScript) {
		SaveScriptGeneratorFileMessageDialog dialog = new SaveScriptGeneratorFileMessageDialog(parent, title, null,  message, filepath, 
				INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0, defaultFilename, generatedScript);

		dialog.open();
	}
	
}

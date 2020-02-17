package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.IOException;

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

import uk.ac.stfc.isis.ibex.scriptgenerator.CheckForOverwriteException;
import uk.ac.stfc.isis.ibex.scriptgenerator.FileGeneratorException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;


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
	 * The generated script to write to file.
	 */
	private String generatedScript;
	
	/**
	 * The file extenstion for Python files.
	 */
	private String PYTHON_EXT = ".py";
	
	/**
	 * The script generator model.
	 */
	private ScriptGeneratorSingleton model;
	
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
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String defaultFilename, String generatedScript, ScriptGeneratorSingleton model) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
		this.filepathPrefix = filepathPrefix;
		this.filename = defaultFilename;
		this.generatedScript = generatedScript;
		this.model = model;
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
			fileSaveHandler(false);
		}));
		// create save and open file button
		Button saveOpenFileButton = createButton(parent, IDialogConstants.OK_ID, "Save and Open File", true);
		saveOpenFileButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		saveOpenFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			fileSaveHandler(true);
		}));
	}		
	
	private void fileSaveHandler(boolean openFile) {
		// Detect whether the file has been written or not
		boolean fileWritten = false;
		
		// Attempt to save the generated script
		try {
			model.getFileHandler().generateWithOverwriteCheck(filepathPrefix, filename, generatedScript, PYTHON_EXT);
			fileWritten = true;
		} catch(CheckForOverwriteException e) {
			// Ask the user if they want to overwrite and try again
			boolean overwriteChosen = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
				"File already exists",
				"File already exists, would you like to overwite?");
			if(overwriteChosen) {
				// We have asked to overwrite to attempt to do so
				try {
					model.getFileHandler().generateWithoutOverwriteCheck(filepathPrefix, filename, generatedScript, PYTHON_EXT);
					fileWritten = true;
				} catch (FileGeneratorException | IOException e1) {
					MessageDialog.openError(Display.getDefault().getActiveShell(),
							"Error", "Failed to write generated script to file");
				}
			}
		} catch(FileGeneratorException e) {
			MessageDialog.openWarning(
					Display.getDefault().getActiveShell(),
					"Cannot save", "Cannot save: filename contains a . ; / or \\");
		} catch(IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Failed to write generated script to file");
		}
		
		// Only close the dialog if we have succesfully written to file
		if(fileWritten) {
			if(openFile) {
				model.getFileHandler().openFile(filepathPrefix, filename, PYTHON_EXT);
			}
			this.close();
		} else {
			this.open();
		}
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
	public static void openInformation(Shell parent, String title,  String message, String filepath, String defaultFilename, String generatedScript,
			ScriptGeneratorSingleton model) {
		SaveScriptGeneratorFileMessageDialog dialog = new SaveScriptGeneratorFileMessageDialog(parent, title, null,  message, filepath, 
				INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0, defaultFilename, generatedScript, model);

		dialog.open();
	}
	
}

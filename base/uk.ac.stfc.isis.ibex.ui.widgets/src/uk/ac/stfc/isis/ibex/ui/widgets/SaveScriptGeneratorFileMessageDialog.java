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
		filename = filepathPrefix+defaultFilename;
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
		Image image = getImage();
		if (image != null) {
			imageLabel = new Label(composite, SWT.NULL);
			image.setBackground(imageLabel.getBackground());
			imageLabel.setImage(image);

			imageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, false, false));
		}
			
		// Create filename edit area
		
		Label filenameLabel = new Label(composite, SWT.NONE);
		
		filenameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		filenameLabel.setText("File name: ");
			
		Text msg = new Text(composite, SWT.NONE);

		msg.setText(filename);
		
		msg.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				filename = filepathPrefix + e.data;
				System.out.println(filename);
			}
		});

		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);

		msg.setLayoutData(data);

		return composite;
	}
	
	/**
	 * Create an save and, save and open file button to place in the dialog
	 * 
	 * @param parent The parent to put the buttons in.
	 */
	@Override 
	protected void createButtonsForButtonBar(Composite parent) {
		// create save button
		Button saveFileButton = createButton(parent, IDialogConstants.OK_ID, "Save File", true);
		saveFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			generate();
			this.close();
		}));
		// create save and open file button
		Button saveOpenFileButton = createButton(parent, IDialogConstants.OK_ID, "Save and Open File", true);
		saveOpenFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			generate();
			openFile();
			this.close();
		}));
	}
	
	/**
	 * Save a generated script to file.
	 */
	public void generate() {
		File scriptFile = new File(filename);
		try (BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(scriptFile))) {
			scriptWriter.write(generatedScript);
			scriptWriter.flush();
		} catch(IOException e) {
			LOG.error("Failed to write generated file");
			LOG.error(e);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Failed to write generated script to file");
		}
	}
	
	private void openFile() {
		File file = new File(filename);
		try {
			if(file.exists()) {
				Runtime rs = Runtime.getRuntime();
				String notepadExe = findNotepadExe();
				rs.exec(String.format("%s %s", notepadExe, file));
			} else {
				String notepadLaunchWarning = "Could not launch notepad++";
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
	 */
	public static void openInformation(Shell parent, String title,  String message, String filepath, String defaultFilename, String generatedScript) {
		SaveScriptGeneratorFileMessageDialog dialog = new SaveScriptGeneratorFileMessageDialog(parent, title, null,  message, filepath, 
				INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0, defaultFilename, generatedScript);

		dialog.open();
	}
	
}

package uk.ac.stfc.isis.ibex.ui.widgets;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.awt.Desktop;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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
public class FileMessageDialog extends MessageDialog {
	
	/**
	 * The file to open with the openFileButton.
	 */
	private File file;
	
	/**
	 * The parent of the message dialog.
	 */
	private Shell parentShell;
	
	private static final Logger LOG = IsisLog.getLogger(FileMessageDialog.class);

	/**
	 * Create the dialog box.
	 * 
	 * @param parentShell The shell that the dialog sits in.
	 * @param dialogTitle The title of the dialog box.
	 * @param dialogTitleImage The image that goes with the title.
	 * @param dialogMessage The message that sits prepended to the filepath.
	 * @param filepath The path to the file that we can open in this dialog.
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
	 */
	public FileMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, String filepath,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage+filepath, dialogImageType, dialogButtonLabels, defaultIndex);
		file = new File(filepath);
		this.parentShell = parentShell;
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

		// Use Text control for message to allow copy

		if (message != null) {
			Text msg = new Text(composite, SWT.READ_ONLY | SWT.MULTI);

			msg.setText(message);

			GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);

			msg.setLayoutData(data);
		}

		return composite;
	}
	
	/**
	 * Create an ok and open file button to place in the dialog
	 * 
	 * @param parent The parent to put the buttons in.
	 */
	@Override 
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK button as default does
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		// create Button to open file
		Button openFileButton = createButton(parent, IDialogConstants.OK_ID, "Open File", false);
		// Open the file if possible, if not notify the user
		openFileButton.addSelectionListener(widgetSelectedAdapter(event -> {
			try {
				if(file.exists() && Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
				} else {
					handleFailedToOpenFile();
				}
			} catch(IOException e) {
				LOG.catching(e);
				handleFailedToOpenFile();
			}
		}));
	}
		
	
	/**
	 * Handle the case where we fail to open the file by showing a warning to the user.
	 */
	private void handleFailedToOpenFile() {
		LOG.error("Failed to open file " + file.getAbsolutePath());
		Display.getDefault().asyncExec( () -> {
			MessageDialog.openWarning(parentShell, "Error", "Failed to open file: " + file.getAbsolutePath());
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
	public static void openInformation(Shell parent, String title,  String message, String filepath) {
		FileMessageDialog dialog = new FileMessageDialog(parent, title, null,  message, filepath, 
				INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0);

		dialog.open();
	}
	
}
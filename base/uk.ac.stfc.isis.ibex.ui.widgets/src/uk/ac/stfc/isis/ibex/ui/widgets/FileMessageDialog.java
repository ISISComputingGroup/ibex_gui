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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import uk.ac.stfc.isis.ibex.logger.IsisLog;


/**
 * A message dialog box that allows you to copy the contents of the dialog and open the file the dialog is tied to.
 * 
 * @author James King
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

	public FileMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, String filepath,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage+filepath, dialogImageType, dialogButtonLabels, defaultIndex);
		file = new File(filepath);
		this.parentShell = parentShell;
	}

	/**
	 * Create the area of the message dialog with text in.
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
	 * Handle the case where we fail to open the file.
	 */
	private void handleFailedToOpenFile() {
		LOG.error("Failed to open file " + file.getAbsolutePath());
		MessageDialog.openWarning(parentShell, "Error", "Failed to open file: " + file.getAbsolutePath());
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A message dialog box that allows you to copy the contents
 * @author ltu34219
 *
 */
public class MessageDialogWithCopy extends MessageDialog {


	public MessageDialogWithCopy(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
	}

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
	
	public static void openInformation(Shell parent, String title,  String message) {
		MessageDialogWithCopy dialog = new MessageDialogWithCopy(parent, title, null,  message, 
				INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0);

		dialog.open();
	}
	
}

package uk.ac.stfc.isis.ibex.ui.help;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

/**
 * A dialog box showing information about the client.
 */
public class ClientAboutDialogBox extends AboutDialogBox {
	Image image;
	private static final Logger LOG = IsisLog.getLogger(ClientAboutDialogBox.class);

	/**
	 * Creates the dialog box.
	 * @param parentShell the parent shell
	 */
	public ClientAboutDialogBox(Shell parentShell) {
		super(parentShell, "IBEX");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		try {
			image = new Image(null, new FileInputStream(
					"C:\\Instrument\\Dev\\ibex_gui\\base\\uk.ac.stfc.isis.ibex.ui.help\\icon\\splash200x132.bmp"));

			Label imageSet = new Label(parent, SWT.NONE);
			imageSet.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
			imageSet.setImage(image);
			

		} catch (FileNotFoundException e) {
			
			LOG.error(e);
		}

		setTitle("About IBEX");

		Composite container = super.superCreateDialogArea(parent);

		container.setLayout(new GridLayout(1, false));
		
		new ClientVersionPanel(container, SWT.NONE);

		return container;
	}

}

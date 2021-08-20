package uk.ac.stfc.isis.ibex.ui.help;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.GC;

import uk.ac.stfc.isis.ibex.ui.about.AboutDialogBox;

public class ClientAboutDialogBox extends AboutDialogBox {
	Image image;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("About IBEX");

		Composite container = super.superCreateDialogArea(parent);

		container.setLayout(new GridLayout(1, false));
		
		new ClientVersionPanel(container, SWT.NONE);

		return container;
	}

}

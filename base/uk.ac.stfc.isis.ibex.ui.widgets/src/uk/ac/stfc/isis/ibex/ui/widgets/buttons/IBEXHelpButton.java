package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

/**
 * A help icon button that opens browser links.
 *
 */
public class IBEXHelpButton extends IBEXButton {

	/**
	 * 
	 * @param parent
	 * @param link
	 * @param tooltip
	 */
	public IBEXHelpButton(Composite parent, String link, String tooltip) {
		super(parent, SWT.PUSH);
		
		String description = String.format("Open user manual link in browser for help with '%s': \n%s", tooltip, link);
		Image image = ResourceManager.getPluginImage(SYMBOLIC_PATH, "/icons/helpIcon.png");
		
		this.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
		.link(link)
		.tooltip(description)
		.image(image);
	}
}

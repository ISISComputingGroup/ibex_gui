package uk.ac.stfc.isis.ibex.ui.dae;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.dae.perspective"; //$NON-NLS-1$

	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "DAE";
	}

	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/server16_32x24.png");
		// Icon made by Freepik http://www.flaticon.com/authors/freepik
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}

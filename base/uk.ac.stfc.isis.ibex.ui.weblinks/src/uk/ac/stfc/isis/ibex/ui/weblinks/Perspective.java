package uk.ac.stfc.isis.ibex.ui.weblinks;


import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.weblinks.perspective"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
	}
	
	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "Web Links";
	}
	
	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.weblinks", "icons/external1_32x24.png");
		// Icon made by Dave Gandy http://www.flaticon.com/authors/dave-gandy
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}
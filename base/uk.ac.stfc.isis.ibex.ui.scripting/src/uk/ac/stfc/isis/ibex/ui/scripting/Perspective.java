package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scripting.perspective"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		Consoles.getDefault().createConsole();
	}
	
	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "Scripting";
	}
	
	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scripting", "icons/script_32x24.png");
		// Icon made by Freepik http://www.flaticon.com/authors/freepik
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}

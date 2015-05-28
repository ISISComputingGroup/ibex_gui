package uk.ac.stfc.isis.ibex.ui.synoptic;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;
import uk.ac.stfc.isis.ibex.ui.synoptic.views.OpiTargetView;

public class InstrumentPerspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.instrument.perspective"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		lockView(layout, OpiTargetView.ID);
		lockView(layout, IPageLayout.ID_PROP_SHEET);
	}
	
	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "Synoptic";
	}

	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.synoptic", "icons/earth16_32x24.png");
		// Icon made by Icomoon http://www.flaticon.com/authors/icomoon
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
}

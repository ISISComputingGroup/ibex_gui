package uk.ac.stfc.isis.ibex.ui.databrowser;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPageLayout;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.databrowser.perspective"; //$NON-NLS-1$
	
	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "Data Browser";
	}

	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.databrowser", "icons/stocks2_32x24.png");
		// Icon made by OCHA http://www.flaticon.com/authors/ocha
		// from Flaticon http://www.flaticon.com
		// is licensed by Creative Commons BY 3.0 http://creativecommons.org/licenses/by/3.0/
	}
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		layout.setEditorAreaVisible(true);
		lockView(layout, "org.csstudio.trends.databrowser.waveformview.WaveformView");		
	}
}

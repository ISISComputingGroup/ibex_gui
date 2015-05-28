package uk.ac.stfc.isis.ibex.ui.beamstatus;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.BasePerspective;

public class Perspective extends BasePerspective {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.perspective"; //$NON-NLS-1$

	@Override
	public String ID() {
		return ID;
	}

	@Override
	public String name() {
		return "Beam Status";
	}

	@Override
	public Image image() {
		return ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.beamstatus", "icons/status_32x24.png");
	}
}

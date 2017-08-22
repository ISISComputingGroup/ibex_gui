package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;

public class PerspectivesProvider {

	public ArrayList<PerspectiveInfo> getPerspectives() {
		ArrayList<PerspectiveInfo> perspectives = new ArrayList<PerspectiveInfo>();
		perspectives.add(new PerspectiveInfo("Alarms", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.alarms", true, "uk.ac.stfc.isis.ibex.e4.ui", "perspectiveIcons/alarms.png"));
		return perspectives;
	}

}

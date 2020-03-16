package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

public class TableOfMotors17to24 extends TableOfMotorsOpiTargetView {
	@Override
    protected String getViewName() {
    	return "Motors 17-24";
    }
	
	@Override
	protected List<Integer> getControllerIndices() {
		return List.of(17, 18, 19, 20, 21, 22, 23, 24);
	}
}

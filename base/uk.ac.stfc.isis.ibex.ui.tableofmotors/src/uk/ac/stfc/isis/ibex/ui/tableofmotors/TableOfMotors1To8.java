package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

public class TableOfMotors1To8 extends TableOfMotorsOpiTargetView {
	@Override
    protected String getViewName() {
    	return "Motors 1-8";
    }
	
	@Override
	protected List<Integer> getControllerIndices() {
		return List.of(1, 2, 3, 4, 5, 6, 7, 8);
	}
}

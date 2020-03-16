package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

public class TableOfMotors9to16 extends TableOfMotorsOpiTargetView {
	@Override
    protected String getViewName() {
    	return "Motors 9-16";
    }
	
	@Override
	protected List<Integer> getControllerIndices() {
		return List.of(9, 10, 11, 12, 13, 14, 15, 16);
	}
}

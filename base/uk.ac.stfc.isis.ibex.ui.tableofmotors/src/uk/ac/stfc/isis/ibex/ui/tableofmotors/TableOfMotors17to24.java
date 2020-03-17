package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

/**
 * OPI view for motor controllers 17 to 24.
 */
public class TableOfMotors17to24 extends TableOfMotorsOpiTargetView {
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected String getViewName() {
    	return "Motor controllers 17-24";
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Integer> getMotorControllers() {
		return List.of(17, 18, 19, 20, 21, 22, 23, 24);
	}
}

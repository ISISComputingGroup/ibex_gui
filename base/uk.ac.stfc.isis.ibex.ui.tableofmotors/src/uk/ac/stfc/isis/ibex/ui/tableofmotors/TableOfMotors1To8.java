package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

/**
 * OPI view for motor controllers 1 to 8.
 */
public class TableOfMotors1To8 extends TableOfMotorsOpiTargetView {
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected String getViewName() {
    	return "Motor controllers 1-8";
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Integer> getMotorControllers() {
		return List.of(1, 2, 3, 4, 5, 6, 7, 8);
	}
}

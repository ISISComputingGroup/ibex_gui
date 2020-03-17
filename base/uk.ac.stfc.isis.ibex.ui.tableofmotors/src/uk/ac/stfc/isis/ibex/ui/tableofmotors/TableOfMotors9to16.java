package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import java.util.List;

/**
 * OPI view for motor controllers 9 to 16.
 */
public class TableOfMotors9to16 extends TableOfMotorsOpiTargetView {
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected String getViewName() {
    	return "Motor controllers 9-16";
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Integer> getMotorControllers() {
		return List.of(9, 10, 11, 12, 13, 14, 15, 16);
	}
}

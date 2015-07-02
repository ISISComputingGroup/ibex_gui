package uk.ac.stfc.isis.ibex.ui.motor.views;

import uk.ac.stfc.isis.ibex.motor.Motors;

/**
 * Shows additional motors for LARMOR, for the mini-stages. 
 * <p>
 * This should not be needed once the table of motors is made more configurable.
 * </p>
 */
public class AdditionalMotorsView extends AllMotorsView {
	
	@Override
	protected void setMotorsTable() {
		this.motorsTable = Motors.getInstance().getAdditionalMotorsTable();
	}
}

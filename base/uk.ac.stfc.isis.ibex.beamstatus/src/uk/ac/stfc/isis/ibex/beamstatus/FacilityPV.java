package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A class to create facility PV objects
 *
 */
public class FacilityPV {

	public String pv;
	public UpdatedValue<String> updatedValue;

	/**
	 * Constructor for creating a facility PV
	 * 
	 * @param PV           Pv address
	 * @param updatedValue Value of the pv
	 */
	public FacilityPV(String PV, UpdatedValue<String> updatedValue) {
		this.pv = PV;
		this.updatedValue = updatedValue;

	}

}

package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A class to create facility PV objects.
 *
 */
public class FacilityPV {

	/**
	 * The source PV address.
	 */
	public String pv;
	
	/**
	 * The object holding the latest PV value.
	 */
	public UpdatedValue<String> updatedValue;

	/**
	 * Constructor for creating a facility PV.
	 * 
	 * @param pv           Pv address
	 * @param updatedValue Value of the pv
	 */
	public FacilityPV(String pv, UpdatedValue<String> updatedValue) {
		this.pv = pv;
		this.updatedValue = updatedValue;

	}

}

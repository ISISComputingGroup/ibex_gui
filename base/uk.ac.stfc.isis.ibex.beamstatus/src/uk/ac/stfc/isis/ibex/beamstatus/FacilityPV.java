package uk.ac.stfc.isis.ibex.beamstatus;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class FacilityPV {
	
	public String pv;
	public UpdatedValue<String> updatedValue;
	
	
	
	public FacilityPV (String PV, UpdatedValue<String> updatedValue) {
		this.pv = PV;
		this.updatedValue = updatedValue;
				
	}

}

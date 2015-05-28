package uk.ac.stfc.isis.ibex.dae.updatesettings;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class UpdateSettings extends ModelObject {
	
	private int autosaveFrequency;
	private AutosaveUnit autosaveUnits = AutosaveUnit.FRAMES; 
	
	public int getAutosaveFrequency() {
		return autosaveFrequency;
	}

	public void setAutosaveFrequency(int value) {
		firePropertyChange("autosaveFrequency", autosaveFrequency, autosaveFrequency = value);
	}

	public AutosaveUnit getAutosaveUnits() {
		return autosaveUnits;
	}

	public void setAutosaveUnits(AutosaveUnit value) {
		firePropertyChange("autosaveUnits", autosaveUnits, autosaveUnits = value);
	}
}

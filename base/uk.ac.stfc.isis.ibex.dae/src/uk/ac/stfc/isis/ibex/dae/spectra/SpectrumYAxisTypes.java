package uk.ac.stfc.isis.ibex.dae.spectra;

import uk.ac.stfc.isis.ibex.ui.Utils;

public enum SpectrumYAxisTypes {
	/* Enum constant indicating the Y axis is a count rate, in counts per microsecond. */
	COUNT_RATE("Counts (/" + Utils.MU + "s)"),
	/* Enum constant indicating the Y axis is showing cumulative counts. */
	ABSOLUTE_COUNTS("Counts");
	
	private String name;
	
	private SpectrumYAxisTypes(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}

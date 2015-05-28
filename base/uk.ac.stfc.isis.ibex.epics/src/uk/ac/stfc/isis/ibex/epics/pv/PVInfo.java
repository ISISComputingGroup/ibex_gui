package uk.ac.stfc.isis.ibex.epics.pv;

/**
 * Stores the PV information for a PV
 *
 */
public final class PVInfo<T> {

	private final String pvAddress;
	private Class<T> type;

	public PVInfo(String pvAddress, Class<T> type) {
		this.pvAddress = pvAddress;
		this.type = type; 
	}
	
	public String address() {
		return pvAddress;
	}

	public Class<T> type() {
		return type;
	}
}


package uk.ac.stfc.isis.ibex.dashboard;

import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;

/**
 * PVs which the dashboard looks at.
 * 
 * These are not directly pointing at the DAE because some instruments (in particular, muons) see
 * a different set of information. Therefore these point at aliases which are loaded into INSTETC
 * from the instrument's configuration area.
 */
public enum DashboardPv {
	BANNER_LEFT("BANNER:LEFT"),
	BANNER_MIDDLE("BANNER:MIDDLE"),
	BANNER_RIGHT("BANNER:RIGHT"),
	TABLE_1_1("TAB:1:1"),
	TABLE_1_2("TAB:1:2"),
	TABLE_2_1("TAB:2:1"),
	TABLE_2_2("TAB:2:2"),
	TABLE_3_1("TAB:3:1"),
	TABLE_3_2("TAB:3:2");
	
	private final String pvSuffix;
	
	private static final String DASHBOARD_PVS = "CS:DASHBOARD";
	
	private DashboardPv(String pvSuffix) {
		this.pvSuffix = pvSuffix;
	}
	
	/**
	 * Get the PV containing the value of this dashboard field.
	 * @return the pv
	 */
	public String getValuePV() {
		return InstrumentUtils.addPrefix(String.format("%s:%s:VALUE", DASHBOARD_PVS, pvSuffix));
	}
	
	/**
	 * Get the PV containing the label of this dashboard field.
	 * @return the pv
	 */
	public String getLabelPV() {
		return InstrumentUtils.addPrefix(String.format("%s:%s:LABEL", DASHBOARD_PVS, pvSuffix));
	}
}

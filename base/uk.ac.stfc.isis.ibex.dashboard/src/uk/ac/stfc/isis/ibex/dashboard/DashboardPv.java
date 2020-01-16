package uk.ac.stfc.isis.ibex.dashboard;

import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;

public enum DashboardPv {
	BANNER_LEFT("BANNER:LEFT"),
	BANNER_MIDDLE("BANNER:MIDDLE"),
	BANNER_RIGHT("BANNER:RIGHT"),
	ROW1_COL1("TAB:1:1"),
	ROW1_COL2("TAB:1:2"),
	ROW2_COL1("TAB:2:1"),
	ROW2_COL2("TAB:2:2"),
	ROW3_COL1("TAB:3:1"),
	ROW3_COL2("TAB:3:2");
	
	private final String pvSuffix;
	
	private static final String DASHBOARD_PVS = "CS:DASHBOARD";
	
	private DashboardPv(String pvSuffix) {
		this.pvSuffix = pvSuffix;
	}
	
	public String getValuePV() {
		return InstrumentUtils.addPrefix(String.format("%s:%s:VALUE", DASHBOARD_PVS, pvSuffix));
	}
	
	public String getLabelPV() {
		return InstrumentUtils.addPrefix(String.format("%s:%s:LABEL", DASHBOARD_PVS, pvSuffix));
	}
}

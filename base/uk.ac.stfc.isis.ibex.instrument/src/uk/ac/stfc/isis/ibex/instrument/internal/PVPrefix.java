package uk.ac.stfc.isis.ibex.instrument.internal;

public class PVPrefix {

	private static final String INSTRUMENT_FORMAT = "IN:%s:";
	private static final String USER_FORMAT = "%s:%s:";
	public static final String NDX = "NDX";
	
	private final String prefix;
	
	public PVPrefix(String machineName, String userName) {
		prefix = isInstrument(machineName) 
				? instrumentPrefix(machineName) : userPrefix(machineName, userName);
	}
	
	public String get() {
		return prefix;
	}
	
	private boolean isInstrument(String machineName) {
		return machineName.startsWith(NDX);
	}

	private String userPrefix(String machineName, String userName) {
		return String.format(USER_FORMAT, machineName, userName);
	}

	private String instrumentPrefix(String machineName) {
		return String.format(INSTRUMENT_FORMAT, instrumentName(machineName));
	}

	// Strip off the NDX prefix
	private String instrumentName(String machineName) {
		return machineName.substring(NDX.length()).toUpperCase();
	}
}

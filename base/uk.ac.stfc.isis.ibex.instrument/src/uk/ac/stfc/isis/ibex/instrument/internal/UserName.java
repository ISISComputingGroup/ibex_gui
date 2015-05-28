package uk.ac.stfc.isis.ibex.instrument.internal;

public class UserName {

	public static String get() {
		return userName();
	}
	
	private static String userName() {
		return System.getProperty("user.name");
	}
}

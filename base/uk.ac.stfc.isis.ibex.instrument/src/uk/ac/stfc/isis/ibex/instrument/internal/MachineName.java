package uk.ac.stfc.isis.ibex.instrument.internal;

import java.net.UnknownHostException;

public class MachineName {
	
	public static String get() {
		return machineName();
	}
	
	private static String machineName() {
		java.net.InetAddress localMachine = null;
		try {
			localMachine = java.net.InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		
		return localMachine == null ? "" : localMachine.getHostName().toUpperCase();
	}
}

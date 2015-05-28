package uk.ac.stfc.isis.ibex.configserver;

import com.google.common.base.Strings;

public class ServerStatus {
	
	private String status;
	
	public String status() {
		return status;
	}
	
	public boolean isBusy() {
		return !Strings.isNullOrEmpty(status);
	}
	
	@Override
	public String toString() {
		return status;
	}
}

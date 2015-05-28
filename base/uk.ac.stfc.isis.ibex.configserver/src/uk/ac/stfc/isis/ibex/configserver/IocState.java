/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about an IOC
 * @author sjb99183
 *
 */
public class IocState extends ModelObject implements Comparable<IocState> {
	private final String name;
	private final boolean allowControl;

	private boolean isRunning;
	private String description;
	
	public IocState(String name, boolean isRunning, String description, boolean allowControl) {
		this.name = name;
		this.isRunning = isRunning;
		this.description = description;
		this.allowControl = allowControl;
	}
	
	public IocState(IocState other) {
		this(other.name, other.isRunning, other.description, other.allowControl);
	}	

	public String getName() {
		return name;
	}
	
	public boolean getIsRunning() {
		return isRunning;
	}
	
	public void setIsRunning(boolean isRunning) {
		firePropertyChange("isRunning", this.isRunning, this.isRunning = isRunning);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}
	
	public boolean getAllowControl() {
		return allowControl;
	}

	@Override
	public int compareTo(IocState arg0) {
		return name.compareTo(arg0.getName());
	}
}

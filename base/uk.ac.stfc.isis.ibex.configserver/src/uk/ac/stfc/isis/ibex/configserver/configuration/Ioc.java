
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.epics.observing.INamed;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Holds an IOC, and notifies any listeners set to changes to this class.
 * 
 * Contains the IOC name, if it autostarts, auto-restarts and the simulation
 * level. Contains lists of PVsets, PVDefaultValues and set Macros.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
public class Ioc extends ModelObject implements Comparable<Ioc>, INamed {

	private final String name;	
	
    /**
     * The IOC will be started along with the config and restarted when the
     * block server restarts.
     */
	private boolean autostart = true;

    /**
     * If the IOC is terminated unexpectedly, and autostart is also true, the
     * IOC will be restarted.
     */
	private boolean restart = true;

	private SimLevel simlevel = SimLevel.NONE;
	private Collection<PVSet> pvsets = new ArrayList<PVSet>();
	private Collection<PVDefaultValue> pvs = new ArrayList<PVDefaultValue>();
	private Collection<Macro> macros = new ArrayList<Macro>();
	private String component;
    private String host = "";
	
    /**
     * Create an IOC with a given name.
     * 
     * @param name
     *            The IOC name
     */
	public Ioc(String name) {
		this.name = name;
	}
	
    /**
     * Create an IOC with matching properties to another.
     * 
     * @param other
     *            The IOC to match
     */
	public Ioc(Ioc other) {
		this(other.getName());
		this.setAutostart(other.getAutostart());
		this.setSimLevel(other.getSimLevel());
		this.setRestart(other.getRestart());
		this.setComponent(other.getComponent());
		this.setHost(other.getHost());
		
		for (PVSet set : other.getPvSets()) {
			pvsets.add(new PVSet(set));
		}
		
		for (PVDefaultValue defaultValue : other.getPvs()) {
			pvs.add(new PVDefaultValue(defaultValue));
		}
		
		for (Macro macro : other.getMacros()) {
			macros.add(new Macro(macro));
		}
	}
	
    /**
     * @return The IOC name
     */
	@Override
    public String getName() {
		return name;
	}
		
    /**
     * @return Whether the IOC is set to auto-start
     */
	public boolean getAutostart() {
		return autostart;
	}
	
    /**
     * @param autostart
     *            Set whether the IOC should auto-start
     */
	public void setAutostart(boolean autostart) {
        firePropertyChange("autostart", this.autostart, this.autostart = autostart);
	}
	
    /**
     * @return Whether the IOC is set to auto-restart
     */
	public boolean getRestart() {
		return restart;
	}

    /**
     * @param restart Set whether the IOC should auto-restart
     */
	public void setRestart(boolean restart) {
		firePropertyChange("restart", this.restart, this.restart = restart);
	}
	
    /**
     * @return Get the IOC's simulation level
     */
	public SimLevel getSimLevel() {
		return Optional.ofNullable(simlevel).orElse(SimLevel.NONE);
	}

    /**
     * @param simlevel
     *            Set the IOC's simulation level
     */
	public void setSimLevel(SimLevel simlevel) {
		firePropertyChange("simLevel", this.simlevel, this.simlevel = simlevel);
	}
	
    /**
     * @return A collection of IOC macros
     */
	public Collection<Macro> getMacros() {
		return Optional.ofNullable(macros).orElseGet(ArrayList::new);
	}
	
    /**
     * @return A collection of IOC PV sets
     */
	public Collection<PVSet> getPvSets() {
		return Optional.ofNullable(pvsets).orElseGet(ArrayList::new);
	}

    /**
     * @return A collection of IOC PV values
     */
	public Collection<PVDefaultValue> getPvs() {
		return Optional.ofNullable(pvs).orElseGet(ArrayList::new);
	}

    /**
     * @param macros
     *            Set the IOC macros
     */
	public void setMacros(Collection<Macro> macros) {
		firePropertyChange("macros", this.macros, this.macros = macros);
	}
	
    /**
     * @param pvs
     *            Set the IOC PV values
     */
	public void setPvs(Collection<PVDefaultValue> pvs) {
		firePropertyChange("pvs", this.pvs, this.pvs = pvs);
	}

    /**
     * @param pvsets
     *            Set the IOC PV sets
     */
	public void setPvSets(Collection<PVSet> pvsets) {
		firePropertyChange("pvSets", this.pvsets, this.pvsets = pvsets);
	}

    /**
     * @param component
     *            Set the name of the component this block is part of.
     */
    public void setComponent(String component) {
        firePropertyChange("component", this.component, this.component = component);
    }

    /**
     * @return The name of the component this block is part of (if any).
     */
    public String getComponent() {
        return component;
    }

    /**
     * @return Whether this block is part of a component.
     */
	public boolean inComponent() {
		return !Strings.isNullOrEmpty(component);
	}
	
	@Override
	public int compareTo(Ioc other) {
		return name.compareTo(other.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ioc)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Ioc other = (Ioc) obj;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Gets the host that this IOC is running on.
	 * @return - the hostname
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Sets the host that this IOC should run on.
	 * @param host - the hostname
	 */
	public void setHost(String host) {
		firePropertyChange("host", this.host, this.host = host);
	}
}

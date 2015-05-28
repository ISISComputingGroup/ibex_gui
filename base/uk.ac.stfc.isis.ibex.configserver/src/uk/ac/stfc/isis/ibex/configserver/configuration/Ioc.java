package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.model.ModelObject;

import com.google.common.base.Strings;

public class Ioc extends ModelObject implements Comparable<Ioc> {

	private final String name;	
	
	private boolean autostart;
	private boolean restart;
	private SimLevel simlevel = SimLevel.NONE;
	
	private Collection<PVSet> pvsets = new ArrayList<PVSet>();
	private Collection<PVDefaultValue> pvs = new ArrayList<PVDefaultValue>();
	private Collection<Macro> macros = new ArrayList<Macro>();
	private String subConfig;
	
	public Ioc(String name) {
		this.name = name;
	}
	
	public Ioc(Ioc other) {
		this.name = other.getName();
		this.autostart = other.getAutostart();
		this.simlevel = other.getSimLevel();
		this.restart = other.getRestart();
		
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
	
	public String getName() {
		return name;
	}
		
	public boolean getAutostart() {
		return autostart;
	}
	
	public void setAutostart(boolean autostart) {
		firePropertyChange("shouldStart", this.autostart, this.autostart = autostart);
	}
	
	public boolean getRestart() {
		return restart;
	}
	
	public void setRestart(boolean restart) {
		firePropertyChange("restart", this.restart, this.restart = restart);
	}
	
	public SimLevel getSimLevel() {
		if (simlevel == null) {
			simlevel = SimLevel.NONE;
		}
		
		return simlevel;
	}

	public void setSimLevel(SimLevel simlevel) {
		firePropertyChange("simLevel", this.simlevel, this.simlevel = simlevel);
	}
	
	public Collection<Macro> getMacros() {
		if (macros == null) {
			macros = new ArrayList<>();
		}
		
		return macros;
	}
	
	public Collection<PVSet> getPvSets() {
		if (pvsets == null) {
			pvsets = new ArrayList<>();
		}
		
		return pvsets;
	}

	public Collection<PVDefaultValue> getPvs() {
		if (pvs == null) {
			pvs = new ArrayList<>();
		}
		
		return pvs;
	}

	public void setPvs(Collection<PVDefaultValue> pvs) {
		firePropertyChange("pvs", this.pvs, this.pvs = pvs);
	}

	public void setPvSets(Collection<PVSet> pvsets) {
		firePropertyChange("pvSets", this.pvsets, this.pvsets = pvsets);
	}

	public String getSubConfig() {
		return subConfig;
	}
	
	public boolean hasSubConfig() {
		return !Strings.isNullOrEmpty(subConfig);
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
}

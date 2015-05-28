package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePVSet;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;

// NB This class holds data coming from IOCS, so it lists the properties available to be set on an IOC, rather than those actually set in a config
public class IocParameters {

	private final boolean running;
	private List<Macro> macros = new ArrayList<Macro>();
	private List<AvailablePV> pvs = new ArrayList<AvailablePV>();
	private List<AvailablePVSet> pvsets = new ArrayList<AvailablePVSet>();

	public IocParameters(boolean running, Collection<Macro> macros, Collection<AvailablePV> pvs, Collection<AvailablePVSet> pvsets) {
		this.running = running;

		for (Macro macro : macros) {
			this.macros.add(macro);
		}
		
		for (AvailablePV pv : pvs) {
			this.pvs.add(pv);
		}
		
		for (AvailablePVSet pvset : pvsets) {
			this.pvsets.add(pvset);
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public Collection<Macro> getMacros() {
		return macros;
	}
	
	public Collection<AvailablePV> getPVs() {
		return pvs;
	}
	
	public Collection<AvailablePVSet> getPVSets() {
		return pvsets;
	}
}

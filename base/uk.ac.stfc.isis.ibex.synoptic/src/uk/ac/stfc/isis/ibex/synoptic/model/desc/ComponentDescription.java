package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;

@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComponentDescription {
	
	@XmlTransient
	private ComponentDescription parent;

	private String name;

	private ComponentType type;
	
	@XmlElement(name = "target", type = TargetDescription.class)
	private TargetDescription target;
	
	@XmlElementWrapper(name = "pvs")
	@XmlElement(name = "pv", type = PV.class)
	private List<PV> pvs = new ArrayList<>();
		
	@XmlElementWrapper(name = "components")
	@XmlElement(name = "component", type = ComponentDescription.class)
	private List<ComponentDescription> components = new ArrayList<>();
	
	public String name() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ComponentType type() {
		return type;
	}
	
	public TargetDescription target() {
		return target;
	}
	
	public void setType(ComponentType type) {
		this.type = type;
	}
	
	public void setTarget(TargetDescription target) {
		this.target = target;
	}
	
	public List<PV> pvs() {
		return pvs;
	}
	
	public void removePV(PV pv) {
		if (pv != null && pvs.contains(pv)) {
			pvs.remove(pv);
		}
	}
	
	public void addPV(PV pv) {
		if (pv != null) {
			pvs.add(0, pv);
		}
	}
	
	public void addPV(PV pv, int position) {
		if (pv != null) {
			pvs.add(position, pv);
		}
	}
	
	public void promotePV(PV pv) {
		if (pv != null && pvs.contains(pv)) {
			int position = pvs.indexOf(pv) - 1;
			if (position >= 0) {
				pvs.remove(pv);
				pvs.add(position, pv);
			}
		}
	}
	
	public void demotePV(PV pv) {
		if (pv != null && pvs.contains(pv)) {
			int position = pvs.indexOf(pv) + 1;
			if (position <= pvs.size()) {
				pvs.remove(pv);
				pvs.add(position, pv);
			}
		}
	}
	
	public List<ComponentDescription> components() {
		return components;
	}
	
	public void addComponent(ComponentDescription component) {
		components.add(component);
	}
	
	public void addComponent(ComponentDescription component, int index) {
		components.add(index, component);
	}
	
	public void removeComponent(ComponentDescription component) {
		components.remove(component);
	}
	
	public ComponentDescription getParent() {
		return parent;
	}

	public void setParent(ComponentDescription parent) {
		this.parent = parent;
	}
	
	public void processChildComponents() {
		for (ComponentDescription cd: components) {
			cd.setParent(this);
			cd.processChildComponents();
		}
	}
	
	public boolean hasChild(ComponentDescription child) {
		// Recursive function that returns true if child is in components
		for (ComponentDescription component : components()) {
			if (child == component || component.hasChild(child)) {
				return true;
			}
		}
		return false;
	}
}

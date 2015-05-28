package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

@XmlRootElement(name = "instrument")
@XmlAccessorType(XmlAccessType.FIELD)
public class InstrumentDescription {
	private String name;
	
	@XmlElementWrapper(name = "components")
	@XmlElement(name = "component", type = ComponentDescription.class)
	private ArrayList<ComponentDescription> components = new ArrayList<>();
	
	public String name() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public void processChildComponents() {
		for (ComponentDescription cd: components) {
			cd.setParent(null);
			cd.processChildComponents();
		}
	}
	
	public String getXmlDescription() throws JAXBException, SAXException {
		return XMLUtil.toXml(this);
	}
}

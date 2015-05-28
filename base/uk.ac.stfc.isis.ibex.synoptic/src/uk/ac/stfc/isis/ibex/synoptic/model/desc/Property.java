package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "targetproperty")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
	
	private String key;
	private String value;
	
	// Required for XML unmarshalling
	public Property() {
	}
	
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String key() {
		return key;
	}

	public String value() {
		return value;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Property)) {
			return false;
		}
		
		Property other = (Property) obj;
		return key.equals(other.key) && value.equals(other.value);
	}
	
	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}
}

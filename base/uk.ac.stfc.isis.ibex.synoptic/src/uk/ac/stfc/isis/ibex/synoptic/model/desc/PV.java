package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pv")
@XmlAccessorType(XmlAccessType.FIELD)
public class PV {

	@XmlElement(name = "displayname")
	private String displayName;

	private String address;
	
	@XmlElement(name = "recordtype")
	private RecordType recordType;
		
	public String displayName() {
		return displayName;
	}
	
	public String address() {
		return address;
	}
	
	public RecordType recordType() {
		return recordType;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PV)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
			
		PV other = (PV) obj;
		return displayName.equals(other.displayName()) 
				&& address.equals(other.address)
				&& recordType.equals(other.recordType);		
	}
	
	@Override
	public int hashCode() {
		return displayName.hashCode() ^ address.hashCode() ^ recordType.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s @ %s", displayName, recordType.toString(), address);
	}
}

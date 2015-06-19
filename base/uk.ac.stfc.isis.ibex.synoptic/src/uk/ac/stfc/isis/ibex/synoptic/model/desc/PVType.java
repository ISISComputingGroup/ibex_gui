package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * ENUM of types and an associated display name for linking to PVs for the synoptic
 * BLOCK is commented as a block lookup is required, but am starting with the PVs
 *
 */
@XmlType(name = "pvtype")
@XmlEnum(String.class)
public enum PVType {
	LOCAL_PV ("Local PV"),
	REMOTE_PV ("Remote PV");
	
	private String displayName;
	
	private PVType(String displayName) {
		this.displayName = displayName;
	}
	
	public String display() {
		return displayName;
	}
	
	@Override
	public String toString () {
		return displayName;
	}
}

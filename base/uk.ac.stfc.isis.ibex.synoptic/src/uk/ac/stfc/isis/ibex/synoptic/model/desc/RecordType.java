package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recordtype")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordType {

	private IO io;
	
	public IO io() {
		return io;
	}
	
	public void setIO(IO io) {
		this.io = io;
	}
}

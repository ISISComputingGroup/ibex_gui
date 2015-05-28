package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "io")
@XmlEnum(String.class)
public enum IO {
	READ,
	WRITE
}

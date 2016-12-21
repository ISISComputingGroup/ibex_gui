
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

package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Holds the information relating to a synoptic PV, which covers its name,
 * address and whether it is read or write (recordType).
 */
@XmlRootElement(name = "pv")
@XmlAccessorType(XmlAccessType.FIELD)
public class PV extends ModelObject {

	@XmlElement(name = "displayname")
	private String displayName;

	private String address;
	
	@XmlElement(name = "recordtype")
	private RecordType recordType;

    /**
     * Default constructor, required due to existence of copy constructor.
     */
    public PV() {
    }

    /**
     * Constructor to create a PV based on some pre-existing values.
     * 
     * @param name
     *            the name of the PV
     * @param address
     *            the address of the PV
     * @param mode
     *            the read/write mode of the PV
     */
    public PV(String name, String address, IO mode) {
        setDisplayName(name);
        setAddress(address);
        recordType = new RecordType();
        recordType.setIO(mode);
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            PV to be copied
     */
    public PV(PV other) {
        this.displayName = other.displayName;
        this.address = other.address;
        this.recordType = other.recordType;
    }

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
        firePropertyChange("pvChanged", null, this);
	}
	
	public void setAddress(String address) {
		this.address = address;
        firePropertyChange("pvChanged", null, this);
	}
	
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
        firePropertyChange("pvChanged", null, this);
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
        return displayName.equals(other.displayName()) && address.equals(other.address())
                && recordType.io().equals(other.recordType.io());
	}
	
	@Override
	public int hashCode() {
        return displayName.hashCode() ^ address.hashCode() ^ recordType.hashCode();
	}
	
	@Override
	public String toString() {
        return String.format("%s: %s @ %s %s", displayName, recordType.toString(), address);
	}
}

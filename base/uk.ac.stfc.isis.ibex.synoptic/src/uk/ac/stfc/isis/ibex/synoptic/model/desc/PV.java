
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

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;

@XmlRootElement(name = "pv")
@XmlAccessorType(XmlAccessType.FIELD)
public class PV {

	@XmlElement(name = "displayname")
	private String displayName;

	private String address;
	
	@XmlElement(name = "recordtype")
	private RecordType recordType;
	
	@XmlElement(name = "pvtype")
	private PVType pvType;

    /**
     * Default constructor, required due to existence of copy constructor.
     */
    public PV() {
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
        this.pvType = other.pvType;
    }
		
	public PVType getPvType() {
		return pvType;
	}

	public void setPvType(PVType pvType) {
		this.pvType = pvType;
	}

	public String displayName() {
		return displayName;
	}
	
	public String address() {
		return address;
	}
	
	public String fullAddress() {
		String addressToUse = address;
		if (this.pvType != null) {
			switch (this.pvType) {
			case LOCAL_PV:
				String pvprefix = Instrument.getInstance().currentInstrument().pvPrefix();
				addressToUse = pvprefix + addressToUse;
				break;
			default:
				//Leave the address as that entered - could be remote
				break;
			}
		}
		return addressToUse;
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
				&& recordType.equals(other.recordType)
				&& pvType.equals(other.pvType);		
	}
	
	@Override
	public int hashCode() {
		return displayName.hashCode() ^ address.hashCode() ^ recordType.hashCode() ^ pvType.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s @ %s %s", displayName, recordType.toString(), address, pvType);
	}
}

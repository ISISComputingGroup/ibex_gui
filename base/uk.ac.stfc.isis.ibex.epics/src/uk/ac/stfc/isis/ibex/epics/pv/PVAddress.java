
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

package uk.ac.stfc.isis.ibex.epics.pv;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

/**
 * Represents a PV name.
 */
public final class PVAddress {
	
    /**
     * The colon character used in PV addresses.
     */
	public static final String COLON = ":";
	
	/**
	 * A dot character used in PV addresses.
	 */
	public static final String DOT = ".";
	
	private final String separator;
	private final List<String> terms = new ArrayList<>();
		
	private PVAddress(String prefix) {
		this(prefix, COLON);
	}

	private PVAddress(String prefix, String separator) {
		terms.add(prefix);
		this.separator = separator;
	}
	
	private PVAddress(String prefix, String separator, String term) {
		this(prefix, separator);
		terms.add(term);
	}
	
	/**
	 * Creates a PV address that starts with a prefix.
	 * 
	 * @param prefix the prefix for the address to start with.
	 * @return a new PVAddress with a prefix
	 */
	public static PVAddress startWith(String prefix) {
		return new PVAddress(prefix);
	}
	
	/**
	 * Appends a term to the PV address.
	 * 
	 * @param term to term to append to the address.
	 * @return the new PV Address with the term appended to it.
	 */
	public PVAddress append(String term) {
		return new PVAddress(this.toString(), this.separator, term);
	}
	
	/**
	 * Appends a field to a PV address.
	 * 
	 * @param value the field to append.
	 * @return the new PV address with a field appended to it.
	 */
	public PVAddress field(String value) {
		return changeSeparator(DOT).append(value).changeSeparator(separator);
	}
	
	/**
	 * Appends a term to the PV address and returns it as a string.
	 * 
	 * @param term the term to append to the address.
	 * @return the complete PV address as a string.
	 */
	public String endWith(String term) {
		return append(term).toString();
	}

	/**
     * Appends a field to the PV address and returns it as a string.
     * 
     * @param term the field to append to the address.
     * @return the complete PV address as a string.
     */
	public String endWithField(String term) {
		return field(term).toString();
	}
	
	@Override
	public String toString() {
		return Joiner.on(separator).join(terms);
	}
	
	private PVAddress changeSeparator(String separator) {
		return new PVAddress(this.toString(), separator);
	}
	
}

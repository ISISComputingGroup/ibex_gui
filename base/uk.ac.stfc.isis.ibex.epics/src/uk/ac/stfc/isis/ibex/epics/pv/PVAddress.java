
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
 * Represents a PV name
 */
public class PVAddress {
	
	public static final String COLON = ":";
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
	
	public static PVAddress startWith(String prefix) {
		return new PVAddress(prefix);
	}
	
	public PVAddress append(String term) {
		return new PVAddress(this.toString(), this.separator, term);
	}
	
	public PVAddress field(String value) {
		return changeSeparator(DOT).append(value).changeSeparator(separator);
	}
	
	public String endWith(String term) {
		return append(term).toString();
	}

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

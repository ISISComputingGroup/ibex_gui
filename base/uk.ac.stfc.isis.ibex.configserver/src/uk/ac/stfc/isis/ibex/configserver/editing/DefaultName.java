
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultName {

	private static final String OPTIONAL_NUMBER_AFTER_UNDERSCORE = "_?(\\d+)?";

	private final String name;
	private final Pattern namePattern;
	
	public DefaultName(String name) {
		this.name = name; 
		namePattern = Pattern.compile(name + OPTIONAL_NUMBER_AFTER_UNDERSCORE);
	}
	
	public String getUnique(Collection<String> existingNames) {
		return uniqueName(existingNames);
	}

	private String uniqueName(Collection<String> names) {
		Set<Integer> taken = new HashSet<>();
		for (String name : names) {
			Matcher match = namePattern.matcher(name);
			if (match.matches()) {
				String number = match.group(1);
				Integer count = number != null ? Integer.parseInt(number) : 0;
				taken.add(count);
			}
		}
		
		if (taken.isEmpty() || !taken.contains(0)) {
			return name;
		}
		
		return name + "_" + nextAvailable(taken);	
	}

	private String nextAvailable(Set<Integer> taken) {
		Integer i = 1;
		while (taken.contains(i)) {
			i++;
		}
		
		return i.toString();
	}
}

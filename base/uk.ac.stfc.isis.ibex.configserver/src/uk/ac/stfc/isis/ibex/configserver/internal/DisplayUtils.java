
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DisplayUtils {
	
	private static final String NONE = "none"; 
	private static final String OTHER = "Other";
	
	public static String renameGroup(String name) {
		return name.toLowerCase().equals(NONE) ? OTHER : name;
	}
	
	public static <T extends Group> Collection<T> removeOtherGroup(Collection<T> groups) {
		return Lists.newArrayList(Iterables.filter(groups, new Predicate<T>() {
			@Override
			public boolean apply(T group) {
				return !group.getName().equals(OTHER);
			}
		}));
	}
}

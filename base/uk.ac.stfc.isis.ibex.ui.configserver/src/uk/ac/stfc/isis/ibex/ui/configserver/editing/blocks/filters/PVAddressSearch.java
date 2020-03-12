
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

public class PVAddressSearch extends ViewerFilter {
	private Pattern searchPattern = Pattern.compile(".*", Pattern.CASE_INSENSITIVE);
	private boolean searchIsEmpty = true;
	
	public void setSearchText(String s) {
	    // ensure that the value can be used for matching
		this.searchIsEmpty = Strings.isNullOrEmpty(s);
	    this.searchPattern = Pattern.compile(".*" + s + ".*", Pattern.CASE_INSENSITIVE);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchIsEmpty) {
			return true;
		}
		
		PV p = (PV) element;
		
		Predicate<String> matcher = searchPattern.asMatchPredicate();
		
		if (matcher.test(p.getAddress()) || matcher.test(p.description())) {
			return true;
		}
		return false;
	}
}

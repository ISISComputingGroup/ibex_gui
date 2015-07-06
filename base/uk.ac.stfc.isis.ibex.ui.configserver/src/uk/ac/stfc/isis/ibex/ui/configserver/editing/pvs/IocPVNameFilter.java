
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;

public class IocPVNameFilter extends ViewerFilter {
	private String searchString = ".*";
	
	public void setSearchText(String s) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer arg0, Object arg1, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		AvailablePV p = (AvailablePV) element;
		if (p.getName().matches(searchString)) {
			return true;
		}
		if (p.getDescription().matches(searchString)) {
			return true;
		}
		return false;
	}

}

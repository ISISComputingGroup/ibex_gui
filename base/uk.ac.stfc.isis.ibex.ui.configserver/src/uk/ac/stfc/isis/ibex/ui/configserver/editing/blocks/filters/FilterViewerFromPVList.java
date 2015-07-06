
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

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

public class FilterViewerFromPVList extends ViewerFilter {
	
	private Collection<String> AllowedPVAddresses;
	
	public void setAllowedPVs(Collection<String> PVNames) {
		this.AllowedPVAddresses = PVNames;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (AllowedPVAddresses == null) {
			return true;
		}
		
		if (AllowedPVAddresses.isEmpty()) {
			return false;
		}
		
		PV p = (PV) element;
		String PVname = p.getAddress();
		
		if (AllowedPVAddresses.contains(PVname)) {
			return true;
		}
		
		return false;
	}

}

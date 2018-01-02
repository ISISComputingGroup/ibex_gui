
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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

/**
 * A PV filter that only allows PVs that belong to one of the specified IOCs.
 */
public class AssociatedPvViewerFilter extends ViewerFilter {
	
	private Collection<String> allowedIocNames = new ArrayList<String>();
	
	/**
	 * Set the list of allowed IOCs to display PVs from.
	 * @param iocNames the names of allowed IOCs.
	 */
	public void setAllowedIocs(Collection<String> iocNames) {
		this.allowedIocNames = iocNames;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		PV p = (PV) element;
		
		return allowedIocNames.contains(p.iocName());
	}

}

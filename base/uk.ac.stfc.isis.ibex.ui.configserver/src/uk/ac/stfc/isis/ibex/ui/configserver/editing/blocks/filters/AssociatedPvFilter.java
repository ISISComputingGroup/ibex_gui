
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

/**
 * PV filter that creates a viewer filter that will only give PVs that belong to the specified IOCs.
 */
public class AssociatedPvFilter extends PVFilter {
	
	private AssociatedPvViewerFilter filter = new AssociatedPvViewerFilter();
	
	/**
	 * Constructor, adds listener to all specified IOCs to update the filter when the IOCs change.
	 * 
	 * @param availableIocs A list of IOCs to allow the PVs from.
	 */
	public AssociatedPvFilter(final Collection<EditableIoc> availableIocs) {	
		updateIocList(availableIocs);
		
		//Add listener to all available IOCs as they may become part of the configuration
		for (Ioc ioc : availableIocs) {
			ioc.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					updateIocList(availableIocs);
					Display.getDefault().asyncExec(() -> firePropertyChange("refresh", false, true));
				}
			});
		}
	}
	
	private void updateIocList(Collection<EditableIoc> availableIocs) {
		Collection<String> iocNames = new ArrayList<String>();
		Collection<EditableIoc> usedIOCs = UsedIocFilter.filterIocs(availableIocs);
		for (EditableIoc ioc : usedIOCs) {
			iocNames.add(ioc.getName());
		}
		filter.setAllowedIocs(iocNames);
	}
	
	@Override
	public ViewerFilter getFilter() {
		return filter;
	}
}
